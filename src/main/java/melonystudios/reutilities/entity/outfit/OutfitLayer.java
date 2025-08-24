package melonystudios.reutilities.entity.outfit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.util.Reconstants;
import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static melonystudios.reutilities.util.Reconstants.EMISSIVE_LIGHT_VALUE;

@OnlyIn(Dist.CLIENT)
public class OutfitLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private final OutfitModel<T> outfitModel;

    public OutfitLayer(RenderLayerParent<T, A> renderer, OutfitModel<T> outfitModel) {
        super(renderer);
        this.outfitModel = outfitModel;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, T mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch) {
        if (!ReConfigs.RENDER_OUTFITS.get()) return;
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getProfiler().push("outfitRendering");
        boolean slimArms = this.outfitModel.slimArms();

        this.outfitModel.setAllVisible(true);
        if (this.getParentModel() instanceof PlayerModel<?> playerModel) {
            this.outfitModel.copyPropertiesFrom(playerModel);
        } else {
            this.getParentModel().copyPropertiesTo(this.outfitModel);
        }

        this.renderOutfitPart(stack, buffer, mob, EquipmentSlot.HEAD, packedLight, slimArms);
        this.renderOutfitPart(stack, buffer, mob, EquipmentSlot.CHEST, packedLight, slimArms);
        this.renderOutfitPart(stack, buffer, mob, EquipmentSlot.LEGS, packedLight, slimArms);
        this.renderOutfitPart(stack, buffer, mob, EquipmentSlot.FEET, packedLight, slimArms);
        minecraft.getProfiler().pop();
    }

    /// Renders part of an outfit based on which type of outfit is available for the current slot: either the "**full-body outfit**", or the "**component outfit**".
    public void renderOutfitPart(PoseStack stack, MultiBufferSource buffer, T mob, EquipmentSlot slot, int packedLight, boolean slimArms) {
        boolean wearingOutfit = false;
        if (mob instanceof FullBodyOutfit wearer) wearingOutfit = wearer.isWearingOutfit();

        Level world = mob.level();
        ItemStack armorStack = mob.getItemBySlot(slot);

        // Updated outfit rendering ~isa 4-12-24
        if (mob instanceof FullBodyOutfit && wearingOutfit) {
            this.renderFullBodyOutfit(stack, buffer, mob, slot, packedLight, OutfitDefinition.definitions(world), slimArms);
        } else if (!armorStack.isEmpty() && armorStack.has(ReDataComponents.OUTFIT)) {
            this.renderComponentOutfit(stack, buffer, slot, armorStack, world, packedLight, slimArms);
        }
    }

    /// Renders part of an outfit based on the {@linkplain FullBodyOutfit entity's built-in `outfit` tag}, called a "**full-body outfit**".
    public void renderFullBodyOutfit(PoseStack stack, MultiBufferSource buffer, T mob, EquipmentSlot slot, int packedLight, Registry<OutfitDefinition> definitions, boolean slimArms) {
        OutfitDefinition definition = definitions.get(ResourceLocation.parse(((FullBodyOutfit) mob).getOutfitDefinition()));
        ResourceLocation outfitLocation = OutfitDefinition.getOutfitTexture(slot, definition, slimArms);
        ResourceLocation emissiveLocation = OutfitDefinition.getEmissiveOutfitTexture(slot, definition, slimArms);
        int outfitColor = OutfitDefinition.getOutfitColors(definition, null, slot);
        int overlayCoordinates = Reconstants.getOverlayCoordinates(0);

        // Regular texture
        if (outfitLocation != null) {
            VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(outfitLocation));
            Player player = Minecraft.getInstance().player;
            int transparency = mob.isInvisible() && player != null && !mob.isInvisibleTo(player) ? 38 : (mob.isInvisible() ? 0 : 255);
            this.outfitModel.renderToBuffer(stack, translucentBuffer, packedLight, overlayCoordinates, FastColor.ARGB32.color(transparency, outfitColor));
        }

        // Emissive texture
        if (emissiveLocation != null) {
            VertexConsumer emissiveBuffer = buffer.getBuffer(RenderType.eyes(emissiveLocation));
            this.outfitModel.renderToBuffer(stack, emissiveBuffer, EMISSIVE_LIGHT_VALUE, overlayCoordinates);
        }
    }

    /// Renders part of an outfit based on the item stack's {@link ReDataComponents#OUTFIT reutilities:outfit} component, called a "**component outfit**".
    public void renderComponentOutfit(PoseStack stack, MultiBufferSource buffer, EquipmentSlot slot, ItemStack armorStack, Level world, int packedLight, boolean slimArms) {
        OutfitDefinition definition = OutfitDefinition.getDefinition(world, armorStack);
        ResourceLocation outfitLocation = OutfitDefinition.getOutfitTexture(slot, definition, slimArms);
        ResourceLocation emissiveLocation = OutfitDefinition.getEmissiveOutfitTexture(slot, definition, slimArms);
        int outfitColor = OutfitDefinition.getOutfitColors(definition, armorStack, slot);
        int overlayCoordinates = Reconstants.getOverlayCoordinates(0);

        // Regular texture
        if (outfitLocation != null) {
            int brightLight = armorStack.is(ReItemTags.EMISSIVE_LIGHTING) ? EMISSIVE_LIGHT_VALUE : packedLight;

            VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(outfitLocation));
            this.outfitModel.renderToBuffer(stack, translucentBuffer, brightLight, overlayCoordinates, FastColor.ARGB32.color(255, outfitColor));
        }

        // Emissive texture
        if (emissiveLocation != null) {
            VertexConsumer emissiveBuffer = buffer.getBuffer(RenderType.eyes(emissiveLocation));
            this.outfitModel.renderToBuffer(stack, emissiveBuffer, EMISSIVE_LIGHT_VALUE, overlayCoordinates);
        }

        // glint currently renders on the player as well, just like during Back Math dev ~isa 23-8-25
        /*if (armorStack.hasFoil()) {
            this.outfitModel.renderToBuffer(stack, buffer.getBuffer(RenderType.entityGlint()), packedLight, Reconstants.getOverlayCoordinates(1));
        }*/
    }
}
