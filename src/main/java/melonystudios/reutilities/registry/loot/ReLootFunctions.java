package melonystudios.reutilities.registry.loot;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.registry.loot.function.ColorPickDyedColorFunction;
import melonystudios.reutilities.registry.loot.function.SetRandomDyesFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReLootFunctions {
    public static final DeferredRegister<LootItemFunctionType<?>> FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, Reutilities.MOD_ID);

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<SetRandomDyesFunction>> SET_RANDOM_DYES = FUNCTIONS.register("set_random_dyes",
            () -> new LootItemFunctionType<>(SetRandomDyesFunction.CODEC));
    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<ColorPickDyedColorFunction>> COLOR_PICK_DYED_COLOR = FUNCTIONS.register("color_pick_dyed_color",
            () -> new LootItemFunctionType<>(ColorPickDyedColorFunction.CODEC));
}
