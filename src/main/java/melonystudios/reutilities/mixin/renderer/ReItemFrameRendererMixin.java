package melonystudios.reutilities.mixin.renderer;

import melonystudios.reutilities.client.api.ReClientAPI;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameRenderer.class)
public class ReItemFrameRendererMixin<T extends ItemFrame> {
    @Inject(method = "getLightVal", at = @At("HEAD"), cancellable = true)
    private void getLightVal(T frame, int glowLight, int regularLight, CallbackInfoReturnable<Integer> callback) {
        int oldLight = frame.getType() == EntityType.GLOW_ITEM_FRAME ? glowLight : regularLight;
        int newLight = ReClientAPI.getItemBrightness(frame.getItem(), oldLight, frame.level(), frame.blockPosition(), true);
        if (oldLight != newLight) callback.setReturnValue(newLight);
    }
}
