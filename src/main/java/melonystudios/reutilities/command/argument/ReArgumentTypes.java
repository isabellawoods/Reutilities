package melonystudios.reutilities.command.argument;

import melonystudios.reutilities.Reutilities;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReArgumentTypes {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Reutilities.MOD_ID);

    public static final Holder<ArgumentTypeInfo<?, ?>> STRING_REPRESENTABLE_ENUM = TYPES.register("string_representable_enum", () -> ArgumentTypeInfos.registerByClass(StringRepresentableEnumArgument.class, new StringRepresentableEnumArgument.Info()));
    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<StudiosCommandArgument>> STUDIOS_COMMAND = TYPES.register("studios_command", () -> ArgumentTypeInfos.registerByClass(StudiosCommandArgument.class, SingletonArgumentInfo.contextFree(StudiosCommandArgument::studiosCommand)));
}
