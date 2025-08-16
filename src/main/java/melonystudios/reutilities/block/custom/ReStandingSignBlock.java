package melonystudios.reutilities.block.custom;

import melonystudios.reutilities.blockentity.custom.ReSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

public class ReStandingSignBlock extends StandingSignBlock {
    public ReStandingSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    @NotNull
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReSignBlockEntity(pos, state);
    }
}
