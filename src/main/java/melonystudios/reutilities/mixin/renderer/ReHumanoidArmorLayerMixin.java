package melonystudios.reutilities.mixin.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.option.ReClientOptions;
import melonystudios.reutilities.option.ReCommonOptions;
import melonystudios.reutilities.util.ReClientConstants;
import melonystudios.reutilities.util.tag.ReTrimMaterialTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class ReHumanoidArmorLayerMixin {
    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private <T extends LivingEntity, A extends HumanoidModel<T>> void cancelForOutfitRendering(PoseStack stack, MultiBufferSource buffer, T livEntity, EquipmentSlot slot, int packedLight, A model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float headYaw, float headPitch, CallbackInfo callback) {
        ItemStack armorStack = livEntity.getItemBySlot(slot);
        if (ReClientOptions.RENDER_OUTFITS.get() && armorStack.has(ReDataComponents.OUTFIT)) callback.cancel();
    }

    @ModifyVariable(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int makeModelsEmissive(int packedLight, @Local(argsOnly = true) LivingEntity livEntity, @Local(argsOnly = true) EquipmentSlot slot) {
        return ReAPI.getItemBrightness(livEntity.getItemBySlot(slot), packedLight, livEntity.level(), livEntity.blockPosition(), true);
    }

    @ModifyArg(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderTrim(Lnet/minecraft/core/Holder;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/armortrim/ArmorTrim;Lnet/minecraft/client/model/Model;Z)V"))
    private int makeTrimEmissive(int packedLight, @Local ArmorTrim material, @Local(argsOnly = true) LivingEntity livEntity, @Local(argsOnly = true) EquipmentSlot slot) {
        if (material.material().is(ReTrimMaterialTags.EMISSIVE_LIGHTING) && ReCommonOptions.LIGHT_EMITTING_EMISSIVES.get()) {
            return ReClientConstants.EMISSIVE_LIGHT_VALUE;
        }
        return ReAPI.getItemBrightness(livEntity.getItemBySlot(slot), packedLight, livEntity.level(), livEntity.blockPosition(), true);
    }
}
