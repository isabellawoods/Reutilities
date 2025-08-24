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

    public static final ModConfigSpec SPEC = BUILDER.build();
}
