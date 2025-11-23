package melonystudios.reutilities.block.custom;

import melonystudios.reutilities.util.tag.ReFluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public interface PixelShortBlock {
    VoxelShape PIXEL_SHORT_SHAPE = Block.box(0, 0, 0, 16, 15, 16);

    /// Represents the block that this block will turn into when not hydrated or not placed in the correct stop.
    /// Defaults to:
    /// - **Dirt** for vanilla farmland and dirt paths;
    /// - **Numo Dirt** for *Numinosity*'s farmland and dirt paths.
    /// - **Netherrack** for Nether farmland added by *Revaried*.
    BlockState getBaseBlock();

    /// Represents the fluid that this farmland requires to stay hydrated. Defaults to:
    /// - **Water** (`#reutilities:water_based_farmland`) for vanilla and *Numinosity*'s farmland.
    /// * **Lava** (`#reutilities:lava_based_farmland`) for *Revaried*'s Nether farmland.
    default TagKey<Fluid> getHydratingFluid() {
        return ReFluidTags.HYDRATES_WATER_BASED_FARMLAND;
    }

    /// Turns this farmland block back to its base block, sending a "block change" game event.
    /// @param entity *(optional)* the entity trampling this farmland.
    /// @param state The block state being checked.
    /// @param world The world.
    /// @param pos This block's position in the world.
    default void returnToBaseBlock(@Nullable Entity entity, BlockState state, Level world, BlockPos pos) {
        BlockState baseState = Block.pushEntitiesUp(state, this.getBaseBlock(), world, pos);
        world.setBlockAndUpdate(pos, baseState);
        world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, baseState));
    }
}
