package melonystudios.reutilities.mixin.compat.femalegender;

import com.wildfire.main.config.ClientConfiguration;
import com.wildfire.main.config.FloatConfigKey;
import melonystudios.reutilities.option.ReClientOptions;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = ClientConfiguration.class, remap = false)
public class ReClientConfigurationMixin {
    @Shadow
    @Mutable
    @Final
    public static FloatConfigKey BUST_SIZE;

    @Inject(method = "<init>", at = @At("TAIL"))
    private static void increaseMaximumBustSize(String fileName, CallbackInfo callback) {
        BUST_SIZE = new FloatConfigKey("bust_size", 0.6F, 0, ReClientOptions.MAXIMUM_BUST_SIZE.get().floatValue());
    }
}
