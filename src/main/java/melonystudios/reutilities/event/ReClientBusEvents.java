package melonystudios.reutilities.event;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.entity.ReEntities;
import melonystudios.reutilities.entity.renderer.ReBoatRenderer;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Reutilities.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ReClientBusEvents {
    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (BoatType type : Reconstants.BOATS.values()) {
            event.registerLayerDefinition(ReBoatRenderer.createBoatModelName(type), BoatModel::createBodyModel);
            event.registerLayerDefinition(ReBoatRenderer.createChestBoatModelName(type), ChestBoatModel::createBodyModel);
        }
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Block Entity Renderers
        event.registerBlockEntityRenderer(ReBlockEntities.SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ReBlockEntities.HANGING_SIGN.get(), HangingSignRenderer::new);

        // Entity Renderers
        event.registerEntityRenderer(ReEntities.BOAT.get(), context -> new ReBoatRenderer(context, false));
        event.registerEntityRenderer(ReEntities.CHEST_BOAT.get(), context -> new ReBoatRenderer(context, true));
    }
}
