package melonystudios.reutilities.mixin.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import melonystudios.reutilities.api.ReAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ReItemRendererMixin {
    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int makeItemEmissive(int packedLight, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ItemDisplayContext context) {
        if (context == ItemDisplayContext.GUI) return packedLight;
        Minecraft minecraft = Minecraft.getInstance();
        BlockPos pos = minecraft.cameraEntity != null ? minecraft.cameraEntity.blockPosition() : null;
        return ReAPI.getLightOutputFromItem(stack, packedLight, minecraft.level, pos, true);
    }
}
