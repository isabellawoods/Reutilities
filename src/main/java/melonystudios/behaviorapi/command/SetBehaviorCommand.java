package melonystudios.behaviorapi.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.reutilities.component.ReDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SetBehaviorCommand {
    private static final DynamicCommandExceptionType ERROR_INVALID_BEHAVIOR = new DynamicCommandExceptionType(
            behavior -> Component.translatableEscape("commands.itembehavior.set.invalid", behavior)
    );

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandBuildContext buildContext) {
        return Commands.argument("targets", EntityArgument.players())
                .then(Commands.literal("add").then(Commands.argument("item_behavior", ResourceKeyArgument.key(BehaviorAPI.ITEM_BEHAVIOR_KEY))
                        .then(Commands.argument("properties", CompoundTagArgument.compoundTag())
                                .executes(context -> addItemBehavior(context.getSource(), getItemBehavior(context, "item_behavior"),
                                        CompoundTagArgument.getCompoundTag(context, "properties"), EntityArgument.getPlayers(context, "targets"))))));
    }

    private static int addItemBehavior(CommandSourceStack source, Holder.Reference<ItemBehavior> behaviorRef, CompoundTag properties, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemBehavior behavior = behaviorRef.value();
            if (!stack.isEmpty()) {
                List<ItemBehavior> existing = stack.getOrDefault(ReDataComponents.BEHAVIORS, new ArrayList<>());

                List<ItemBehavior> behaviors = new ArrayList<>(existing);
                behaviors.add(behavior);
                stack.set(ReDataComponents.BEHAVIORS, behaviors);
                if (players.size() == 1) {
                    source.sendSuccess(() -> Component.translatable("commands.itembehavior.set.success.single", behavior.getCommandDisplayName(), player.getDisplayName()), true);
                } else {
                    source.sendSuccess(() -> Component.translatable("commands.itembehavior.set.success.multiple", behavior.getCommandDisplayName(), players.size()), true);
                }
                return players.size();
            } else {
                source.sendFailure(Component.translatable("commands.itembehavior.set.fail", behavior.getCommandDisplayName(), getItemDisplayName(stack)));
            }
        }
        return 0;
    }

    public static Component getItemDisplayName(ItemStack stack) {
        MutableComponent component = stack.getHoverName().copy().withStyle(ChatFormatting.RED);
        if (stack.get(DataComponents.CUSTOM_NAME) != null) component.withStyle(ChatFormatting.ITALIC);
        MutableComponent wrappedComponent = ComponentUtils.wrapInSquareBrackets(component);
        wrappedComponent.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack))));
        return wrappedComponent;
    }

    public static Holder.Reference<ItemBehavior> getItemBehavior(CommandContext<CommandSourceStack> context, String argument) throws CommandSyntaxException {
        return ResourceKeyArgument.resolveKey(context, argument, BehaviorAPI.ITEM_BEHAVIOR_KEY, ERROR_INVALID_BEHAVIOR);
    }
}
