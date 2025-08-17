package melonystudios.reutilities;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ReConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue RENDER_OUTFITS = BUILDER.comment("Whether to render outfits on your player model and first-person hand.", "Disabling this makes the rendering fall back to vanilla.").define("rendering.outfits", true);
    public static final ModConfigSpec.BooleanValue RENDER_ARMOR_ON_HAND = BUILDER.comment("Whether to render chestplates in your first-person hand.").define("rendering.armorOnHand", true);
    public static final ModConfigSpec.BooleanValue RENDER_OUTFIT_ON_HAND = BUILDER.comment("Whether to render outfits in the \"chest\" slot in your first-person hand.").define("rendering.outfitOnHand", true);
    public static final ModConfigSpec.BooleanValue LIGHT_EMITTING_EMISSIVES = BUILDER.comment("Whether light-emitting blocks should be emissive based on their light level.").define("rendering.lightEmittingEmissives", true);
    public static final ModConfigSpec.BooleanValue SHOW_TAGS_WITH_ALT = BUILDER.comment("Whether to display an item's NBT tags when holding Alt.").define("item.showTagsWithAlt", false);
    public static final ModConfigSpec.BooleanValue LINE_BREAKS_ON_TAGS = BUILDER.comment("Whether NBT display on tooltips should have line breaks.").define("item.lineBreaksOnTags", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
