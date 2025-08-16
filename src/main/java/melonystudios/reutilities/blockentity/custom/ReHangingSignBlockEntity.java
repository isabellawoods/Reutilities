package melonystudios.reutilities.blockentity.custom;

import melonystudios.reutilities.blockentity.ReBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ReHangingSignBlockEntity extends HangingSignBlockEntity {
    public ReHangingSignBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    @NotNull
    public BlockEntityType<?> getType() {
        return ReBlockEntities.HANGING_SIGN.get();
    }
}
