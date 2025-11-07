package melonystudios.reutilities.event;

import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.entity.renderer.HandArmorRenderer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = Reutilities.MOD_ID)
public class ReNeoForgeEvents {
    @SubscribeEvent
    public static void renderArmorInArm(RenderArmEvent event) {
        if (ReConfigs.RENDER_OUTFITS.get()) HandArmorRenderer.renderOutfitInArm(event.getPlayer(), event.getArm(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        HandArmorRenderer.renderArmorInArm(event.getPlayer(), event.getArm(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        ReAPI.runItemBehavior(player.getMainHandItem(), player.level(), player, GlobalSettings.ATTACKING);
    }
}
