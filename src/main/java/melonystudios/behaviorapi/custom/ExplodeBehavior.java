package melonystudios.behaviorapi.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.behaviorapi.settings.IndividualSettings;
import melonystudios.reutilities.api.ReAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.IntFunction;

public class ExplodeBehavior extends ItemBehavior {
    public static final MapCodec<ExplodeBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Explode.CODEC.forGetter(ExplodeBehavior::explode),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, ExplodeBehavior::new));
    private final Explode explode;

    public ExplodeBehavior(Explode explode, GlobalSettings settings) {
        super(explode, settings);
        this.explode = explode;
    }

    public ExplodeBehavior(Explode explode) {
        this(explode, GlobalSettings.defaults());
    }

    public ExplodeBehavior() {
        this(new Explode(
                false,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Vec3.ZERO,
                0,
                false,
                Level.ExplosionInteraction.NONE,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.GENERIC_EXPLODE
        ));
    }

    @Nullable
    private DamageSource getDamageSource(LivingEntity target, Vec3 pos) {
        if (this.explode.damageType().isEmpty()) {
            return null;
        } else {
            return this.explode.attributeToTarget() ? new DamageSource(this.explode.damageType().get(), target) : new DamageSource(this.explode.damageType().get(), pos);
        }
    }

    public Explode explode() {
        return this.explode;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        if (world.isClientSide()) return;

        Vec3 pos = livEntity.position().add(this.explode.offset());
        world.explode(
                this.explode.attributeToTarget() ? livEntity : null,
                this.getDamageSource(livEntity, pos),
                new SimpleExplosionDamageCalculator(
                        this.explode.blockInteraction() != Level.ExplosionInteraction.NONE,
                        this.explode.damageType().isPresent(),
                        this.explode.knockbackMultiplier(),
                        this.explode.immuneBlocks()
                ),
                pos.x, pos.y, pos.z,
                Math.max(this.explode.radius(), 0),
                this.explode.createFire(),
                this.explode.blockInteraction(),
                this.explode.smallParticle(),
                this.explode.largeParticle(),
                this.explode.sound()
        );
    }

    public record Explode(
            boolean attributeToTarget,
            Optional<Holder<DamageType>> damageType,
            Optional<Float> knockbackMultiplier,
            Optional<HolderSet<Block>> immuneBlocks,
            Vec3 offset,
            float radius,
            boolean createFire,
            Level.ExplosionInteraction blockInteraction,
            ParticleOptions smallParticle,
            ParticleOptions largeParticle,
            Holder<SoundEvent> sound
    ) implements IndividualSettings<Explode> {
        private static final IntFunction<Level.ExplosionInteraction> BY_ID = ByIdMap.continuous(Enum::ordinal, Level.ExplosionInteraction.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Level.ExplosionInteraction> INTERACTION_STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
        public static final MapCodec<Explode> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("attribute_to_target", false).forGetter(Explode::attributeToTarget),
                DamageType.CODEC.optionalFieldOf("damage_type").forGetter(Explode::damageType),
                ReAPI.floatRange(0, Float.MAX_VALUE).optionalFieldOf("knockback_multiplier").forGetter(Explode::knockbackMultiplier),
                RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(Explode::immuneBlocks),
                Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(Explode::offset),
                ReAPI.floatRange(0, 128).fieldOf("radius").forGetter(Explode::radius),
                Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(Explode::createFire),
                Level.ExplosionInteraction.CODEC.fieldOf("block_interaction").forGetter(Explode::blockInteraction),
                ParticleTypes.CODEC.fieldOf("small_particle").forGetter(Explode::smallParticle),
                ParticleTypes.CODEC.fieldOf("large_particle").forGetter(Explode::largeParticle),
                SoundEvent.CODEC.fieldOf("sound").forGetter(Explode::sound)
        ).apply(instance, Explode::new));

        @Override
        public MapCodec<Explode> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Explode> streamCodec() {
            return new StreamCodec<>() {
                @Override
                @NotNull
                public Explode decode(RegistryFriendlyByteBuf buffer) {
                    boolean attributeToTarget = ByteBufCodecs.BOOL.decode(buffer);
                    Optional<Holder<DamageType>> damageType = ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(Registries.DAMAGE_TYPE)).decode(buffer);
                    Optional<Float> knockbackMultiplier = ByteBufCodecs.optional(ByteBufCodecs.FLOAT).decode(buffer);
                    Optional<HolderSet<Block>> immuneBlocks = ByteBufCodecs.optional(ByteBufCodecs.holderSet(Registries.BLOCK)).decode(buffer);
                    Vec3 offset = ReAPI.VEC3_STREAM_CODEC.decode(buffer);
                    float radius = ByteBufCodecs.FLOAT.decode(buffer);
                    boolean createFire = ByteBufCodecs.BOOL.decode(buffer);
                    Level.ExplosionInteraction blockInteraction = INTERACTION_STREAM_CODEC.decode(buffer);
                    ParticleOptions smallParticle = ParticleTypes.STREAM_CODEC.decode(buffer);
                    ParticleOptions largeParticle = ParticleTypes.STREAM_CODEC.decode(buffer);
                    Holder<SoundEvent> sound = SoundEvent.STREAM_CODEC.decode(buffer);

                    return new Explode(
                            attributeToTarget,
                            damageType,
                            knockbackMultiplier,
                            immuneBlocks,
                            offset,
                            radius,
                            createFire,
                            blockInteraction,
                            smallParticle,
                            largeParticle,
                            sound
                    );
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buffer, Explode explode) {
                    ByteBufCodecs.BOOL.encode(buffer, explode.attributeToTarget());
                    ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(Registries.DAMAGE_TYPE)).encode(buffer, explode.damageType());
                    ByteBufCodecs.optional(ByteBufCodecs.FLOAT).encode(buffer, explode.knockbackMultiplier());
                    ByteBufCodecs.optional(ByteBufCodecs.holderSet(Registries.BLOCK)).encode(buffer, explode.immuneBlocks());
                    ReAPI.VEC3_STREAM_CODEC.encode(buffer, explode.offset());
                    ByteBufCodecs.FLOAT.encode(buffer, explode.radius());
                    ByteBufCodecs.BOOL.encode(buffer, explode.createFire());
                    INTERACTION_STREAM_CODEC.encode(buffer, explode.blockInteraction());
                    ParticleTypes.STREAM_CODEC.encode(buffer, explode.smallParticle());
                    ParticleTypes.STREAM_CODEC.encode(buffer, explode.largeParticle());
                    SoundEvent.STREAM_CODEC.encode(buffer, explode.sound());
                }
            };
        }
    }
}
