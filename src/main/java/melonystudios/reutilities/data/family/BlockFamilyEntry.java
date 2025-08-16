package melonystudios.reutilities.data.family;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/// Represents a single provider in a {@linkplain BlockFamilyModelProvider block family provider}.
/// A provider is a "factory" that makes the block model for a specified block type, like "<code>stripped_log</code>" or "<code>fence</code>".
public interface BlockFamilyEntry {
    /// Makes a block model for a block family.
    /// @param block The block being generated.
    /// @param identifier The registry name of the block, used for getting the texture when <code>this.</code>{@link BlockFamilyModelProvider#texture texture} doesn't work.
    void makeModel(Block block, ResourceLocation identifier);
}
