package melonystudios.reutilities;

import melonystudios.reutilities.option.ReClientOptions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Reutilities.MOD_ID, dist = Dist.CLIENT)
public class ReClient {
    public ReClient(IEventBus eventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.registerConfig(ModConfig.Type.CLIENT, ReClientOptions.SPEC, "melonystudios/reutilities-client.toml");

        // register config screen for female gender mod
        ModList.get().getModContainerById("wildfire_gender").ifPresent(container1 ->
                container1.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new));
    }
}
