package melonystudios.behaviorapi.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.event.BehaviorTeleportEvent;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.behaviorapi.settings.IndividualSettings;
import melonystudios.reutilities.api.ReAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class TeleportEntityBehavior extends ItemBehavior {
    public static final MapCodec<TeleportEntityBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TeleportEntity.CODEC.forGetter(TeleportEntityBehavior::teleportEntity),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, TeleportEntityBehavior::new));
    private final TeleportEntity teleportEntity;

    public TeleportEntityBehavior(TeleportEntity teleportEntity, GlobalSettings settings) {
        super(teleportEntity, settings);
        this.teleportEntity = teleportEntity;
    }

    public TeleportEntityBehavior(TeleportEntity teleportEntity) {
        this(teleportEntity, GlobalSettings.defaults());
    }

    public TeleportEntityBehavior() {
        this(new TeleportEntity(16));
    }

    public TeleportEntity teleportEntity() {
        return this.teleportEntity;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        Vec3 pos = this.teleportEntity().position();
        RandomSource rand = world.getRandom();

        // spawns particles
        if (!isAllowedToTeleportTarget(livEntity, world)) return;
        for (int i = 0; i < 32; ++i) {
            world.addParticle(ParticleTypes.PORTAL, pos.x, pos.y + rand.nextDouble() * 2, pos.z, rand.nextGaussian(), 0, rand.nextGaussian());
        }
        if (world.isClientSide()) return;

        if (this.teleportEntity().teleportRandomly()) {
            teleportWithinDiameter(stack, world, livEntity, this.teleportEntity().diameter());
        } else {
            if (livEntity.isPassenger()) livEntity.unRide();

            // cancel the teleportation if the event was cancelled
            BehaviorTeleportEvent event = BehaviorAPI.exactTeleportThroughBehavior(stack, world, livEntity, pos);
            if (event.isCanceled()) return;

            if (livEntity instanceof ServerPlayer player && player.connection.isAcceptingMessages()) {
                livEntity.changeDimension(new DimensionTransition((ServerLevel) world, pos, livEntity.getDeltaMovement(), livEntity.getYRot(), livEntity.getXRot(), DimensionTransition.DO_NOTHING));
                livEntity.resetFallDistance();
                player.resetCurrentImpulseContext();
                world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
            } else {
                livEntity.changeDimension(new DimensionTransition((ServerLevel) world, pos, livEntity.getDeltaMovement(), livEntity.getYRot(), livEntity.getXRot(), DimensionTransition.DO_NOTHING));
                livEntity.resetFallDistance();
                world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
            }
        }
    }

    /// Teleports an entity to any valid position within the specified diameter.
    /// @param stack The item stack with the `reutilities:behaviors` component.
    /// @param world The world.
    /// @param livEntity The entity running the effect.
    /// @param diameter The diameter that the entity can teleport within.
    public static void teleportWithinDiameter(ItemStack stack, Level world, LivingEntity livEntity, float diameter) {
        RandomSource rand = livEntity.getRandom();
        for (int attempts = 0; attempts < 16; ++attempts) {
            double x = livEntity.getX() + (rand.nextDouble() - 0.5) * diameter;
            double y = Mth.clamp(
                    livEntity.getY() + (rand.nextInt((int) diameter) - (diameter / 2)),
                    world.getMinBuildHeight(),
                    world.getMinBuildHeight() + ((ServerLevel) world).getLogicalHeight() - 1
            );
            double z = livEntity.getZ() + (rand.nextDouble() - 0.5) * diameter;

            // cancel the teleportation if the event was cancelled
            BehaviorTeleportEvent event = BehaviorAPI.randomTeleportThroughBehavior(stack, world, livEntity, x, y, z, diameter);
            if (event.isCanceled()) return;

            if (!event.isCanceled() && livEntity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                world.gameEvent(GameEvent.TELEPORT, livEntity.position(), GameEvent.Context.of(livEntity));
                SoundEvent sound = SoundEvents.CHORUS_FRUIT_TELEPORT;
                SoundSource source = SoundSource.PLAYERS;

                if (livEntity instanceof Fox) {
                    sound = SoundEvents.FOX_TELEPORT;
                    source = SoundSource.NEUTRAL;
                }

                world.playSound(null, livEntity.getX(), livEntity.getY(), livEntity.getZ(), sound, source);
                livEntity.resetFallDistance();
                break;
            }
        }
    }

    /// Whether an entity is allowed to be teleported.
    /// @param livEntity The entity running the effect.
    /// @param world The world.
    public static boolean isAllowedToTeleportTarget(LivingEntity livEntity, Level world) {
        if (livEntity.level().dimension() == world.dimension()) {
            return livEntity.isAlive() && !livEntity.isSleeping();
        } else {
            return livEntity.canUsePortal(true);
        }
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flag) {
        super.addToTooltip(context, adder, flag);
        if (!flag.isAdvanced()) return;

        if (this.teleportEntity().teleportRandomly()) {
            adder.accept(Component.translatable("item_behavior.behaviorapi.teleport_entity.diameter", this.teleportEntity().diameter()).withStyle(ChatFormatting.GRAY));
        } else {
            Vec3 pos = this.teleportEntity().position();
            adder.accept(Component.translatable("item_behavior.behaviorapi.teleport_entity.position", pos.x, pos.y, pos.z).withStyle(ChatFormatting.GRAY));
        }
    }

    public record TeleportEntity(boolean teleportRandomly, float diameter, Vec3 position) implements IndividualSettings<TeleportEntity> {
        public static final MapCodec<TeleportEntity> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.fieldOf("teleport_randomly").forGetter(TeleportEntity::teleportRandomly),
                ReAPI.floatRange(1, 60000000).optionalFieldOf("diameter", 16F).forGetter(TeleportEntity::diameter),
                Vec3.CODEC.optionalFieldOf("position", Vec3.ZERO).forGetter(TeleportEntity::position)
        ).apply(instance, TeleportEntity::new));

        public TeleportEntity(float diameter) {
            this(true, diameter, Vec3.ZERO);
        }

        public TeleportEntity(Vec3 position) {
            this(false, 16, position);
        }

        @Override
        public MapCodec<TeleportEntity> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TeleportEntity> streamCodec() {
            return StreamCodec.composite(
                    ByteBufCodecs.BOOL, TeleportEntity::teleportRandomly,
                    ByteBufCodecs.FLOAT, TeleportEntity::diameter,
                    ReAPI.VEC3_STREAM_CODEC, TeleportEntity::position,
                    TeleportEntity::new
            );
        }
    }
}
