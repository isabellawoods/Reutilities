package melonystudios.reutilities;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ReConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue RENDER_OUTFITS = BUILDER.comment("Whether to render outfits on your player model and first-person hand.", "Disabling this makes the rendering fall back to vanilla.").translation("config.reutilities.render_outfits").define("rendering.outfits", true);
    public static final ModConfigSpec.BooleanValue RENDER_OUTFIT_ON_HAND = BUILDER.comment("Whether to render outfits in the \"chest\" slot in your first-person hand.").translation("config.reutilities.render_outfit_on_hand").define("rendering.outfitOnHand", true);
    public static final ModConfigSpec.BooleanValue RENDER_ARMOR_ON_HAND = BUILDER.comment("Whether to render chestplates in your first-person hand.").translation("config.reutilities.render_armor_on_hand").define("rendering.armorOnHand", true);
    public static final ModConfigSpec.BooleanValue LIGHT_EMITTING_EMISSIVES = BUILDER.comment("Whether light-emitting blocks should be emissive based on their light level.").translation("config.reutilities.light_emitting_emissives").define("rendering.lightEmittingEmissives", true);
    public static final ModConfigSpec.BooleanValue SHOW_COMPONENTS_WITH_ALT = BUILDER.comment("Whether to display an item's data components when holding Alt.").translation("config.reutilities.show_components_with_alt").define("item.showComponentsWithAlt", false);
    public static final ModConfigSpec.BooleanValue LINE_BREAKS_ON_COMPONENTS = BUILDER.comment("Whether component display on tooltips should have line breaks.").translation("config.reutilities.line_breaks_on_components").define("item.lineBreaksOnComponents", true);
    public static final ModConfigSpec.BooleanValue PANORAMIC_SCREENSHOTS = BUILDER.comment("Whether panoramic screenshots can be taken using Ctrl + [Screenshot Key].", "If Iris Shaders is loaded, panoramas will not be taken correctly.").translation("config.reutilities.panoramic_screenshots").define("miscellaneous.panoramicScreenshots", false);
    public static final ModConfigSpec.IntValue PANORAMIC_SCREENSHOT_SIZE = BUILDER.comment("How big each screenshot should be. Defaults to 1024px.", "Increasing the resolution will lead to images with better quality, but at the cost of performance and storage space.").translation("config.reutilities.panoramic_screenshot_size").defineInRange("miscellaneous.panoramicScreenshotSize", 1024, 32, 8192);
    public static final ModConfigSpec.ConfigValue<String> PANORAMA_SAVE_FOLDER = BUILDER.comment("Which folder panoramic screenshots should be saved to. Defaults to \"panorama\".").translation("config.reutilities.panorama_save_folder").define("miscellaneous.panoramaSaveFolder", "panorama");

    public static final ModConfigSpec SPEC = BUILDER.build();
}
