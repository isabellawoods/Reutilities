package melonystudios.reutilities.mixin.compat.femalegender;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wildfire.render.GenderLayer;
import com.wildfire.render.WildfireModelRenderer;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.custom.ArmSize;
import melonystudios.reutilities.entity.outfit.FullBodyOutfit;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.option.ReClientOptions;
import melonystudios.reutilities.option.ReCommonOptions;
import melonystudios.reutilities.util.ReClientConstants;
import melonystudios.reutilities.util.tag.ReTrimMaterialTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static melonystudios.reutilities.util.ReClientConstants.EMISSIVE_LIGHT_VALUE;

@Pseudo
@Mixin(value = GenderLayer.class, remap = false)
public abstract class ReGenderLayerMixin<ENTITY extends LivingEntity, MODEL extends HumanoidModel<ENTITY>> extends RenderLayer<ENTITY, MODEL> {
    @Shadow private static void renderBox(WildfireModelRenderer.ModelBox model, PoseStack stack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {}
    @Shadow protected abstract void shiftForJacket(PoseStack stack);
    @Shadow private WildfireModelRenderer.BreastModelBox lBreast;
    @Shadow private WildfireModelRenderer.BreastModelBox rBreast;
    @Shadow @Final private static WildfireModelRenderer.OverlayModelBox lBreastWear;
    @Shadow @Final private static WildfireModelRenderer.OverlayModelBox rBreastWear;
    @Unique private static final WildfireModelRenderer.BreastModelBox lBoobOutfit = new WildfireModelRenderer.BreastModelBox(64, 64, 16, 17, -4, 0, 0, 4, 5, 4, 0, false);
    @Unique private static final WildfireModelRenderer.BreastModelBox rBoobOutfit = new WildfireModelRenderer.BreastModelBox(64, 64, 20, 17, 0, 0, 0, 4, 5, 4, 0, false);
    @Unique private static final WildfireModelRenderer.OverlayModelBox lBoobOutfitWear = new WildfireModelRenderer.OverlayModelBox(true,64, 64, 17, 34, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
    @Unique private static final WildfireModelRenderer.OverlayModelBox rBoobOutfitWear = new WildfireModelRenderer.OverlayModelBox(false,64, 64, 21, 34, 0, 0.0F, 0F, 4, 5, 3, 0.0F, false);

    public ReGenderLayerMixin(RenderLayerParent<ENTITY, MODEL> renderer) {
        super(renderer);
    }

    @Inject(method = "renderBreast", at = @At("HEAD"), cancellable = true)
    private void renderBoobOutfitPart(ENTITY livEntity, ItemStack chestStack, PoseStack stack, MultiBufferSource buffer, @Nullable RenderType breastRenderType, int packedLight, int packedOverlay, float alpha, boolean leftBoob, boolean hasJacketLayer, CallbackInfo callback) {
        FullBodyOutfit outfit = livEntity.getCapability(ReAPI.OUTFIT_CAPABILITY);
        boolean rendersAsOutfit = chestStack.has(ReDataComponents.OUTFIT) || (!(chestStack.getItem() instanceof ArmorItem) && outfit != null);

        if (!ReClientOptions.RENDER_OUTFITS.get() || !rendersAsOutfit) return;
        callback.cancel();

        // render the wearer's titties (from their body, not armor)
        if (breastRenderType != null) {
            VertexConsumer consumer = buffer.getBuffer(breastRenderType);
            int color = 0xFFFFFFFF;
            renderBox(leftBoob ? this.lBreast : this.rBreast, stack, consumer, packedLight, packedOverlay, color);
            if (hasJacketLayer) {
                this.shiftForJacket(stack);
                renderBox(leftBoob ? lBreastWear : rBreastWear, stack, consumer, packedLight, packedOverlay, color);
            }
        } else if (hasJacketLayer) this.shiftForJacket(stack);

        // actually render the tit outfit
        stack.pushPose();
        stack.translate(leftBoob ? 0.001F : -0.001F, 0.015F, -0.015F);
        stack.scale(1.05F, 1, 1);
        WildfireModelRenderer.BreastModelBox outfitBox = leftBoob ? lBoobOutfit : rBoobOutfit;
        WildfireModelRenderer.OverlayModelBox outfitWearBox = leftBoob ? lBoobOutfitWear : rBoobOutfitWear;

        boolean slimArms = false;
        if (Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livEntity) instanceof LivingEntityRenderer<?,?> renderer && renderer.getModel() instanceof ArmSize armSize) {
            slimArms = armSize.reutilities$slimArms();
        }

        // prioritize rendering component outfit over full-body ~isa 5-1-26
        if (!chestStack.isEmpty() && livEntity.getEquipmentSlotForItem(chestStack) == EquipmentSlot.CHEST && chestStack.has(ReDataComponents.OUTFIT)) {
            this.renderComponentBoobOutfit(stack, buffer, outfitBox, chestStack, livEntity.level(), livEntity.blockPosition(), packedLight, slimArms);

            stack.pushPose();
            stack.translate(leftBoob ? 0.003F : -0.003F, 0.017F, -0.017F);
            stack.scale(1.06F, 1.01F, 1.01F);
            this.renderComponentBoobOutfit(stack, buffer, outfitWearBox, chestStack, livEntity.level(), livEntity.blockPosition(), packedLight, slimArms);
            stack.popPose();
        } else if (outfit != null) {
            this.renderFullBodyBoobOutfit(stack, buffer, outfitBox, livEntity, packedLight, outfit, slimArms);

            stack.pushPose();
            stack.translate(leftBoob ? 0.003F : -0.003F, 0.017F, -0.017F);
            stack.scale(1.06F, 1.01F, 1.01F);
            this.renderFullBodyBoobOutfit(stack, buffer, outfitWearBox, livEntity, packedLight, outfit, slimArms);
            stack.popPose();
        }
        stack.popPose();
    }

    @Unique
    private void renderFullBodyBoobOutfit(PoseStack stack, MultiBufferSource buffer, WildfireModelRenderer.ModelBox outfitBox, ENTITY livEntity, int packedLight, FullBodyOutfit outfit, boolean slimArms) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getProfiler().push(Reutilities.reutilities("full_body_boob_outfit").toString());
        OutfitDefinition definition = outfit.definition().value();
        int outfitColor = OutfitDefinition.getOutfitColors(definition, null, EquipmentSlot.CHEST);
        int overlayCoordinates = ReClientConstants.getOverlayCoordinates(0);

        // Regular texture
        ResourceLocation outfitLocation = OutfitDefinition.getOutfitTexture(EquipmentSlot.CHEST, definition, slimArms);
        if (outfitLocation != null) {
            VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(outfitLocation));
            Player player = Minecraft.getInstance().player;
            int transparency = livEntity.isInvisible() && player != null && !livEntity.isInvisibleTo(player) ? 38 : (livEntity.isInvisible() ? 0 : 255);
            renderBox(outfitBox, stack, translucentBuffer, packedLight, overlayCoordinates, FastColor.ARGB32.color(transparency, outfitColor));
        }

