package melonystudios.reutilities.event;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.entity.ReEntities;
import melonystudios.reutilities.entity.layer.OutfitLayer;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.entity.renderer.ReBoatRenderer;
import melonystudios.reutilities.mixin.renderer.PlayerSlimAccessor;
import melonystudios.reutilities.util.ReRegistries;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Reutilities.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ReEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model model : event.getSkins()) {
            if (event.getSkin(model) instanceof PlayerRenderer renderer) {
                renderer.addLayer(new OutfitLayer<>(renderer, ((PlayerSlimAccessor) renderer.getModel()).reutilities$slimArms()));
            }
        }
    }

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

    @SubscribeEvent
    public static void addSignBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(ReBlockEntities.SIGN.get(), Reconstants.SIGNS.toArray(new Block[0]));
        event.modify(ReBlockEntities.HANGING_SIGN.get(), Reconstants.HANGING_SIGNS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void addDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ReRegistries.OUTFIT_DEFINITION, OutfitDefinition.DIRECT_CODEC, OutfitDefinition.DIRECT_CODEC);
    }
}
