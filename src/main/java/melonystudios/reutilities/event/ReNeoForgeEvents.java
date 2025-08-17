package melonystudios.reutilities.event;

import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.entity.renderer.HandArmorRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;

@EventBusSubscriber(modid = Reutilities.MOD_ID)
public class ReNeoForgeEvents {
    @SubscribeEvent
    public static void renderArmorInArm(RenderArmEvent event) {
        if (ReConfigs.RENDER_OUTFITS.get()) HandArmorRenderer.renderOutfitInArm(event.getPlayer(), event.getArm(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        HandArmorRenderer.renderArmorInArm(event.getPlayer(), event.getArm(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
    }
}
