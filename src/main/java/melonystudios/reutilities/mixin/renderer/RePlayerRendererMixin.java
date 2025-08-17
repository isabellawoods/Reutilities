package melonystudios.reutilities.mixin.renderer;

import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class RePlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public RePlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "setModelProperties", at = @At("TAIL"))
    private void hideLayersForOutfit(AbstractClientPlayer player, CallbackInfo callback) {
        PlayerModel<AbstractClientPlayer> model = this.getModel();

        if (!ReConfigs.RENDER_OUTFITS.get()) return;
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.has(ReDataComponents.OUTFIT)) {
                OutfitDefinition definition = stack.get(ReDataComponents.OUTFIT).value();
                Equipable equipable = Equipable.get(stack);
                if (equipable == null) return;

                if (OutfitDefinition.shouldHideLayer(equipable.getEquipmentSlot(), definition, ((PlayerSlimAccessor) model).reutilities$slimArms())) {
                    this.hideModelLayers(model, equipable.getEquipmentSlot());
                }
            }
        }
    }

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> callback) {
        ItemStack handStack = player.getItemInHand(hand);
        if (!handStack.isEmpty() && !player.swinging) {
            if (handStack.is(ReItemTags.DUAL_WIELDED)) callback.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_CHARGE);
        }
    }

    @Unique
    private void hideModelLayers(PlayerModel<AbstractClientPlayer> model, EquipmentSlot slot) {
        switch (slot) {
            case HEAD: {
                model.hat.visible = false;
                break;
            }
            case CHEST: {
                model.jacket.visible = false;
                model.rightSleeve.visible = false;
                model.leftSleeve.visible = false;
                break;
            }
            case LEGS:
            case FEET: {
                model.rightPants.visible = false;
                model.leftPants.visible = false;
                break;
            }
        }
    }
}
