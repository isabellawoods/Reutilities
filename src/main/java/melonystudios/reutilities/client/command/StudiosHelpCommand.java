package melonystudios.reutilities.client.command;

import com.mojang.brigadier.CommandDispatcher;
import melonystudios.renderslice.api.SliceComponents;
import melonystudios.reutilities.client.ReClientConstants;
import melonystudios.reutilities.client.screen.CommandHelpScreen;
import melonystudios.reutilities.command.argument.StudiosCommandArgument;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class StudiosHelpCommand {
    private static final String STUDIOS_COMMAND_PARAM = "studios_command";
    public static final String IDENTIFIER = "help";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // /mstudios help "<studios command>"
        dispatcher.register(Commands.literal("mstudios").then(Commands.literal(IDENTIFIER)
                .then(Commands.argument(STUDIOS_COMMAND_PARAM, StudiosCommandArgument.studiosCommand())
                        .executes(context -> openCommandHelpScreen(
                                context.getSource(),
                                StudiosCommandArgument.studiosCommand(context, STUDIOS_COMMAND_PARAM)
                        ))
                )
        ));
    }

    private static int openCommandHelpScreen(CommandSourceStack source, String commandID) {
        String modID = ReClientConstants.STUDIOS_COMMAND_IDENTIFIERS.get(commandID);
        int color = SliceComponents.accentColorOrDefault(modID, 0xFFFFFF);
        source.sendSuccess(() -> Component.translatable(
                "commands.mstudios.help.success",
                Component.literal(commandID).withStyle(style -> style.withColor(color).withBold(true)),
                Component.literal(CommandHelpScreen.getModName(modID)).withStyle(style -> style.withColor(color).withBold(true))
        ), true);
        Minecraft.getInstance().setScreen(new CommandHelpScreen(modID, commandID));
        return 1;
    }
}
