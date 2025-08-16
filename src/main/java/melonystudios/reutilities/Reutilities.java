package melonystudios.reutilities;

import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.ReEntities;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Reutilities.MOD_ID)
public class Reutilities {
    public static final String MOD_ID = "reutilities";

    public Reutilities(IEventBus eventBus, ModContainer container) {
        eventBus.addListener(this::commonSetup);
        ReDataComponents.COMPONENTS.register(eventBus);
        ReBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ReEntities.ENTITIES.register(eventBus);
    }

    /// Creates a new resource location under <i><b>Reutilities</b></i>' namespace.
    /// @param name The path of this resource location
    public static ResourceLocation reutilities(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    /// Creates a new resource location using the <b>Common</b> (<code>c</code>) namespace.
    /// @param name The path of this resource location.
    public static ResourceLocation common(String name) {
        return ResourceLocation.fromNamespaceAndPath("c", name);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ReAPI.addBoat(Reconstants.OAK);
    }
}
