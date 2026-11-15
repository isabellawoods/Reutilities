package melonystudios.reutilities.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import melonystudios.renderslice.api.SliceComponents;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.picker.ColorPicker;
import melonystudios.reutilities.api.picker.PickerSample;
import melonystudios.reutilities.util.ReCommonConstants;
import melonystudios.reutilities.util.ReRegistries;
import melonystudios.reutilities.util.debug.ReDebuggingFlags;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ReCommands {
    private static final DynamicCommandExceptionType ERROR_INVALID_COLOR_PICKER = new DynamicCommandExceptionType(
            behavior -> Component.translatableEscape("commands.mstudios.sample_color_picker.invalid", behavior)
    );
    private static final Marker MARKER = MarkerFactory.getMarker("ReCommands");

    // argument names
    public static final String ALPHABETICAL_SORT_PARAM = "alphabetical_sort";
    public static final String COLOR_PICKER_PARAM = "color_picker";
    public static final String SEQUENCE_PARAM = "use_picker_random_sequence";

    // subcommand identifiers (for the /mstudios help subcommand)
    public static final String DUMP_BOAT_TYPES = "develop/dump_boat_types";
    public static final String DUMP_RECOLORS = "develop/dump_recolors";
    public static final String SAMPLE_COLOR_PICKER = "develop/sample_color_picker";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        int accentColor = SliceComponents.accentColorOrDefault(Reutilities.MOD_ID, 0xFFFFFF);

        // /mstudios develop/dump_<identifiers> <alphabetical_sort>
        dispatcher.register(Commands.literal("mstudios")
                .then(Commands.literal(DUMP_BOAT_TYPES)
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_OWNERS) && ReDebuggingFlags.DEBUG_DUMP_COMMANDS)
                        .executes(context -> dumpIdentifiers(
                                context.getSource(),
                                false,
                                ReCommonConstants.BOATS.keySet(),
                                Component.translatable("commands.mstudios.dump_identifiers.boat_types").withColor(accentColor),
                                "boat_types.txt")
                        )
                        .then(Commands.argument(ALPHABETICAL_SORT_PARAM, BoolArgumentType.bool())
                        .executes(context -> dumpIdentifiers(
                                context.getSource(),
                                BoolArgumentType.getBool(context, ALPHABETICAL_SORT_PARAM),
                                ReCommonConstants.BOATS.keySet(),
                                Component.translatable("commands.mstudios.dump_identifiers.boat_types").withColor(accentColor),
                                "boat_types.txt")
                        )
                ))
                .then(Commands.literal(DUMP_RECOLORS)
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_OWNERS) && ReDebuggingFlags.DEBUG_DUMP_COMMANDS)
                        .executes(context -> dumpIdentifiers(
                                context.getSource(),
                                false,
                                ReCommonConstants.COLORS.keySet(),
                                Component.translatable("commands.mstudios.dump_identifiers.recolors").withColor(accentColor),
                                "recolors.txt")
                        )
                        .then(Commands.argument(ALPHABETICAL_SORT_PARAM, BoolArgumentType.bool())
                        .executes(context -> dumpIdentifiers(
                                context.getSource(),
                                BoolArgumentType.getBool(context, ALPHABETICAL_SORT_PARAM),
                                ReCommonConstants.COLORS.keySet(),
                                Component.translatable("commands.mstudios.dump_identifiers.recolors").withColor(accentColor),
                                "recolors.txt")
                        )
                ))
                // /mstudios develop/sample_color_picker <color_picker: ColorPicker> [<use_picker_random_sequence: boolean>]
                .then(Commands.literal(SAMPLE_COLOR_PICKER).requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument(COLOR_PICKER_PARAM, ResourceKeyArgument.key(ReRegistries.COLOR_PICKER))
                                .executes(context -> sampleColorPicker(
                                        context.getSource(),
                                        ResourceKeyArgument.resolveKey(context, COLOR_PICKER_PARAM, ReRegistries.COLOR_PICKER, ERROR_INVALID_COLOR_PICKER),
                                        false
                                ))
                                .then(Commands.argument(SEQUENCE_PARAM, BoolArgumentType.bool())
                                        .executes(context -> sampleColorPicker(
                                                context.getSource(),
                                                ResourceKeyArgument.resolveKey(context, COLOR_PICKER_PARAM, ReRegistries.COLOR_PICKER, ERROR_INVALID_COLOR_PICKER),
                                                BoolArgumentType.getBool(context, SEQUENCE_PARAM)
                                        ))
                                )
                        )
                )
        );
    }

    private static int dumpIdentifiers(CommandSourceStack context, boolean alphabeticalSort, Set<ResourceLocation> identifiers, Component locationName, String fileName) {
        String fileLocationForErrors = "";

        try {
            Path dumpDirectory = FMLLoader.getGamePath().resolve("dumps/" + Reutilities.MOD_ID);
            Files.createDirectories(dumpDirectory);

            Path dumpFile = dumpDirectory.resolve(fileName);
            fileLocationForErrors = dumpFile.toString();

            try (var outputStream = Files.newOutputStream(dumpFile)) {
                List<ResourceLocation> sortedLocations = getSortedKeys(alphabeticalSort, identifiers);
                for (ResourceLocation location : sortedLocations) outputStream.write((location.toString() + "\n").getBytes());
            }

            MutableComponent filePathMessage = Component.literal(FMLLoader.getGamePath().relativize(dumpFile).toString())
                    .withStyle(ChatFormatting.UNDERLINE).withColor(ReCommonConstants.REVARIED_ACCENT_COLOR);

            if (!FMLLoader.getDist().isDedicatedServer()) {
                filePathMessage.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, dumpFile.toString())));
            }

            context.sendSuccess(() -> Component.translatable("commands.mstudios.dump_identifiers.success", locationName, filePathMessage), false);
            return 1;
        } catch (Exception exception) {
            Component errorMessage = Component.translatable("commands.mstudios.dump_identifiers.fail", locationName, Component.literal(fileLocationForErrors).withColor(ReCommonConstants.REVARIED_ACCENT_COLOR), exception.toString());
            context.sendFailure(errorMessage);
            Reutilities.LOGGER.error(MARKER, errorMessage.getString(), exception);
            return 0;
        }
    }

    private static List<ResourceLocation> getSortedKeys(boolean alphabeticalSort, Set<ResourceLocation> identifiers) {
        List<ResourceLocation> sortedIdentifiers = new ArrayList<>(identifiers);
        if (alphabeticalSort) sortedIdentifiers.sort(ResourceLocation::compareNamespaced);
        return sortedIdentifiers;
    }

    private static int sampleColorPicker(CommandSourceStack source, Holder.Reference<ColorPicker> colorPicker, boolean usePickerRandomSequence) {
        try {
            PickerSample sampledColor;
            if (usePickerRandomSequence) {
                sampledColor = ColorPicker.sample(colorPicker.value(), source.getLevel());
            } else {
                sampledColor = ColorPicker.sample(colorPicker.value(), source.getLevel().getRandom());
            }

            // "Sample from <color picker> returned █████ [Copy as hexadecimal] [Copy as decimal] [Copy as floats]"
            Component squares = Component.translatable("commands.mstudios.sample_color_picker.squares").withColor(sampledColor.asDecimal());

            Component copyHex = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.mstudios.sample_color_picker.copy_hex")).withStyle(style -> style
                    .withColor(ChatFormatting.GREEN)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, sampledColor.asHexadecimal().getString()))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, sampledColor.asHexadecimal()))
            );
            Component copyDecimal = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.mstudios.sample_color_picker.copy_decimal")).withStyle(style -> style
                    .withColor(ChatFormatting.GREEN)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, Integer.toString(sampledColor.asDecimal())))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(Integer.toString(sampledColor.asDecimal()))))
            );
            Component copyFloats = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.mstudios.sample_color_picker.copy_floats")).withStyle(style -> style
                    .withColor(ChatFormatting.GREEN)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, Arrays.toString(sampledColor.asFloatArray())))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(Arrays.toString(sampledColor.asFloatArray()))))
            );

            if (colorPicker.getRegisteredName().equals("[unregistered]")) { // can't actually happen currently but it's supported ~isa 05-11-26
                source.sendSuccess(() -> Component.translatable("commands.mstudios.sample_color_picker.success.unregistered", squares, copyHex, copyDecimal, copyFloats), true);
            } else {
                Component pickerID = Component.literal(colorPicker.getRegisteredName()).withStyle(style -> style.withBold(true));
                source.sendSuccess(() -> Component.translatable("commands.mstudios.sample_color_picker.success.registered", pickerID, squares, copyHex, copyDecimal, copyFloats), true);
            }

            return sampledColor.asDecimal();
        } catch (Exception exception) {
            source.sendFailure(Component.translatable("commands.mstudios.sample_color_picker.exception", Component.literal(exception.toString()).withStyle(ChatFormatting.GRAY)));
            return 0;
        }
    }
}
