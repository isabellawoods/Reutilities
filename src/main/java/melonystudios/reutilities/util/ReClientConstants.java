package melonystudios.reutilities.util;

import melonystudios.reutilities.Reutilities;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/// Contains various constants, maps and methods used across *Reutilities* in a clientside context.
@OnlyIn(Dist.CLIENT)
public class ReClientConstants {
    public static final int EMISSIVE_LIGHT_VALUE = LightTexture.pack(15, 15);
    public static String CURRENT_PANORAMA_OUTPUT_FOLDER = "unset_please_report_to_" + Reutilities.MOD_ID;

    /// Custom {@link net.minecraft.client.renderer.entity.LivingEntityRenderer#getOverlayCoords getOverlayCoords()} method to remove the red tint from taking damage or dying.
    /// @param u The textures U value, usually set to `0`.
    public static int getOverlayCoordinates(float u) {
        return OverlayTexture.pack(OverlayTexture.u(u), OverlayTexture.v(false));
    }
}
