package melonystudios.reutilities.mixin.renderer;

import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.component.ReDataComponents;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class RePlayerModelMixin<T extends LivingEntity> {
    @Shadow
    @Final
    private ModelPart cloak;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void setupAnim(T player, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, CallbackInfo callback) {
        if (player.getItemBySlot(EquipmentSlot.CHEST).has(ReDataComponents.OUTFIT) && ReConfigs.RENDER_OUTFITS.get()) {
            if (player.isCrouching()) {
                this.cloak.z = 1.4F;
                this.cloak.y = 1.85F;
            } else {
                this.cloak.z = 0;
                this.cloak.y = 0;
            }
        }
    }
}
