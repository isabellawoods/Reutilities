package melonystudios.reutilities.entity.outfit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import melonystudios.reutilities.Reutilities;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class OutfitModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation CLASSIC = new ModelLayerLocation(Reutilities.reutilities("outfit/classic"), "main");
    public static final ModelLayerLocation SLIM = new ModelLayerLocation(Reutilities.reutilities("outfit/slim"), "main");
    public final ModelPart jacket;
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    private final boolean slimArms;

    public OutfitModel(ModelPart root, boolean slimArms) {
        super(root);
        this.slimArms = slimArms;
        this.jacket = root.getChild("jacket");
        this.leftSleeve = root.getChild("left_sleeve");
        this.rightSleeve = root.getChild("right_sleeve");
        this.leftPants = root.getChild("left_pants");
        this.rightPants = root.getChild("right_pants");
    }

    public boolean slimArms() {
        return this.slimArms;
    }

    public static MeshDefinition createBodyLayer(CubeDeformation deformation, boolean slimArms) {
        MeshDefinition mesh = HumanoidModel.createMesh(deformation, 0);
        PartDefinition definition = mesh.getRoot();

        if (slimArms) {
            definition.addOrReplaceChild("left_arm",
                    CubeListBuilder.create().texOffs(32, 48).addBox(-1, -2, -2, 3, 12, 4, deformation),
                    PartPose.offset(5, 2.5F, 0));
            definition.addOrReplaceChild("right_arm",
                    CubeListBuilder.create().texOffs(40, 16).addBox(-2, -2, -2, 3, 12, 4, deformation),
                    PartPose.offset(-5, 2.5F, 0));
            definition.addOrReplaceChild("left_sleeve",
                    CubeListBuilder.create().texOffs(48, 48).addBox(-1, -2, -2, 3, 12, 4, deformation.extend(0.25F)),
                    PartPose.offset(5, 2.5F, 0));
            definition.addOrReplaceChild("right_sleeve",
                    CubeListBuilder.create().texOffs(40, 32).addBox(-2, -2, -2, 3, 12, 4, deformation.extend(0.25F)),
                    PartPose.offset(-5, 2.5F, 0));
        } else {
            definition.addOrReplaceChild("left_arm",
                    CubeListBuilder.create().texOffs(32, 48).addBox(-1, -2, -2, 4, 12, 4, deformation),
                    PartPose.offset(5, 2, 0));
            definition.addOrReplaceChild("left_sleeve",
                    CubeListBuilder.create().texOffs(48, 48).addBox(-1, -2, -2, 4, 12, 4, deformation.extend(0.25F)),
                    PartPose.offset(5, 2, 0));
            definition.addOrReplaceChild("right_sleeve",
                    CubeListBuilder.create().texOffs(40, 32).addBox(-3, -2, -2, 4, 12, 4, deformation.extend(0.25F)),
                    PartPose.offset(-5, 2, 0));
        }

        definition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2, 0, -2, 4, 12, 4, deformation), PartPose.offset(1.9F, 12, 0));
        definition.addOrReplaceChild("left_pants",
                CubeListBuilder.create().texOffs(0, 48).addBox(-2, 0, -2, 4, 12, 4, deformation.extend(0.25F)),
                PartPose.offset(1.9F, 12, 0));
        definition.addOrReplaceChild("right_pants",
                CubeListBuilder.create().texOffs(0, 32).addBox(-2, 0, -2, 4, 12, 4, deformation.extend(0.25F)),
                PartPose.offset(-1.9F, 12, 0));
        definition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4, 0, -2, 8, 12, 4, deformation.extend(0.25F)), PartPose.ZERO);
        return mesh;
    }

    @Override
    @NotNull
    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket));
    }

    @Override
    public void setupAnim(T mob, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        super.setupAnim(mob, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch);
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
    }

    @Override
    public void setAllVisible(boolean visible) {
        super.setAllVisible(visible);
        this.leftSleeve.visible = visible;
        this.rightSleeve.visible = visible;
        this.leftPants.visible = visible;
        this.rightPants.visible = visible;
        this.jacket.visible = visible;
    }

    public void copyPropertiesFrom(PlayerModel<?> model) {
        this.attackTime = model.attackTime;
        this.riding = model.riding;
        this.young = model.young;
        this.leftArmPose = model.leftArmPose;
        this.rightArmPose = model.rightArmPose;
        this.crouching = model.crouching;

        this.head.copyFrom(model.head);
        this.hat.copyFrom(model.hat);
        this.body.copyFrom(model.body);
        this.leftArm.copyFrom(model.leftArm);
        this.rightArm.copyFrom(model.rightArm);
        this.leftLeg.copyFrom(model.leftLeg);
        this.rightLeg.copyFrom(model.rightLeg);

        this.jacket.copyFrom(model.jacket);
        this.leftSleeve.copyFrom(model.leftSleeve);
        this.rightSleeve.copyFrom(model.rightSleeve);
        this.leftPants.copyFrom(model.leftPants);
        this.rightPants.copyFrom(model.rightPants);
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack stack) {
        ModelPart arm = this.getArm(side);
        if (this.slimArms) {
            float offset = 0.5F * (float) (side == HumanoidArm.RIGHT ? 1 : -1);
            arm.x += offset;
            arm.translateAndRotate(stack);
            arm.x -= offset;
        } else {
            arm.translateAndRotate(stack);
        }
    }
}
