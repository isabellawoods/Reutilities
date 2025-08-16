package melonystudios.reutilities.blockentity.custom;

import melonystudios.reutilities.blockentity.ReBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ReSignBlockEntity extends SignBlockEntity {
    public ReSignBlockEntity(BlockPos pos, BlockState state) {
        super(ReBlockEntities.SIGN.get(), pos, state);
    }
}
