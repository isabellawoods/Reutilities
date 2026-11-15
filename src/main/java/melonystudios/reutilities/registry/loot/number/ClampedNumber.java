package melonystudios.reutilities.registry.loot.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.registry.loot.ReNumberProviders;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.jetbrains.annotations.NotNull;

public record ClampedNumber(NumberProvider source, int minInclusive, int maxInclusive) implements NumberProvider {
    public static final MapCodec<ClampedNumber> CODEC = RecordCodecBuilder.<ClampedNumber>mapCodec(instance -> instance.group(
            NumberProviders.CODEC.fieldOf("source").forGetter(ClampedNumber::source),
            Codec.INT.fieldOf("min_inclusive").forGetter(ClampedNumber::minInclusive),
            Codec.INT.fieldOf("max_inclusive").forGetter(ClampedNumber::maxInclusive)
    ).apply(instance, ClampedNumber::new)).validate(number -> number.maxInclusive() < number.minInclusive() ?
            DataResult.error(() -> "Max must be at least min, min_inclusive: " + number.minInclusive + ", max_inclusive: " + number.maxInclusive) :
            DataResult.success(number));

    @Override
    public float getFloat(LootContext context) {
        return Mth.clamp(this.source.getFloat(context), this.minInclusive, this.maxInclusive);
    }

    @Override
    public int getInt(LootContext context) {
        return Mth.clamp(this.source.getInt(context), this.minInclusive, this.maxInclusive);
    }

    @Override
    @NotNull
    public LootNumberProviderType getType() {
        return ReNumberProviders.CLAMPED_NUMBER.get();
    }
}
