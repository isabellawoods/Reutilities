package melonystudios.behaviorapi.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.behaviorapi.settings.IndividualSettings;
import melonystudios.reutilities.api.ReAPI;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class PlaySoundBehavior extends ItemBehavior {
    public static final MapCodec<PlaySoundBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PlaySound.CODEC.forGetter(PlaySoundBehavior::playSound),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, PlaySoundBehavior::new));
    private final PlaySound playSound;

    public PlaySoundBehavior(PlaySound playSound, GlobalSettings settings) {
        super(playSound, settings);
        this.playSound = playSound;
    }

    public PlaySoundBehavior(PlaySound playSound) {
        this(playSound, GlobalSettings.defaults());
    }

    public PlaySoundBehavior() {
        this(new PlaySound(BehaviorAPI.PLAY_SOUND_DEFAULT, SoundSource.MASTER, Vec3.ZERO));
    }

    public PlaySound playSound() {
        return this.playSound;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        if (world.isClientSide()) return;

        Holder<SoundEvent> sound = Holder.direct(SoundEvent.createVariableRangeEvent(this.playSound.sound()));
        double rangeSquared = Mth.square(sound.value().getRange(this.playSound.volume()));
        long seed = world.getRandom().nextLong();

        double x = this.playSound.position(livEntity).x - livEntity.getX();
        double y = this.playSound.position(livEntity).y - livEntity.getY();
        double z = this.playSound.position(livEntity).z - livEntity.getZ();
        double range = x * x + y * y + z * z;
        Vec3 truePos = this.playSound.position(livEntity);
        float trueVolume = this.playSound.volume();

        if (range > rangeSquared) {
            if (this.playSound.minVolume() <= 0) return;

            double rangeRoot = Math.sqrt(range);
            truePos = new Vec3(livEntity.getX() + x / rangeRoot * 2, livEntity.getY() + y / rangeRoot * 2, livEntity.getZ() + z / rangeRoot * 2);
            trueVolume = this.playSound.minVolume();
        }

        // play the sound to the player/entity
        if (livEntity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSoundPacket(sound, this.playSound.source(), truePos.x, truePos.y, truePos.z, trueVolume, this.playSound.pitch(), seed));
        } else {
            world.playSound(null, truePos.x, truePos.y, truePos.z, sound.value(), this.playSound.source(), trueVolume, this.playSound.pitch());
        }
    }

    public record PlaySound(ResourceLocation sound, SoundSource source, Optional<Vec3> position, float volume, float pitch, float minVolume) implements IndividualSettings<PlaySound> {
        public static final MapCodec<PlaySound> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("sound").forGetter(PlaySound::sound),
                ReAPI.SOUND_SOURCE_CODEC.fieldOf("source").forGetter(PlaySound::source),
                Vec3.CODEC.optionalFieldOf("position").forGetter(PlaySound::position),
                ReAPI.floatRange(0, Float.MAX_VALUE).optionalFieldOf("volume", 1F).forGetter(PlaySound::volume),
                ReAPI.floatRange(0, 2).optionalFieldOf("pitch", 1F).forGetter(PlaySound::pitch),
                ReAPI.floatRange(0, 1).optionalFieldOf("min_volume", 1F).forGetter(PlaySound::minVolume)
        ).apply(instance, PlaySound::new));

        public PlaySound(ResourceLocation sound, SoundSource source, Vec3 position) {
            this(sound, source, Optional.of(position), 1, 1, 1);
        }

        public Vec3 position(LivingEntity target) {
            return this.position().orElse(target.position());
        }

        @Override
        public MapCodec<PlaySound> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PlaySound> streamCodec() {
            return StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, PlaySound::sound,
                    ReAPI.SOUND_SOURCE_STREAM_CODEC, PlaySound::source,
                    ByteBufCodecs.optional(ReAPI.VEC3_STREAM_CODEC), PlaySound::position,
                    ByteBufCodecs.FLOAT, PlaySound::volume,
                    ByteBufCodecs.FLOAT, PlaySound::pitch,
                    ByteBufCodecs.FLOAT, PlaySound::minVolume,
                    PlaySound::new
            );
        }
    }
}
