package melonystudios.reutilities.util.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ReBlockTags {
    /// Grouping tag for all farmland.
    public static final TagKey<Block> FARMLAND = reutilities("farmland");
    /// Blocks in this tag don't transform farmland and dirt paths into dirt.
    public static final TagKey<Block> FARMLAND_TRANSPARENT = reutilities("farmland_transparent");
    /// Plants (some {@link net.minecraft.world.level.block.BushBlock BushBlock}`s`) can be placed on these blocks.
    public static final TagKey<Block> PLANTS_MAY_PLACE_ON = reutilities("may_place_on/plants");
    /// Crops in general can be placed on these blocks.
    public static final TagKey<Block> CROPS_MAY_PLACE_ON = reutilities("may_place_on/crops");
    public static final TagKey<Block> AZALEA_MAY_PLACE_ON = reutilities("may_place_on/azalea");
    public static final TagKey<Block> MANGROVE_PROPAGULE_MAY_PLACE_ON = reutilities("may_place_on/mangrove_propagule");
    public static final TagKey<Block> LILY_PAD_MAY_PLACE_ON = reutilities("may_place_on/lily_pad");
    public static final TagKey<Block> FUNGI_MAY_PLACE_ON = reutilities("may_place_on/fungi");
    public static final TagKey<Block> ROOTS_MAY_PLACE_ON = reutilities("may_place_on/roots");
    public static final TagKey<Block> NETHER_SPROUTS_MAY_PLACE_ON = reutilities("may_place_on/nether_sprouts");
    public static final TagKey<Block> WITHER_ROSE_MAY_PLACE_ON = reutilities("may_place_on/wither_rose");
    public static final TagKey<Block> NETHER_WART_MAY_PLACE_ON = reutilities("may_place_on/nether_wart");
    public static final TagKey<Block> SEAGRASS_CANNOT_PLACE_ON = reutilities("seagrass_cannot_place_on");

    /// Creates a new block tag with a specified name.
    /// @param name The tag's name, under ***Reutilities***' namespace.
    public static TagKey<Block> reutilities(String name) {
        return TagKey.create(Registries.BLOCK, Reutilities.reutilities(name));
    }
}
