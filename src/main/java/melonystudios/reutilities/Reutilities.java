package melonystudios.reutilities;

import com.mojang.logging.LogUtils;
import melonystudios.behaviorapi.BehaviorSounds;
import melonystudios.behaviorapi.ItemBehaviors;
import melonystudios.behaviorapi.serializer.ConsumeBehaviorSerializers;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.block.ReBlockTypes;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.container.ReMenuTypes;
import melonystudios.reutilities.entity.ReEntities;
import melonystudios.reutilities.util.ReArmorMaterials;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(Reutilities.MOD_ID)
public class Reutilities {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "reutilities";

    public Reutilities(IEventBus eventBus, ModContainer container) {
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);

        // Reutilities
        ReArmorMaterials.MATERIALS.register(eventBus);
        ReDataComponents.COMPONENTS.register(eventBus);
        ReBlockTypes.TYPES.register(eventBus);
        ReBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ReEntities.ENTITIES.register(eventBus);
        ReMenuTypes.MENUS.register(eventBus);

        // Behavior API
        BehaviorSounds.SOUNDS.register(eventBus);
        ItemBehaviors.BEHAVIORS.register(eventBus);
        ConsumeBehaviorSerializers.SERIALIZERS.register(eventBus);

        container.registerConfig(ModConfig.Type.COMMON, ReConfigs.SPEC, "melonystudios/reutilities-common.toml");
    }

    /// Creates a name for a data generator using ***Reutilities***' name.
    /// @param name The name of the generator, like *"Item Models"*.
    public static String generatorName(String name) {
        return "Reutilities — " + name;
    }

    /// Creates a new resource location under ***Reutilities'*** namespace.
    /// @param name The path of this resource location.
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

    private void clientSetup(final FMLClientSetupEvent event) {
        // Configs
        ModContainer container = ModList.get()
                .getModContainerById(MOD_ID)
                .orElseThrow(() -> new IllegalStateException("Could not find Reutilities' mod container"));
        Supplier<IConfigScreenFactory> screenFactory = () -> (minecraft, lastScreen) -> new ConfigurationScreen(container, lastScreen);
        container.registerExtensionPoint(IConfigScreenFactory.class, screenFactory);
    }
}
