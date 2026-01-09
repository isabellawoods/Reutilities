package melonystudios.reutilities.mixin.renderer;

import melonystudios.reutilities.component.custom.CapePositioning;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CapeLayer.class)
public class ReCapeLayerMixin {
    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    public boolean hideCapeBasedOnComponent(ItemStack stack, Item item) {
        return !CapePositioning.shouldCapeRender(stack);
    }
}
