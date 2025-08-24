package melonystudios.reutilities.mixin.item;

import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Item.class)
public class ReItemMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag, CallbackInfo callback) {
        List<Component> tags = Reconstants.addItemTagsTooltip(stack, new ArrayList<>());
        if (flag.isAdvanced() && ReConfigs.SHOW_COMPONENTS_WITH_ALT.get() && ReAPI.shouldDisplay(stack, Reutilities.reutilities("item_components")) && !tags.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.reutilities.hold_alt", Component.keybind("key.keyboard.left.alt").withStyle(flag.hasAltDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
            if (flag.hasAltDown()) tooltip.addAll(tags);
        }
    }
}
