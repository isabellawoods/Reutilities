package melonystudios.reutilities.util;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ReRegistries {
    public static final ResourceKey<Registry<OutfitDefinition>> OUTFIT_DEFINITION = ResourceKey.createRegistryKey(Reutilities.reutilities("outfit_definition"));
}
