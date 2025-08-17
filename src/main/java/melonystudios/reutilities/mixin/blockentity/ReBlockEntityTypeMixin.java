package melonystudios.reutilities.mixin.blockentity;

import melonystudios.reutilities.util.Reconstants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(BlockEntityType.class)
public abstract class ReBlockEntityTypeMixin {
    @Shadow
    public abstract Set<Block> getValidBlocks();

    // neoforge event doesn't seem to work so jank code it is ~isa 16-8-25
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    public void isValid(BlockState state, CallbackInfoReturnable<Boolean> callback) {
        Set<Block> validBlocks = this.getValidBlocks();
        boolean hasSigns = validBlocks.contains(Blocks.OAK_SIGN) && validBlocks.size() == 1 && Reconstants.SIGNS.contains(state.getBlock());
        boolean hasHangingSigns = validBlocks.contains(Blocks.OAK_HANGING_SIGN) && validBlocks.size() == 1 && Reconstants.HANGING_SIGNS.contains(state.getBlock());
        if (hasSigns || hasHangingSigns) callback.setReturnValue(true);
    }
}
