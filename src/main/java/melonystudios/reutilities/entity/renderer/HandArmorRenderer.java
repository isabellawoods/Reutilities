package melonystudios.reutilities.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.entity.outfit.OutfitModel;
import melonystudios.reutilities.option.ReClientOptions;
import melonystudios.reutilities.option.ReCommonOptions;
import melonystudios.reutilities.util.ReClientConstants;
import melonystudios.reutilities.util.tag.ReTrimMaterialTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientHooks;

import java.util.List;

public class HandArmorRenderer {
    public static void renderOutfitInArm(AbstractClientPlayer player, HumanoidArm side, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (!ReClientOptions.RENDER_OUTFIT_ON_HAND.get()) return;
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getProfiler().push(Reutilities.reutilities("hand_outfit_rendering").toString());

        OutfitDefinition definition = null;
        Level world = player.level();
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
        boolean fullBody = false;

        if (chestStack.has(ReDataComponents.OUTFIT)) {
            definition = OutfitDefinition.getDefinition(world, chestStack);
        } else if (player.getCapability(ReAPI.OUTFIT_CAPABILITY) != null) {
            definition = player.getCapability(ReAPI.OUTFIT_CAPABILITY).definition().value();
            fullBody = true;
        }
        if (definition == null) return;

        boolean slimArms = player.getSkin().model().id().equals("slim");
        PlayerModel<AbstractClientPlayer> playerModel = new PlayerModel<>(minecraft.getEntityModels().bakeLayer(slimArms ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slimArms);
        OutfitModel<AbstractClientPlayer> outfitModel = new OutfitModel<>(minecraft.getEntityModels().bakeLayer(slimArms ? OutfitModel.SLIM : OutfitModel.CLASSIC), slimArms);
        ModelPart arm = side == HumanoidArm.LEFT ? outfitModel.leftArm : outfitModel.rightArm;
        ModelPart sleeve = side == HumanoidArm.LEFT ? outfitModel.leftSleeve : outfitModel.rightSleeve;

        outfitModel.attackTime = 0;
        outfitModel.swimAmount = 0;
        outfitModel.crouching = false;
        arm.xRot = 0;
        sleeve.xRot = 0;
        outfitModel.copyPropertiesFrom(playerModel);
        outfitModel.setupAnim(player, 0, 0, 0, 0, 0);

        EquipmentSlot slot = side == HumanoidArm.LEFT ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
        int color = FastColor.ARGB32.color(255, OutfitDefinition.getOutfitColors(definition, chestStack, slot));

        if (!fullBody) packedLight = ReAPI.getItemBrightness(chestStack, packedLight, player.level(), player.blockPosition(), true);

        stack.pushPose();
        stack.scale(1.002F, 1.002F, 1.002F);
        stack.translate(side == HumanoidArm.LEFT ? -0.0005F : 0.0005F, 0, 0);

        // Regular texture
        ResourceLocation outfitLocation = OutfitDefinition.getOutfitTexture(slot, definition, slimArms);
        if (outfitLocation != null) {
            VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(outfitLocation));

            arm.render(stack, translucentBuffer, packedLight, ReClientConstants.getOverlayCoordinates(0), color);
            sleeve.render(stack, translucentBuffer, packedLight, ReClientConstants.getOverlayCoordinates(0), color);
        }

        // Overlay texture
        ResourceLocation overlayLocation = OutfitDefinition.getOverlayOutfitTexture(slot, definition, slimArms);
        if (overlayLocation != null) {
            VertexConsumer translucentBuffer = buffer.getBuffer(RenderType.entityTranslucent(overlayLocation));

            arm.render(stack, translucentBuffer, packedLight, ReClientConstants.getOverlayCoordinates(0));
            sleeve.render(stack, translucentBuffer, packedLight, ReClientConstants.getOverlayCoordinates(0));
        }

        // Emissive texture
        ResourceLocation emissiveLocation = OutfitDefinition.getEmissiveOutfitTexture(slot, definition, slimArms);
        if (emissiveLocation != null) {
            VertexConsumer emissiveBuffer = buffer.getBuffer(RenderType.eyes(emissiveLocation));
            int emissiveColor = ReClientOptions.COLOR_EMISSIVE_OUTFIT_PARTS.get() ? color : -1;

            arm.render(stack, emissiveBuffer, ReClientConstants.EMISSIVE_LIGHT_VALUE, ReClientConstants.getOverlayCoordinates(0), emissiveColor);
            sleeve.render(stack, emissiveBuffer, ReClientConstants.EMISSIVE_LIGHT_VALUE, ReClientConstants.getOverlayCoordinates(0), emissiveColor);
        }

        // Glint
        if (!fullBody && chestStack.hasFoil()) {
            arm.render(stack, buffer.getBuffer(RenderType.entityGlint()), packedLight, OverlayTexture.NO_OVERLAY);
            sleeve.render(stack, buffer.getBuffer(RenderType.entityGlint()), packedLight, OverlayTexture.NO_OVERLAY);
        }
        stack.popPose();
        minecraft.getProfiler().pop();
    }

