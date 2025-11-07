package melonystudios.reutilities.data.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static melonystudios.reutilities.util.tag.ReItemTags.*;

@ApiStatus.Internal
public class ReItemTagsProvider extends ItemTagsProvider {
    public ReItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper fileHelper) {
        super(output, registries, blockTags, Reutilities.MOD_ID, fileHelper);
    }

    @Override
    @NotNull
    public String getName() {
        return Reutilities.generatorName("Item Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Common tags
        this.tag(EMISSIVE_LIGHTING).addTag(Tags.Items.DUSTS_GLOWSTONE).addTag(Tags.Items.RODS_BLAZE).add(Items.BLAZE_POWDER, Items.BLAZE_SPAWN_EGG, Items.GLOW_INK_SAC, Items.GLOW_SQUID_SPAWN_EGG,
                Items.NETHER_STAR, Items.END_CRYSTAL);
        this.tag(DUAL_WIELDED);
        this.tag(LOGOS);
        this.tag(FLINT).add(Items.FLINT);
        this.tag(NETHER_STARS).add(Items.NETHER_STAR);

        this.tag(SUGAR_DUSTS).add(Items.SUGAR);
        this.tag(GUNPOWDER_DUSTS).add(Items.GUNPOWDER);
        this.tag(BLAZE_DUSTS).add(Items.BLAZE_POWDER);
        this.tag(Tags.Items.DUSTS).addTag(SUGAR_DUSTS).addTag(GUNPOWDER_DUSTS).addTag(BLAZE_DUSTS);
    }
}
