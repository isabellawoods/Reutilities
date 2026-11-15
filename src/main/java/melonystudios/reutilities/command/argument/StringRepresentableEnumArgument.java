package melonystudios.reutilities.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.server.command.CommandUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class StringRepresentableEnumArgument<T extends Enum<T> & StringRepresentable> implements ArgumentType<T> {
    private static final Dynamic2CommandExceptionType INVALID_ENUM = new Dynamic2CommandExceptionType(
            (found, constants) -> CommandUtils.makeTranslatableWithFallback("commands.neoforge.arguments.enum.invalid", constants, found));
    private final Class<T> enumClass;

    public static <R extends Enum<R> & StringRepresentable> StringRepresentableEnumArgument<R> representable(Class<R> enumClass) {
        return new StringRepresentableEnumArgument<>(enumClass);
    }

    private StringRepresentableEnumArgument(final Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T parse(final StringReader reader) throws CommandSyntaxException {
        String name = reader.readUnquotedString();
        Map<String, T> parsedValues = new HashMap<>();

        for (T value : this.enumClass.getEnumConstants()) parsedValues.put(value.getSerializedName(), value);
        var parsed = parsedValues.get(name);

        if (parsed == null) {
            throw INVALID_ENUM.createWithContext(reader, name, parsedValues.values());
        } else {
            return parsed;
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Arrays.stream(this.enumClass.getEnumConstants()).map(StringRepresentable::getSerializedName), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.stream(this.enumClass.getEnumConstants()).map(StringRepresentable::getSerializedName).toList();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static class Info<T extends Enum<T> & StringRepresentable> implements ArgumentTypeInfo<StringRepresentableEnumArgument<T>, Info<T>.Template> {
        @Override
        public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
            buffer.writeUtf(template.enumClass.getName());
        }

        @Override
        public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
            try {
                String name = buffer.readUtf();
                return new Template((Class<T>) Class.forName(name));
            } catch (ClassNotFoundException ignored) {
                return null;
            }
        }

        @Override
        public void serializeToJson(Template template, JsonObject json) {
            json.addProperty("enum", template.enumClass.getName());
        }

        @Override
        @NotNull
        public StringRepresentableEnumArgument.Info.Template unpack(StringRepresentableEnumArgument<T> argument) {
            return new Template(argument.enumClass);
        }

        public class Template implements ArgumentTypeInfo.Template<StringRepresentableEnumArgument<T>> {
            final Class<T> enumClass;

            Template(Class<T> enumClass) {
                this.enumClass = enumClass;
            }

            @Override
            @NotNull
            public StringRepresentableEnumArgument<T> instantiate(CommandBuildContext context) {
                return new StringRepresentableEnumArgument<>(this.enumClass);
            }

            @Override
            @NotNull
            public ArgumentTypeInfo<StringRepresentableEnumArgument<T>, ?> type() {
                return Info.this;
            }
        }
    }
}