    public static void renderArmorInArm(AbstractClientPlayer player, HumanoidArm arm, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (ReClientOptions.RENDER_ARMOR_ON_HAND.get() && chestStack.getItem() instanceof ArmorItem item && !chestStack.has(ReDataComponents.OUTFIT)) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getProfiler().push(Reutilities.reutilities("hand_armor_rendering").toString());
            HumanoidModel<AbstractClientPlayer> armorModel = new HumanoidModel<>(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
            List<ArmorMaterial.Layer> layers = item.getMaterial().value().layers();
            ModelPart rightArm = armorModel.rightArm;
            if (arm == HumanoidArm.LEFT) rightArm = armorModel.leftArm;

            armorModel.attackTime = 0;
            armorModel.swimAmount = 0;
            armorModel.crouching = false;
            armorModel.setupAnim(player, 0, 0, 0, 0, 0);
            ResourceLocation armorTexture = ClientHooks.getArmorTexture(player, chestStack, layers.getFirst(), false, EquipmentSlot.CHEST);
            VertexConsumer cutoutBuffer = buffer.getBuffer(RenderType.armorCutoutNoCull(armorTexture));

            packedLight = ReAPI.getItemBrightness(chestStack, packedLight, player.level(), player.blockPosition(), true);

            // Base armor model
            DyedItemColor dyedColor = chestStack.get(DataComponents.DYED_COLOR);
            int defaultColor = dyedColor != null && chestStack.is(ItemTags.DYEABLE) ? dyedColor.rgb() : (item.getMaterial().getRegisteredName().equals("minecraft:leather") ? DyedItemColor.LEATHER_COLOR : 0xFFFFFF);
            rightArm.render(stack, cutoutBuffer, packedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(255, defaultColor));

            // Armor trim
            ArmorTrim trim = chestStack.get(DataComponents.TRIM);
            if (trim != null) {
                TextureAtlasSprite trimSprite = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET).getSprite(trim.outerTexture(item.getMaterial()));
                VertexConsumer trimBuffer = trimSprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
                int trimLight = trim.material().is(ReTrimMaterialTags.EMISSIVE_LIGHTING) && ReCommonOptions.LIGHT_EMITTING_EMISSIVES.get() ? ReClientConstants.EMISSIVE_LIGHT_VALUE : packedLight;
                rightArm.render(stack, trimBuffer, trimLight, OverlayTexture.NO_OVERLAY);
            }

            // Glint
            if (chestStack.hasFoil()) {
                rightArm.render(stack, buffer.getBuffer(RenderType.armorEntityGlint()), packedLight, OverlayTexture.NO_OVERLAY);
            }
            minecraft.getProfiler().pop();
        }
    }
}
