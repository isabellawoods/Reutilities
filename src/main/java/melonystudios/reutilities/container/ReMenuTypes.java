package melonystudios.reutilities.container;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.container.custom.ReCraftingMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Reutilities.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ReCraftingMenu>> CRAFTING = MENUS.register("crafting", () -> new MenuType<>(ReCraftingMenu::new, FeatureFlags.VANILLA_SET));
}
