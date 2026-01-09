package melonystudios.reutilities.entity.cape;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.custom.CapePositioning;
import melonystudios.reutilities.util.ReClientConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RecapeLayer<T extends LivingEntity & OffsetGetter, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private final ModelPart capePart;

    public RecapeLayer(RenderLayerParent<T, A> renderer, ModelPart capePart) {
        super(renderer);
        this.capePart = capePart;
    }

    public ModelPart getCapePart() {
        return this.capePart;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, T livEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch) {
        if (livEntity.isInvisible()) return;
        WornRecape cape = livEntity.getCapability(ReAPI.CAPE_CAPABILITY);
        if (cape == null) return;
        CapePositioning positioning = CapePositioning.positionCapeBasedOnOutfit(livEntity.getItemBySlot(EquipmentSlot.CHEST), livEntity.getCapability(ReAPI.OUTFIT_CAPABILITY));
        if (positioning.isCapeHidden()) return;

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getProfiler().push(Reutilities.reutilities("recape").toString());
        cape.moveCape(livEntity, livEntity.getOffsets());
        CapeOffsets offsets = livEntity.getOffsets();

        stack.pushPose();
        stack.translate(0, 0, 0.125F);
        double d0 = Mth.lerp(partialTicks, offsets.capeXOld, offsets.capeX) - Mth.lerp(partialTicks, livEntity.xo, livEntity.getX());
        double d1 = Mth.lerp(partialTicks, offsets.capeYOld, offsets.capeY) - Mth.lerp(partialTicks, livEntity.yo, livEntity.getY());
        double d2 = Mth.lerp(partialTicks, offsets.capeZOld, offsets.capeZ) - Mth.lerp(partialTicks, livEntity.zo, livEntity.getZ());
        float f = Mth.rotLerp(partialTicks, livEntity.yBodyRotO, livEntity.yBodyRot);
        double d3 = Mth.sin(f * (float) (Math.PI / 180));
        double d4 = -Mth.cos(f * (float) (Math.PI / 180));
        float f1 = (float) d1 * 10;
        f1 = Mth.clamp(f1, -6, 32);
        float f2 = (float) (d0 * d3 + d2 * d4) * 100;
        f2 = Mth.clamp(f2, 0, 150);
        float f3 = (float) (d0 * d4 - d2 * d3) * 100;
        f3 = Mth.clamp(f3, -20, 20);
        if (f2 < 0) {
            f2 = 0;
        }

        float f4 = /*Mth.lerp(partialTicks, livEntity.oBob, livEntity.bob);*/0;
        f1 += Mth.sin(Mth.lerp(partialTicks, livEntity.walkDistO, livEntity.walkDist) * 6) * 32 * f4;
        if (livEntity.isCrouching()) {
            f1 += 25;
        }

        // actually render the cape
        stack.mulPose(Axis.XP.rotationDegrees(6 + f2 / 2 + f1));
        stack.mulPose(Axis.ZP.rotationDegrees(f3 / 2));
        stack.mulPose(Axis.YP.rotationDegrees(180 - f3 / 2));

        // base cape
        VertexConsumer solidBuffer = buffer.getBuffer(RenderType.entitySolid(cape.cape().value().cape()));
        this.capePart.render(stack, solidBuffer, packedLight, OverlayTexture.NO_OVERLAY);

        // emissive texture
        if (cape.cape().value().emissive().isPresent()) {
            VertexConsumer emissiveBuffer = buffer.getBuffer(RenderType.eyes(cape.cape().value().emissive().get()));
            this.capePart.render(stack, emissiveBuffer, ReClientConstants.EMISSIVE_LIGHT_VALUE, ReClientConstants.getOverlayCoordinates(0));
        }

        stack.popPose();
        minecraft.getProfiler().pop();
    }
}
