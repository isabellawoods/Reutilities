package melonystudios.reutilities.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ReDirtPathBlock extends DirtPathBlock implements PixelShortBlock {
    public ReDirtPathBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getBaseBlock() {
        return Blocks.DIRT.defaultBlockState();
    }

    @Override
    @NotNull
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ?
                Block.pushEntitiesUp(this.defaultBlockState(), this.getBaseBlock(), context.getLevel(), context.getClickedPos()) :
                super.getStateForPlacement(context);
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        this.returnToBaseBlock(null, state, world, pos);
    }

    @Override
    @NotNull
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return PIXEL_SHORT_SHAPE;
    }
}
