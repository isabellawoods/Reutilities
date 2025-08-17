package melonystudios.reutilities.mixin.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.util.Reconstants;
import melonystudios.reutilities.util.tag.ReTrimMaterialTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class ReHumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public ReHumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void cancelForOutfitRendering(PoseStack stack, MultiBufferSource buffer, T livEntity, EquipmentSlot slot, int packedLight, A model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float headYaw, float headPitch, CallbackInfo callback) {
        ItemStack armorStack = livEntity.getItemBySlot(slot);
        if (ReConfigs.RENDER_OUTFITS.get() && armorStack.has(ReDataComponents.OUTFIT)) callback.cancel();
    }

    @ModifyVariable(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int makeModelsEmissive(int packedLight, @Local(argsOnly = true) LivingEntity livEntity) {
        return ReAPI.getLightOutputFromItem(livEntity.getItemBySlot(EquipmentSlot.CHEST), packedLight, livEntity.level(), livEntity.blockPosition(), true);
    }

    @ModifyArg(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderTrim(Lnet/minecraft/core/Holder;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/armortrim/ArmorTrim;Lnet/minecraft/client/model/Model;Z)V"))
    private int makeTrimEmissive(int packedLight, @Local ArmorTrim material) {
        return material.material().is(ReTrimMaterialTags.EMISSIVE_LIGHTING) ? Reconstants.EMISSIVE_LIGHT_VALUE : packedLight;
    }
}
