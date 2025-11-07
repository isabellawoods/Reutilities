package melonystudios.reutilities.event;

import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.command.ItemBehaviorCommand;
import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.data.tag.ReBlockTagsProvider;
import melonystudios.reutilities.data.tag.ReEntityTypeTagsProvider;
import melonystudios.reutilities.data.tag.ReItemTagsProvider;
import melonystudios.reutilities.data.tag.ReTrimMaterialTagsProvider;
import melonystudios.reutilities.entity.ReEntities;
import melonystudios.reutilities.entity.outfit.OutfitLayer;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.entity.outfit.OutfitModel;
import melonystudios.reutilities.entity.renderer.ReBoatRenderer;
import melonystudios.reutilities.mixin.renderer.PlayerSlimAccessor;
import melonystudios.reutilities.util.ReRegistries;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ScreenshotEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Reutilities.MOD_ID)
public class ReEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ItemBehaviorCommand.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model model : event.getSkins()) {
            if (event.getSkin(model) instanceof PlayerRenderer renderer) {
                boolean slimArms = ((PlayerSlimAccessor) renderer.getModel()).reutilities$slimArms();
                renderer.addLayer(new OutfitLayer<>(renderer, new OutfitModel<>(event.getContext().bakeLayer(slimArms ? OutfitModel.SLIM : OutfitModel.CLASSIC), slimArms)));
            }
        }
    }

    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OutfitModel.CLASSIC, () -> LayerDefinition.create(OutfitModel.createBodyLayer(CubeDeformation.NONE, false), 64, 64));
        event.registerLayerDefinition(OutfitModel.SLIM, () -> LayerDefinition.create(OutfitModel.createBodyLayer(CubeDeformation.NONE, true), 64, 64));

        for (BoatType type : Reconstants.BOATS.values()) {
            event.registerLayerDefinition(ReBoatRenderer.createBoatModelName(type), BoatModel::createBodyModel);
            event.registerLayerDefinition(ReBoatRenderer.createChestBoatModelName(type), ChestBoatModel::createBodyModel);
        }
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Block entity renderers
        event.registerBlockEntityRenderer(ReBlockEntities.SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ReBlockEntities.HANGING_SIGN.get(), HangingSignRenderer::new);

        // Entity renderers
        event.registerEntityRenderer(ReEntities.BOAT.get(), context -> new ReBoatRenderer(context, false));
        event.registerEntityRenderer(ReEntities.CHEST_BOAT.get(), context -> new ReBoatRenderer(context, true));
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        if (event.includeServer()) {
            // Tags
            ReBlockTagsProvider blockTags = new ReBlockTagsProvider(output, registries, fileHelper);
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new ReItemTagsProvider(output, registries, blockTags.contentsGetter(), fileHelper));
            generator.addProvider(true, new ReEntityTypeTagsProvider(output, registries, fileHelper));
            generator.addProvider(true, new ReTrimMaterialTagsProvider(output, registries, fileHelper));
        }
    }

    @SubscribeEvent
    public static void addSignBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(ReBlockEntities.SIGN.get(), Reconstants.SIGNS.toArray(new Block[0]));
        event.modify(ReBlockEntities.HANGING_SIGN.get(), Reconstants.HANGING_SIGNS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void addBuiltInRegistries(NewRegistryEvent event) {
        event.register(BehaviorAPI.ITEM_BEHAVIOR);
        event.register(BehaviorAPI.ITEM_BEHAVIOR_SERIALIZER);
    }

    @SubscribeEvent
    public static void addDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ReRegistries.OUTFIT_DEFINITION, OutfitDefinition.DIRECT_CODEC, OutfitDefinition.DIRECT_CODEC);
    }

    @SubscribeEvent
    public static void takePanoramicScreenshot(ScreenshotEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        int size = ReConfigs.PANORAMIC_SCREENSHOT_SIZE.get();
        if (ReConfigs.PANORAMIC_SCREENSHOTS.get() && Screen.hasControlDown() && minecraft.level != null && event.getImage().getWidth() != size && event.getImage().getHeight() != size) {
            event.setCanceled(true);
            // make the folder where the panorama will be saved
            // ".minecraft/panorama/<date>"
            Reconstants.CURRENT_PANORAMA_OUTPUT_FOLDER = Util.getFilenameFormattedDateTime();
            String fileLocation = "panorama/" + Reconstants.CURRENT_PANORAMA_OUTPUT_FOLDER;
            File outputFolder = new File(minecraft.gameDirectory, fileLocation);
            outputFolder.mkdir();

            // warning when making panoramas with higher (>1024) resolutions
            if (size > 1536) { // 1.5x default resolution
                minecraft.gui.getChat().addMessage(Component.translatable("screenshot.high_resolution", size).withStyle(ChatFormatting.YELLOW));
            }

            // Saved panorama to "panorama/<date>" message
            Component component = Component.literal(fileLocation)
                    .withStyle(style -> style.withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, outputFolder.getAbsolutePath())));
            event.setResultMessage(Component.translatable("screenshot.success.panoramic", component));

            // actually take the screenshots
            minecraft.grabPanoramixScreenshot(minecraft.gameDirectory, size, size);
        }
    }
}
