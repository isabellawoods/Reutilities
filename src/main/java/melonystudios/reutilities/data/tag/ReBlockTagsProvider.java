package melonystudios.reutilities.data.tag;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.util.tag.ReBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public final class ReBlockTagsProvider extends BlockTagsProvider {
    public ReBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper fileHelper) {
        super(output, registries, Reutilities.MOD_ID, fileHelper);
    }

    @Override
    @NotNull
    public String getName() {
        return Reutilities.generatorName("Block Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Reutilities' tags
        this.tag(ReBlockTags.FARMLAND).add(Blocks.FARMLAND);
        this.tag(ReBlockTags.FARMLAND_TRANSPARENT).addTag(Tags.Blocks.FENCE_GATES).add(Blocks.MOVING_PISTON);
        this.tag(ReBlockTags.PLANTS_MAY_PLACE_ON).addTag(BlockTags.DIRT).addTag(ReBlockTags.FARMLAND);
        this.tag(ReBlockTags.CROPS_MAY_PLACE_ON).addTag(ReBlockTags.FARMLAND);
        this.tag(ReBlockTags.AZALEA_MAY_PLACE_ON).addTag(ReBlockTags.PLANTS_MAY_PLACE_ON).add(Blocks.CLAY);
        this.tag(ReBlockTags.MANGROVE_PROPAGULE_MAY_PLACE_ON).addTag(ReBlockTags.PLANTS_MAY_PLACE_ON).add(Blocks.CLAY);
        this.tag(ReBlockTags.LILY_PAD_MAY_PLACE_ON).add(Blocks.ICE, Blocks.FROSTED_ICE);
        this.tag(ReBlockTags.FUNGI_MAY_PLACE_ON).addTag(ReBlockTags.PLANTS_MAY_PLACE_ON).addTag(BlockTags.NYLIUM).add(Blocks.MYCELIUM, Blocks.SOUL_SOIL);
        this.tag(ReBlockTags.ROOTS_MAY_PLACE_ON).addTag(ReBlockTags.PLANTS_MAY_PLACE_ON).addTag(BlockTags.NYLIUM).add(Blocks.SOUL_SOIL);
        this.tag(ReBlockTags.NETHER_SPROUTS_MAY_PLACE_ON).addTag(ReBlockTags.PLANTS_MAY_PLACE_ON).addTag(BlockTags.NYLIUM).add(Blocks.SOUL_SOIL);
        this.tag(ReBlockTags.WITHER_ROSE_MAY_PLACE_ON).addTag(ReBlockTags.PLANTS_MAY_PLACE_ON).addTag(Tags.Blocks.NETHERRACKS).add(Blocks.SOUL_SAND, Blocks.SOUL_SOIL);
        this.tag(ReBlockTags.NETHER_WART_MAY_PLACE_ON).add(Blocks.SOUL_SAND);
        this.tag(ReBlockTags.SEAGRASS_CANNOT_PLACE_ON).add(Blocks.MAGMA_BLOCK);

        // NeoForge's tags
        this.tag(Tags.Blocks.VILLAGER_FARMLANDS).addTag(ReBlockTags.FARMLAND);

        // Minecraft tags
        this.tag(BlockTags.MINEABLE_WITH_HOE).addTag(BlockTags.LEAVES);
        this.tag(BlockTags.SNIFFER_DIGGABLE_BLOCK).addTag(ReBlockTags.FARMLAND);
    }
}
