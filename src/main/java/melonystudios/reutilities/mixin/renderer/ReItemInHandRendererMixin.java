package melonystudios.reutilities.mixin.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import melonystudios.reutilities.api.ReAPI;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
public class ReItemInHandRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int makeItemEmissive(int packedLight, @Local(argsOnly = true) LivingEntity livEntity, @Local(argsOnly = true) ItemStack stack) {
        return ReAPI.getLightOutputFromItem(stack, packedLight, livEntity.level(), livEntity.blockPosition(), true);
    }
}
