package melonystudios.reutilities.blockentity.custom;

import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ReSignBlockEntity extends SignBlockEntity {
    public ReSignBlockEntity(BlockPos pos, BlockState state) {
        super(ReBlockEntities.SIGN.get(), pos, state);
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return Reconstants.SIGNS.contains(state.getBlock());
    }

    @Override
    @NotNull
    public BlockEntityType<?> getType() {
        return ReBlockEntities.SIGN.get();
    }
}
