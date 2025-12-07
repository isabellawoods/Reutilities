package melonystudios.reutilities.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.util.ReCommonConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ReItemStackMixin implements DataComponentHolder, IItemStackExtension {
    @Inject(method = "getBarColor", at = @At("HEAD"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> callback) {
        if (this.has(ReDataComponents.BAR_COLOR)) callback.setReturnValue(this.get(ReDataComponents.BAR_COLOR));
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            ordinal = 5, shift = At.Shift.AFTER))
    public void addComponentTooltips(Item.TooltipContext context, Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> callback, @Local List<Component> list) {
        for (DataComponentType<? extends TooltipProvider> component : ReCommonConstants.addComponentTooltips().getComponentToAdd()) {
            if (this.shouldDisplay(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(component))) {
                this.addToTooltip(component, context, list::add, flag);
            }
        }
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER))
    public void addComponentDisplay(Item.TooltipContext context, Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> callback, @Local List<Component> list) {
        if (player != null) player.level().getProfiler().push(Reutilities.reutilities("display_components_on_tooltip").toString());

        if (flag.isAdvanced() && ReConfigs.SHOW_COMPONENTS_WITH_ALT.get() && ReCommonConstants.shouldDisplay(this, Reutilities.reutilities("item_components"))) {
            List<Component> tags = ReCommonConstants.addItemTagsTooltip(this, context.registries(), new ArrayList<>());
            if (tags.isEmpty()) return;
            list.add(Component.translatable("tooltip.reutilities.for_components", Component.keybind("key.keyboard.left.alt").withStyle(flag.hasAltDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
            if (flag.hasAltDown()) list.addAll(tags);
        }
        if (player != null) player.level().getProfiler().pop();
    }

    @Unique
    public boolean shouldDisplay(ResourceLocation name) {
        List<ResourceLocation> itemTags = this.getComponents().get(ReDataComponents.HIDE_COMPONENTS.get());
        if (itemTags == null || itemTags.isEmpty()) return true;
        return !itemTags.contains(name);
    }
}
