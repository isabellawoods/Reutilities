package melonystudios.reutilities.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ReBlocks {
    /// Creates a new leaf block using a {@linkplain MapColor map color}.
    /// @param color The map color to use.
    /// @param sound The sound to use for this block.
    public static LeavesBlock leaves(MapColor color, SoundType sound) {
        return new LeavesBlock(BlockBehaviour.Properties.of().mapColor(color).strength(0.2F).ignitedByLava().randomTicks().noOcclusion().sound(sound).pushReaction(PushReaction.DESTROY).isSuffocating(ReBlocks::never).isViewBlocking(ReBlocks::never).isRedstoneConductor(ReBlocks::never));
    }

    /// Helper method for using {@link BlockBehaviour.StatePredicate StatePredicate}.
    public static boolean never(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    /// Helper method for using {@link BlockBehaviour.StatePredicate StatePredicate}.
    public static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }
}
