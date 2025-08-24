package melonystudios.reutilities;

import com.mojang.logging.LogUtils;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.container.ReMenuTypes;
import melonystudios.reutilities.entity.ReEntities;
import melonystudios.reutilities.util.ReArmorMaterials;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(Reutilities.MOD_ID)
public class Reutilities {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "reutilities";

    public Reutilities(IEventBus eventBus, ModContainer container) {
        eventBus.addListener(this::commonSetup);

        ReArmorMaterials.MATERIALS.register(eventBus);
        ReDataComponents.COMPONENTS.register(eventBus);
        ReBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ReEntities.ENTITIES.register(eventBus);
        ReMenuTypes.MENUS.register(eventBus);

        container.registerConfig(ModConfig.Type.COMMON, ReConfigs.SPEC, "melonystudios/reutilities-common.toml");
        container.registerExtensionPoint(IConfigScreenFactory.class, (minecraft, lastScreen) -> new ConfigurationScreen(container, lastScreen));
    }

    /// Creates a new resource location under ***Reutilities'*** namespace.
    /// @param name The path of this resource location
    public static ResourceLocation reutilities(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    /// Creates a new resource location using the **Common** (`c`) namespace.
    /// @param name The path of this resource location.
    public static ResourceLocation common(String name) {
        return ResourceLocation.fromNamespaceAndPath("c", name);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ReAPI.addBoat(Reconstants.OAK);
    }
}
