package melonystudios.reutilities.mixin.renderer;

import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.custom.CapePositioning;
import melonystudios.reutilities.entity.custom.ArmSize;
import melonystudios.reutilities.option.ReClientOptions;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class RePlayerModelMixin<T extends LivingEntity> implements ArmSize {
    @Shadow
    @Final
    private ModelPart cloak;
    @Shadow
    @Final
    private boolean slim;

    @Override
    public boolean reutilities$slimArms() {
        return this.slim;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void setupAnim(T player, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, CallbackInfo callback) {
        if (!ReClientOptions.RENDER_OUTFITS.get()) return;
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);

        // move cape if:
        // 1. has component
        // 2. has full-body
        // 3. component != reg. chestplate
        // boolean operations are hard. ~isa 5-1-26

        CapePositioning positioning = CapePositioning.positionCapeBasedOnOutfit(chestStack, player.getCapability(ReAPI.OUTFIT_CAPABILITY));
        this.cloak.visible = !positioning.isCapeHidden();

        if (positioning.shouldOffset()) {
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
