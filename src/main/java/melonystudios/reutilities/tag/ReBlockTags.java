package melonystudios.reutilities.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ReBlockTags {
    /// Grouping tag for all farmland.
    public static final TagKey<Block> FARMLAND = reutilities("farmland");
    /// Blocks in this tag don't transform farmland and dirt paths into dirt.
    public static final TagKey<Block> FARMLAND_TRANSPARENT = reutilities("farmland_transparent");
    /// Most vegetation based on [`BushBlock`][net.minecraft.world.level.block.BushBlock] can be placed on these blocks.
    public static final TagKey<Block> SUPPORTS_VEGETATION = reutilities("supports_vegetation");
    /// Crops in general can be placed on these blocks.
    public static final TagKey<Block> SUPPORTS_CROPS = reutilities("supports_crops");
    public static final TagKey<Block> SUPPORTS_AZALEA = reutilities("supports_azalea");
    public static final TagKey<Block> SUPPORTS_MANGROVE_PROPAGULE = reutilities("supports_mangrove_propagule");
    public static final TagKey<Block> SUPPORTS_LILY_PAD = reutilities("supports_lily_pad");
    public static final TagKey<Block> SUPPORTS_NETHER_FUNGI = reutilities("supports_nether_fungi");
    public static final TagKey<Block> SUPPORTS_NETHER_ROOTS = reutilities("supports_nether_roots");
    public static final TagKey<Block> SUPPORTS_NETHER_SPROUTS = reutilities("supports_nether_sprouts");
    public static final TagKey<Block> SUPPORTS_WITHER_ROSE = reutilities("supports_wither_rose");
    public static final TagKey<Block> SUPPORTS_NETHER_WART = reutilities("supports_nether_wart");
    public static final TagKey<Block> CANNOT_SUPPORT_SEAGRASS = reutilities("cannot_support_seagrass");

    /// Creates a new block tag with a specified name.
    /// @param name The tag's name, under ***Reutilities***' namespace.
    public static TagKey<Block> reutilities(String name) {
        return TagKey.create(Registries.BLOCK, Reutilities.reutilities(name));
    }
}
