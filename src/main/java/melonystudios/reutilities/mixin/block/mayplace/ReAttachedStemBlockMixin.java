package melonystudios.reutilities.mixin.block.mayplace;

import melonystudios.reutilities.util.tag.ReBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttachedStemBlock.class)
public class ReAttachedStemBlockMixin extends Block {
    public ReAttachedStemBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    protected void tagPlaceOn(BlockState state, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(state.is(ReBlockTags.CROPS_MAY_PLACE_ON));
    }
}
