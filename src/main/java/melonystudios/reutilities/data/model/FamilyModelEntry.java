package melonystudios.reutilities.data.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/// Represents a single provider in a {@linkplain BlockFamilyModelProvider block family model provider}.
/// A provider is a "factory" that makes the block model for a specified block type, like "`stripped_log`" or "`fence`".
public interface FamilyModelEntry {
    /// Makes a block model for a block family.
    /// @param block The block being generated.
    /// @param identifier The registry name of the block, used for getting the texture when `this.`{@link BlockFamilyModelProvider#texture texture} doesn't work.
    void makeModel(Block block, ResourceLocation identifier);
}
