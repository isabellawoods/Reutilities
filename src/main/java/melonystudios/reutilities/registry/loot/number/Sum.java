package melonystudios.reutilities.registry.loot.number;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.registry.loot.ReNumberProviders;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Sum(List<NumberProvider> summands) implements NumberProvider {
    public static final MapCodec<Sum> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            NumberProviders.CODEC.listOf().fieldOf("summands").forGetter(Sum::summands)
    ).apply(instance, Sum::new));

    @Override
    public float getFloat(LootContext context) {
        float value = 0;
        for (NumberProvider provider : this.summands) value += provider.getFloat(context);

        return value;
    }

    @Override
    public int getInt(LootContext context) {
        float value = 0;
        for (NumberProvider provider : this.summands) value += provider.getFloat(context);

        return Mth.floor(value);
    }

    public static Sum sum(NumberProvider... summands) {
        return new Sum(List.of(summands));
    }

    @Override
    @NotNull
    public LootNumberProviderType getType() {
        return ReNumberProviders.SUM.get();
    }
}
