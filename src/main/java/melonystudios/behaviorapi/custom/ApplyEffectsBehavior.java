package melonystudios.behaviorapi.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.settings.IndividualSettings;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.behaviorapi.ChanceEffectInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ApplyEffectsBehavior extends ItemBehavior {
    public static final MapCodec<ApplyEffectsBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ApplyEffects.CODEC.forGetter(ApplyEffectsBehavior::applyEffects),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, ApplyEffectsBehavior::new));
    private final ApplyEffects applyEffects;

    public ApplyEffectsBehavior(ApplyEffects applyEffects, GlobalSettings settings) {
        super(applyEffects, settings);
        this.applyEffects = applyEffects;
    }

    public ApplyEffectsBehavior(ApplyEffects applyEffects) {
        this(applyEffects, GlobalSettings.defaults());
    }

    public ApplyEffectsBehavior() {
        this(null);
    }

    public ApplyEffects applyEffects() {
        return this.applyEffects;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        if (this.applyEffects().effect.isPresent()) {
            if (!world.isClientSide() && world.getRandom().nextFloat() <= this.applyEffects().effect.get().chance()) {
                livEntity.addEffect(this.applyEffects().effect.get().copyEffect());
            }
        }
    }

    public record ApplyEffects(Optional<ChanceEffectInstance> effect) implements IndividualSettings<ApplyEffects> {
        public static final MapCodec<ApplyEffects> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ChanceEffectInstance.CODEC.optionalFieldOf("instance").forGetter(ApplyEffects::effect)
        ).apply(instance, ApplyEffects::new));

        @Override
        public MapCodec<ApplyEffects> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ApplyEffects> streamCodec() {
            return StreamCodec.composite(ByteBufCodecs.optional(ChanceEffectInstance.STREAM_CODEC), ApplyEffects::effect, ApplyEffects::new);
        }
    }
}
