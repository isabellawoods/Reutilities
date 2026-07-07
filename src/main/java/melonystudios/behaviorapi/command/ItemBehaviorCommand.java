package melonystudios.behaviorapi.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ItemBehaviorCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("itembehavior")
                .requires(stack -> stack.hasPermission(2))
                .then(SetBehaviorCommand.register()));
    }
}
