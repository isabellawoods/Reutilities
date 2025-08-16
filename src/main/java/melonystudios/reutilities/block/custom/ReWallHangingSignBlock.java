package melonystudios.reutilities.block.custom;

import melonystudios.reutilities.blockentity.custom.ReHangingSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

public class ReWallHangingSignBlock extends WallHangingSignBlock {
    public ReWallHangingSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    @NotNull
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReHangingSignBlockEntity(pos, state);
    }
}
