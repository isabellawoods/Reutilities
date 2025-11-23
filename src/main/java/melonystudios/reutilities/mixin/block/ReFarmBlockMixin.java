package melonystudios.reutilities.mixin.block;

import melonystudios.reutilities.util.tag.ReBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmBlock.class)
public class ReFarmBlockMixin extends Block {
    public ReFarmBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    protected void canSurvive(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
        BlockState aboveState = world.getBlockState(pos.above());
        callback.setReturnValue(!aboveState.isSolid() || aboveState.is(ReBlockTags.FARMLAND_TRANSPARENT));
    }
}
