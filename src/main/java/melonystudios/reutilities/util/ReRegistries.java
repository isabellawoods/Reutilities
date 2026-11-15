package melonystudios.reutilities.util;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.cape.Recape;
import melonystudios.reutilities.api.outfit.OutfitDefinition;
import melonystudios.reutilities.api.picker.ColorPicker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ReRegistries {
    public static final ResourceKey<Registry<OutfitDefinition>> OUTFIT_DEFINITION = ResourceKey.createRegistryKey(Reutilities.reutilities("outfit_definition"));
    public static final ResourceKey<Registry<Recape>> CAPE = ResourceKey.createRegistryKey(Reutilities.reutilities("cape"));
    public static final ResourceKey<Registry<ColorPicker>> COLOR_PICKER = ResourceKey.createRegistryKey(Reutilities.reutilities("color_picker"));
}
