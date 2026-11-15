package melonystudios.reutilities.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import melonystudios.reutilities.client.ReClientConstants;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StudiosCommandArgument implements ArgumentType<String> {
    private static final List<String> EXAMPLES = List.of("gameplay/update_recorded_disc", "visual/set_resolution", "develop/sample_color_picker");

    public static StudiosCommandArgument studiosCommand() {
        return new StudiosCommandArgument();
    }

    public static String studiosCommand(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readQuotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ReClientConstants.STUDIOS_COMMAND_IDENTIFIERS.keySet().stream()
                .map(command -> "\"" + command + "\""), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
