package melonystudios.reutilities.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.entity.outfit.OutfitWearer;
import melonystudios.reutilities.util.Reconstants;
import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OutfitLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private final boolean slimArms;

    public OutfitLayer(RenderLayerParent<T, A> renderer, boolean slimArms) {
        super(renderer);
        this.slimArms = slimArms;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, T mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch) {
        if (!ReConfigs.RENDER_OUTFITS.get()) return;
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getProfiler().push("outfitRendering");
        this.renderOutfitPart(stack, buffer, mob, EquipmentSlot.HEAD, packedLight, this.slimArms);
        this.renderOutfitPart(stack, buffer, mob, EquipmentSlot.CHEST, packedLight, this.slimArms);
        this.renderOutfitPart(stack, buffer, mob, EquipmentSlot.LEGS, packedLight, this.slimArms);
        this.renderOutfitPart(stack, buffer, mob, EquipmentSlot.FEET, packedLight, this.slimArms);
        minecraft.getProfiler().pop();
    }

    private void renderOutfitPart(PoseStack stack, MultiBufferSource buffer, T mob, EquipmentSlot slot, int packedLight, boolean slimArms) {
        boolean isWearingOutfit = false;
        if (mob instanceof OutfitWearer) isWearingOutfit = ((OutfitWearer) mob).isWearingOutfit();

        Level world = mob.level();
        ItemStack armorStack = mob.getItemBySlot(slot);
        A parentModel = this.getParentModel();
        parentModel.setAllVisible(true);

        // Updated outfit rendering ~isa 4-12-24
        var definitions = OutfitDefinition.definitions(world);
        if (mob instanceof OutfitWearer outfit && isWearingOutfit) {
            OutfitDefinition definition = definitions.get(ResourceLocation.parse(outfit.getOutfitDefinition()));
            ResourceLocation outfitLocation = OutfitDefinition.getOutfitTexture(slot, definition, slimArms);
            ResourceLocation emissiveLocation = OutfitDefinition.getEmissiveOutfitTexture(slot, definition, slimArms);
            int outfitColor = OutfitDefinition.getOutfitColors(definition, null, slot);

            if (outfitLocation != null) {
                VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(outfitLocation));
                int transparency = mob.isInvisible() && !mob.isInvisibleTo(Minecraft.getInstance().player) ? 38 : (mob.isInvisible() ? 0 : 255);
                parentModel.renderToBuffer(stack, translucentBuffer, packedLight, Reconstants.getOverlayCoordinates(0), FastColor.ARGB32.color(transparency, outfitColor));
            }

            if (emissiveLocation != null) {
                VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.eyes(emissiveLocation));
                parentModel.renderToBuffer(stack, translucentBuffer, Reconstants.EMISSIVE_LIGHT_VALUE, Reconstants.getOverlayCoordinates(0));
            }
        } else if (!armorStack.isEmpty() && armorStack.has(ReDataComponents.OUTFIT)) {
            OutfitDefinition definition = OutfitDefinition.getDefinition(world, armorStack);
            ResourceLocation outfitLocation = OutfitDefinition.getOutfitTexture(slot, definition, slimArms);
            ResourceLocation emissiveLocation = OutfitDefinition.getEmissiveOutfitTexture(slot, definition, slimArms);
            int outfitColor = OutfitDefinition.getOutfitColors(definition, armorStack, slot);

            if (outfitLocation != null) {
                int brightLight = armorStack.is(ReItemTags.EMISSIVE_LIGHTING) ? LightTexture.pack(15, 15) : packedLight;

                RenderType translucentType = RenderType.entityTranslucent(outfitLocation);
                parentModel.renderToBuffer(stack, buffer.getBuffer(translucentType), brightLight, Reconstants.getOverlayCoordinates(0), FastColor.ARGB32.color(255, outfitColor));
            }

            if (emissiveLocation != null) {
                VertexConsumer emissiveBuffer = buffer.getBuffer(RenderType.eyes(emissiveLocation));
                parentModel.renderToBuffer(stack, emissiveBuffer, Reconstants.EMISSIVE_LIGHT_VALUE, Reconstants.getOverlayCoordinates(0));
            }
        }
    }
}
