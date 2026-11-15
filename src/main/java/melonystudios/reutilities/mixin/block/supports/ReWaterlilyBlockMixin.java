package melonystudios.reutilities.mixin.block.supports;

import melonystudios.reutilities.tag.ReBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterlilyBlock.class)
public class ReWaterlilyBlockMixin extends Block {
    public ReWaterlilyBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    protected void tagPlaceOn(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
        FluidState aboveState = level.getFluidState(pos.above());
        callback.setReturnValue((level.getFluidState(pos).getType() == Fluids.WATER || state.is(ReBlockTags.SUPPORTS_LILY_PAD)) && aboveState.getType() == Fluids.EMPTY);
    }
}
