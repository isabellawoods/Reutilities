package melonystudios.reutilities.mixin.renderer;

import net.minecraft.client.model.PlayerModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerModel.class)
public interface PlayerSlimAccessor {
    @Final
    @Mutable
    @Accessor("slim")
    boolean reutilities$slimArms();
}
