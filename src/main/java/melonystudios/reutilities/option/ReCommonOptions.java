package melonystudios.reutilities.option;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ReCommonOptions {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LIGHT_EMITTING_EMISSIVES = BUILDER.comment("Whether light-emitting blocks should be emissive based on their light level.").translation("config.reutilities.light_emitting_emissives").define("rendering.lightEmittingEmissives", true);
    public static final ModConfigSpec.BooleanValue SHOW_COMPONENTS_WITH_ALT = BUILDER.comment("Whether to display an item's data components when holding Alt.").translation("config.reutilities.show_components_with_alt").define("item.showComponentsWithAlt", false);
    public static final ModConfigSpec.BooleanValue LINE_BREAKS_ON_COMPONENTS = BUILDER.comment("Whether component display on tooltips should have line breaks.").translation("config.reutilities.line_breaks_on_components").define("item.lineBreaksOnComponents", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
