package melonystudios.reutilities.mixin.item;

import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.custom.ApplyEffectsBehavior;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.ReDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
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
        // Item Behaviors
        if (!stack.has(ReDataComponents.BEHAVIORS)) return;
        List<ItemBehavior> behaviors = stack.get(ReDataComponents.BEHAVIORS);
        List<MobEffectInstance> behaviorEffects = new ArrayList<>();

        // Regular behavior tooltips
        for (ItemBehavior behavior : behaviors) {
            if (behavior.settings().showInTooltip() && ReAPI.shouldDisplay(stack, BehaviorAPI.behaviorAPI("behaviors"))) {
                behavior.addToTooltip(context, tooltip::add, flag);
                if (behavior instanceof ApplyEffectsBehavior applyEffects && applyEffects.applyEffects().effect().isPresent()) {
                    behaviorEffects.add(applyEffects.applyEffects().effect().get().copyEffect());
                }
            }
        }

        // Effect tooltips (so they display together)
        if (ReAPI.shouldDisplay(stack, BehaviorAPI.behaviorAPI("behaviors/effects")) && !behaviorEffects.isEmpty()) {
            PotionContents.addPotionTooltip(behaviorEffects, tooltip::add, 1, context.tickRate());
        }
    }
}
