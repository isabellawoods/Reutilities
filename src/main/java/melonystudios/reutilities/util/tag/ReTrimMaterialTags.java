package melonystudios.reutilities.util.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.armortrim.TrimMaterial;

public class ReTrimMaterialTags {
    public static final TagKey<TrimMaterial> EMISSIVE_LIGHTING = common("emissive_lighting");

    public static TagKey<TrimMaterial> common(String name) {
        return TagKey.create(Registries.TRIM_MATERIAL, Reutilities.common(name));
    }
}