        // Overlay texture
        ResourceLocation overlayLocation = OutfitDefinition.getOverlayOutfitTexture(EquipmentSlot.CHEST, definition, slimArms);
        if (overlayLocation != null) {
            VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(overlayLocation));
            Player player = Minecraft.getInstance().player;
            int transparency = livEntity.isInvisible() && player != null && !livEntity.isInvisibleTo(player) ? 38 : (livEntity.isInvisible() ? 0 : 255);
            renderBox(outfitBox, stack, translucentBuffer, packedLight, overlayCoordinates, FastColor.ARGB32.color(transparency, 0xFFFFFF));
        }

        // Emissive texture
        ResourceLocation emissiveLocation = OutfitDefinition.getEmissiveOutfitTexture(EquipmentSlot.CHEST, definition, slimArms);
        if (emissiveLocation != null) {
            VertexConsumer emissiveBuffer = buffer.getBuffer(RenderType.entityTranslucentEmissive(emissiveLocation, false));
            int emissiveColor = ReClientOptions.COLOR_EMISSIVE_OUTFIT_PARTS.get() ? outfitColor : -1;
            renderBox(outfitBox, stack, emissiveBuffer, EMISSIVE_LIGHT_VALUE, overlayCoordinates, emissiveColor);
        }
        minecraft.getProfiler().pop();
    }

    @Unique
    private void renderComponentBoobOutfit(PoseStack stack, MultiBufferSource buffer, WildfireModelRenderer.ModelBox outfitBox, ItemStack chestStack, Level world, BlockPos pos, int packedLight, boolean slimArms) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getProfiler().push(Reutilities.reutilities("component_boob_outfit").toString());
        OutfitDefinition definition = OutfitDefinition.getDefinition(world, chestStack);
        int outfitColor = OutfitDefinition.getOutfitColors(definition, chestStack, EquipmentSlot.CHEST);
        int overlayCoordinates = ReClientConstants.getOverlayCoordinates(0);

        packedLight = ReAPI.getItemBrightness(chestStack, packedLight, world, pos, true);

        // Regular texture
        ResourceLocation outfitLocation = OutfitDefinition.getOutfitTexture(EquipmentSlot.CHEST, definition, slimArms);
        if (outfitLocation != null) {
            VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(outfitLocation));
            renderBox(outfitBox, stack, translucentBuffer, packedLight, overlayCoordinates, FastColor.ARGB32.opaque(outfitColor));
        }

        // Overlay texture
        ResourceLocation overlayLocation = OutfitDefinition.getOverlayOutfitTexture(EquipmentSlot.CHEST, definition, slimArms);
        if (overlayLocation != null) {
            VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(overlayLocation));
            renderBox(outfitBox, stack, translucentBuffer, packedLight, overlayCoordinates, 0xFFFFFFFF);
        }

        // Emissive texture
        ResourceLocation emissiveLocation = OutfitDefinition.getEmissiveOutfitTexture(EquipmentSlot.CHEST, definition, slimArms);
        if (emissiveLocation != null) {
            VertexConsumer emissiveBuffer = buffer.getBuffer(RenderType.entityTranslucentEmissive(emissiveLocation, false));
            int emissiveColor = ReClientOptions.COLOR_EMISSIVE_OUTFIT_PARTS.get() ? outfitColor : -1;
            renderBox(outfitBox, stack, emissiveBuffer, EMISSIVE_LIGHT_VALUE, overlayCoordinates, emissiveColor);
        }

        minecraft.getProfiler().pop();
    }

    @ModifyArg(method = "renderBreast", at = @At(value = "INVOKE", target = "Lcom/wildfire/render/GenderLayer;renderBox(Lcom/wildfire/render/WildfireModelRenderer$ModelBox;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V", ordinal = 2), index = 3)
    private int makeBoobArmorEmissive(int light, @Local(argsOnly = true) LivingEntity livEntity) {
        return ReAPI.getItemBrightness(livEntity.getItemBySlot(EquipmentSlot.CHEST), light, livEntity.level(), livEntity.blockPosition(), true);
    }

    @ModifyArg(method = "renderBreast", at = @At(value = "INVOKE", target = "Lcom/wildfire/render/GenderLayer;renderBox(Lcom/wildfire/render/WildfireModelRenderer$ModelBox;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V", ordinal = 3), index = 3)
    private int makeBoobTrimEmissive(int light, @Local(argsOnly = true) LivingEntity livEntity, @Local ArmorTrim material) {
        if (material.material().is(ReTrimMaterialTags.EMISSIVE_LIGHTING) && ReCommonOptions.LIGHT_EMITTING_EMISSIVES.get()) {
            return ReClientConstants.EMISSIVE_LIGHT_VALUE;
        }
        return ReAPI.getItemBrightness(livEntity.getItemBySlot(EquipmentSlot.CHEST), light, livEntity.level(), livEntity.blockPosition(), true);
    }

    /// @author ~isa 05-01-26
    /// @reason allows any entity to have a correct breast texture, will be used for some of my mods later (like *Numinosity*)
    @Overwrite
    @Nullable
    private ResourceLocation getBreastTexture(ENTITY livEntity) {
        return livEntity instanceof AbstractClientPlayer player ? player.getSkin().texture() : (livEntity instanceof ArmorStand ? null : Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livEntity).getTextureLocation(livEntity));
    }
}
