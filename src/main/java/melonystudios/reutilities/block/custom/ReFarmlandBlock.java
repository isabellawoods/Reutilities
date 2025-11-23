package melonystudios.reutilities.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.FarmlandWaterManager;
import org.jetbrains.annotations.NotNull;

public class ReFarmlandBlock extends FarmBlock implements PixelShortBlock {
    public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;

    public ReFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 0));
    }

    @Override
    public BlockState getBaseBlock() {
        return Blocks.DIRT.defaultBlockState();
    }

    @Override
    @NotNull
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? this.getBaseBlock() : super.getStateForPlacement(context);
    }

    @Override
    @NotNull
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return PIXEL_SHORT_SHAPE;
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        if (!state.canSurvive(world, pos)) this.returnToBaseBlock(null, state, world, pos);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        int moisture = state.getValue(MOISTURE);
        if (!isNearHydratingFluid(world, pos) && !world.isRainingAt(pos.above())) {
            if (moisture > 0) {
                world.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            } else if (!shouldMaintainFarmland(world, pos)) {
                this.returnToBaseBlock(null, state, world, pos);
            }
        } else if (moisture < 7) {
            world.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClientSide() && CommonHooks.onFarmlandTrample(world, pos, this.getBaseBlock(), fallDistance, entity)) {
            this.returnToBaseBlock(entity, state, world, pos);
        }

        super.fallOn(world, state, pos, entity, fallDistance);
    }

    /// @param world The world.
    /// @param pos This block's position in the world.
    /// @return Whether this block should still be farmland, or whether the block above is in the `#minecraft:maintains_farmland` block tag.
    public static boolean shouldMaintainFarmland(BlockGetter world, BlockPos pos) {
        return world.getBlockState(pos.above()).is(BlockTags.MAINTAINS_FARMLAND);
    }

    /// @param world The world.
    /// @param pos This block's position in the world.
    /// @return Whether this farmland is within 4 blocks of a hydrating source, or whether said fluid can hydrate this block.
    public static boolean isNearHydratingFluid(LevelReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        for (BlockPos pos1 : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
            if (state.canBeHydrated(world, pos, world.getFluidState(pos1), pos1)) return true;
        }

        return FarmlandWaterManager.hasBlockWaterTicket(world, pos);
    }

    @Override
    public boolean canBeHydrated(BlockState state, BlockGetter world, BlockPos pos, FluidState fluid, BlockPos fluidPos) {
        return fluid.is(this.getHydratingFluid());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MOISTURE);
    }
}
