package melonystudios.reutilities.client;

import melonystudios.reutilities.client.api.ReClientAPI;
import melonystudios.reutilities.client.command.StudiosHelpCommand;
import melonystudios.reutilities.client.option.ReClientOptions;
import melonystudios.reutilities.command.ReCommands;
import melonystudios.reutilities.util.ReCommonConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static melonystudios.reutilities.Reutilities.MOD_ID;

@Mod(value = MOD_ID, dist = Dist.CLIENT)
public class ReutilitiesClient {
    public ReutilitiesClient(IEventBus eventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.registerConfig(ModConfig.Type.CLIENT, ReClientOptions.SPEC, "melonystudios/" + MOD_ID + "-client.toml");
        eventBus.addListener(this::clientSetup);

        // register config screen for female gender mod
        ModList.get().getModContainerById(ReCommonConstants.FEMALE_GENDER_MOD_ID).ifPresent(container1 ->
                container1.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new));
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ReClientAPI.registerStudiosCommand(MOD_ID, StudiosHelpCommand.IDENTIFIER);
        ReClientAPI.registerStudiosCommand(MOD_ID, ReCommands.DUMP_BOAT_TYPES);
        ReClientAPI.registerStudiosCommand(MOD_ID, ReCommands.DUMP_RECOLORS);
        ReClientAPI.registerStudiosCommand(MOD_ID, ReCommands.SAMPLE_COLOR_PICKER);
    }
}
