package melonystudios.reutilities;

import com.mojang.logging.LogUtils;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.block.ReBlockTypes;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.command.argument.ReArgumentTypes;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.container.ReMenuTypes;
import melonystudios.reutilities.entity.ReAttachmentTypes;
import melonystudios.reutilities.entity.ReEntities;
import melonystudios.reutilities.item.ReArmorMaterials;
import melonystudios.reutilities.option.ReCommonOptions;
import melonystudios.reutilities.registry.loot.ReLootFunctions;
import melonystudios.reutilities.registry.loot.ReNumberProviders;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

import static melonystudios.reutilities.api.boat.ReBoats.*;

@Mod(Reutilities.MOD_ID)
public class Reutilities {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "reutilities";

    public Reutilities(IEventBus eventBus, ModContainer container) {
        eventBus.addListener(this::commonSetup);

        ReArmorMaterials.MATERIALS.register(eventBus);
        ReDataComponents.COMPONENTS.register(eventBus);
        ReBlockTypes.TYPES.register(eventBus);
        ReBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ReEntities.ENTITIES.register(eventBus);
        ReAttachmentTypes.ATTACHMENTS.register(eventBus);
        ReMenuTypes.MENUS.register(eventBus);
        ReLootFunctions.FUNCTIONS.register(eventBus);
        ReNumberProviders.PROVIDERS.register(eventBus);
        ReArgumentTypes.TYPES.register(eventBus);

        container.registerConfig(ModConfig.Type.COMMON, ReCommonOptions.SPEC, "melonystudios/" + MOD_ID + "-common.toml");
    }

    /// Creates a name for a data generator using ***Reutilities***' name.
    /// @param name The name of the generator, like *"Item Models"*.
    public static String generatorName(String name) {
        return "Reutilities — " + name;
    }

    /// Creates a new identifier under ***Reutilities'*** namespace.
    /// @param name The path of this identifier.
    public static ResourceLocation reutilities(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    /// Creates a new identifier under the **Common** (`c`) namespace.
    /// @param name The path of this identifier.
    public static ResourceLocation common(String name) {
        return ResourceLocation.fromNamespaceAndPath("c", name);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ReAPI.addBoats(OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, MANGROVE, BAMBOO, CHERRY);
    }
}
