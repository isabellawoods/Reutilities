package melonystudios.reutilities.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.util.ReCommonConstants;
import melonystudios.reutilities.util.debug.ReDebuggingFlags;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.MarkerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReCommands {
    public static final String ALPHABETICAL_SORT_PARAM = "alphabetical_sort";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // /melonystudios reutilities:dump_<locations> <alphabetical_sort>
        dispatcher.register(Commands.literal("melonystudios")
                .then(Commands.literal(Reutilities.reutilities("dump_boat_types").toString())
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_OWNERS) && ReDebuggingFlags.DEBUG_DUMP_COMMANDS)
                        .executes(context -> dumpLocations(
                                context,
                                false,
                                ReCommonConstants.BOATS.keySet(),
                                Component.translatable("commands.reutilities.boat_types").withColor(ReCommonConstants.REUTILITIES_ACCENT_COLOR),
                                "boat_types.txt")
                        )
                        .then(Commands.argument(ALPHABETICAL_SORT_PARAM, BoolArgumentType.bool())
                        .executes(context -> dumpLocations(
                                context,
                                BoolArgumentType.getBool(context, ALPHABETICAL_SORT_PARAM),
                                ReCommonConstants.BOATS.keySet(),
                                Component.translatable("commands.reutilities.boat_types").withColor(ReCommonConstants.REUTILITIES_ACCENT_COLOR),
                                "boat_types.txt")
                        )
                ))
                .then(Commands.literal(Reutilities.reutilities("dump_recolors").toString())
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_OWNERS) && ReDebuggingFlags.DEBUG_DUMP_COMMANDS)
                        .executes(context -> dumpLocations(
                                context,
                                false,
                                ReCommonConstants.COLORS.keySet(),
                                Component.translatable("commands.reutilities.recolors").withColor(ReCommonConstants.REUTILITIES_ACCENT_COLOR),
                                "recolors.txt")
                        )
                        .then(Commands.argument(ALPHABETICAL_SORT_PARAM, BoolArgumentType.bool())
                        .executes(context -> dumpLocations(
                                context,
                                BoolArgumentType.getBool(context, ALPHABETICAL_SORT_PARAM),
                                ReCommonConstants.COLORS.keySet(),
                                Component.translatable("commands.reutilities.recolors").withColor(ReCommonConstants.REUTILITIES_ACCENT_COLOR),
                                "recolors.txt")
                        )
                ))
        );
    }

    private static int dumpLocations(CommandContext<CommandSourceStack> context, boolean alphabeticalSort, Set<ResourceLocation> locations, Component locationName, String fileName) {
        String fileLocationForErrors = "";

        try {
            Path dumpDirectory = FMLLoader.getGamePath().resolve("dumps/" + Reutilities.MOD_ID);
            Files.createDirectories(dumpDirectory);

            Path dumpFile = dumpDirectory.resolve(fileName);
            fileLocationForErrors = dumpFile.toString();

            try (var outputStream = Files.newOutputStream(dumpFile)) {
                List<ResourceLocation> sortedLocations = getSortedRegistryKeys(alphabeticalSort, locations);
                for (ResourceLocation location : sortedLocations) outputStream.write((location.toString() + "\n").getBytes());
            }

            MutableComponent filePathMessage = Component.literal(FMLLoader.getGamePath().relativize(dumpFile).toString())
                    .withStyle(ChatFormatting.UNDERLINE).withColor(ReCommonConstants.REVARIED_ACCENT_COLOR);

            if (!FMLLoader.getDist().isDedicatedServer()) {
                filePathMessage.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, dumpFile.toString())));
            }

            context.getSource().sendSuccess(() -> Component.translatable("commands.reutilities.dump_locations.success", locationName, filePathMessage), false);
            return 1;
        } catch (Exception exception) {
            Component errorMessage = Component.translatable("commands.reutilities.dump_locations.fail", locationName, Component.literal(fileLocationForErrors).withColor(0xFFC55F));
            context.getSource().sendFailure(errorMessage);
            Reutilities.LOGGER.error(MarkerFactory.getMarker("ReCommands"), errorMessage.getString(), exception);
            return 0;
        }
    }

    private static List<ResourceLocation> getSortedRegistryKeys(boolean alphabeticalSort, Set<ResourceLocation> locations) {
        List<ResourceLocation> sortedLocations = new ArrayList<>(locations);
        if (alphabeticalSort) sortedLocations.sort(ResourceLocation::compareNamespaced);
        return sortedLocations;
    }
}
