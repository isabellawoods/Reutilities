package melonystudios.reutilities.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.util.ReClientConstants;
import net.minecraft.client.Screenshot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Screenshot.class)
public class ReScreenshotMixin {
    @ModifyArg(method = "_grab", at = @At(value = "INVOKE", target = "Ljava/io/File;<init>(Ljava/io/File;Ljava/lang/String;)V", ordinal = 0))
    private static String switchOutputFolder(String folderName, @Local(argsOnly = true) @Nullable String screenshotName) {
        if (screenshotName != null && screenshotName.startsWith("panorama_")) return ReConfigs.PANORAMA_SAVE_FOLDER.get() + "/" + ReClientConstants.CURRENT_PANORAMA_OUTPUT_FOLDER;
        return folderName;
    }
}
