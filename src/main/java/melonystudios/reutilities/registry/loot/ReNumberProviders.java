package melonystudios.reutilities.registry.loot;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.registry.loot.number.ClampedNumber;
import melonystudios.reutilities.registry.loot.number.Sum;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReNumberProviders {
    public static final DeferredRegister<LootNumberProviderType> PROVIDERS = DeferredRegister.create(Registries.LOOT_NUMBER_PROVIDER_TYPE, Reutilities.MOD_ID);

    public static final DeferredHolder<LootNumberProviderType, LootNumberProviderType> SUM = PROVIDERS.register("sum", () -> new LootNumberProviderType(Sum.CODEC));
    public static final DeferredHolder<LootNumberProviderType, LootNumberProviderType> CLAMPED_NUMBER = PROVIDERS.register("clamped_number", () -> new LootNumberProviderType(ClampedNumber.CODEC));
}
