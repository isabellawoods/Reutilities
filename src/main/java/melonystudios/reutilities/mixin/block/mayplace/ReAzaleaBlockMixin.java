package melonystudios.reutilities.mixin.block.mayplace;

import melonystudios.reutilities.util.tag.ReBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AzaleaBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AzaleaBlock.class)
public class ReAzaleaBlockMixin extends Block {
    public ReAzaleaBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    protected void tagPlaceOn(BlockState state, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(state.is(ReBlockTags.AZALEA_MAY_PLACE_ON));
    }
}
