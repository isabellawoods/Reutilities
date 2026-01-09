package melonystudios.reutilities.option;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ReClientOptions {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue RENDER_OUTFITS = BUILDER.comment("Whether to render outfits on your player model and first-person hand.", "Disabling this makes the rendering fall back to vanilla.").translation("config.reutilities.render_outfits").define("rendering.outfits", true);
    public static final ModConfigSpec.BooleanValue RENDER_OUTFIT_ON_HAND = BUILDER.comment("Whether to render outfits in the \"chest\" slot in your first-person hand.").translation("config.reutilities.render_outfit_on_hand").define("rendering.outfitOnHand", true);
    public static final ModConfigSpec.BooleanValue RENDER_ARMOR_ON_HAND = BUILDER.comment("Whether to render chestplates in your first-person hand.").translation("config.reutilities.render_armor_on_hand").define("rendering.armorOnHand", true);
    public static final ModConfigSpec.BooleanValue COLOR_EMISSIVE_OUTFIT_PARTS = BUILDER.comment("Whether emissive parts of outfits should be colored.").translation("config.reutilities.color_emissive_outfit_parts").define("rendering.colorEmissiveOutfitParts", false);
    public static final ModConfigSpec.BooleanValue PANORAMIC_SCREENSHOTS = BUILDER.comment("Whether panoramic screenshots can be taken using Ctrl + [Screenshot Key].", "If Iris Shaders is loaded, panoramas will not be taken correctly.").translation("config.reutilities.panoramic_screenshots").define("miscellaneous.panoramicScreenshots", false);
    public static final ModConfigSpec.IntValue PANORAMIC_SCREENSHOT_SIZE = BUILDER.comment("How big each screenshot should be. Defaults to 1024px.", "Increasing the resolution will lead to images with better quality, but at the cost of performance and storage space.").translation("config.reutilities.panoramic_screenshot_size").defineInRange("miscellaneous.panoramicScreenshotSize", 1024, 32, 8192);
    public static final ModConfigSpec.ConfigValue<String> PANORAMA_SAVE_FOLDER = BUILDER.comment("Which folder panoramic screenshots should be saved to. Defaults to \"panorama\".").translation("config.reutilities.panorama_save_folder").define("miscellaneous.panoramaSaveFolder", "panorama");
    public static final ModConfigSpec.DoubleValue MAXIMUM_BUST_SIZE = BUILDER.comment("How big the breasts added by Female Gender Mod can be.", "Values above 1 cause the rendering to increasingly fail.").translation("config.reutilities.maximum_bust_size").gameRestart().defineInRange("compat.maximumBustSize", 0.8D, 0, 1.5D);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
