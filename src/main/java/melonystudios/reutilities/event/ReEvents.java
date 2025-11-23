package melonystudios.reutilities.event;

import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.command.ItemBehaviorCommand;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.command.ReCommands;
import melonystudios.reutilities.data.recipe.InternalReRecipeProvider;
import melonystudios.reutilities.data.tag.*;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.util.ReRegistries;
import melonystudios.reutilities.util.ReCommonConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Reutilities.MOD_ID)
public class ReEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ItemBehaviorCommand.register(event.getDispatcher(), event.getBuildContext());
        ReCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        ReAPI.runItemBehavior(player.getMainHandItem(), player.level(), player, GlobalSettings.ATTACKING);
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
            generator.addProvider(true, new ReFluidTagsProvider(output, registries, fileHelper));
            generator.addProvider(true, new ReTrimMaterialTagsProvider(output, registries, fileHelper));

            // Miscellaneous
            generator.addProvider(true, new InternalReRecipeProvider(output, registries));
        }
    }

    @SubscribeEvent
    public static void addSignBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(ReBlockEntities.SIGN.get(), ReCommonConstants.SIGNS.toArray(new Block[0]));
        event.modify(ReBlockEntities.HANGING_SIGN.get(), ReCommonConstants.HANGING_SIGNS.toArray(new Block[0]));
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
}
