package melonystudios.behaviorapi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.api.ReAPI;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;

/// A custom {@link MobEffectInstance} that can have a chance to apply the effect.
///
/// This class' {@link #CODEC} has the following fields:
/// <li>`effect`: A {@linkplain MobEffectInstance#CODEC mob effect instance codec}, defining the effect that should be applied;</li>
/// <li>`chance`: A float codec with a range of `0` to `1`, defining the chance of this effect being applied.</li>
public class ChanceEffectInstance {
    public static final Codec<ChanceEffectInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MobEffectInstance.CODEC.fieldOf("effect").forGetter(ChanceEffectInstance::copyEffect),
            ReAPI.floatRange(0, 1).optionalFieldOf("chance", 1F).forGetter(ChanceEffectInstance::chance)
    ).apply(instance, ChanceEffectInstance::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ChanceEffectInstance> STREAM_CODEC = StreamCodec.composite(
            MobEffectInstance.STREAM_CODEC,
            instance -> instance.instance,
            ByteBufCodecs.FLOAT,
            ChanceEffectInstance::chance,
            ChanceEffectInstance::new
    );
    private final MobEffectInstance instance;
    private float chance;

    /// A custom {@link MobEffectInstance} that can have a chance to apply the effect.
    /// @param instance The defined effect instance.
    /// @param chance The chance of this effect being applied. Defaults to `1`.
    public ChanceEffectInstance(MobEffectInstance instance, float chance) {
        this.instance = instance;
        this.chance = chance;
    }

    /// A custom {@link MobEffectInstance} that can have a chance to apply the effect.
    /// @param instance The defined effect instance.
    public ChanceEffectInstance(MobEffectInstance instance) {
        this(instance, 1);
    }

    /// Sets the chance of this effect being applied.
    /// @param chance The chance. Should be between `0` to `1`.
    public ChanceEffectInstance withChance(float chance) {
        this.chance = chance;
        return this;
    }

    /// Copies all the parameters from this effect instance onto another, in order to apply it.
    ///
    /// This is because effect instances are **mutable**, so one single instance has an effect everywhere.
    public MobEffectInstance copyEffect() {
        MobEffectInstance instance = new MobEffectInstance(this.instance.getEffect(), this.instance.getDuration(), this.instance.getAmplifier(), this.instance.isAmbient(), this.instance.isVisible(), this.instance.showIcon());
        instance.getEffect().value().fillEffectCures(this.instance.getCures(), instance);
        instance.getCures().addAll(this.instance.getCures());
        return instance;
    }

    public float chance() {
        return this.chance;
    }
}
