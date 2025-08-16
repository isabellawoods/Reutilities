package melonystudios.reutilities.mixin;

import melonystudios.reutilities.component.ReDataComponents;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ReItemStackMixin implements DataComponentHolder {
    @Inject(method = "getBarColor", at = @At("HEAD"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> callback) {
        if (this.has(ReDataComponents.BAR_COLOR)) callback.setReturnValue(this.get(ReDataComponents.BAR_COLOR));
    }
}
