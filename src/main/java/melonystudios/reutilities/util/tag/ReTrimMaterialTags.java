package melonystudios.reutilities.util.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.armortrim.TrimMaterial;

public class ReTrimMaterialTags {
    /// Trim materials in this tag glow at light level `15` when displayed.
    public static final TagKey<TrimMaterial> EMISSIVE_LIGHTING = common("emissive_lighting");

    /// Creates a new trim material tag with a specified name.
    /// @param name The tag's name, under the **Common** (`c`) namespace.
    public static TagKey<TrimMaterial> common(String name) {
        return TagKey.create(Registries.TRIM_MATERIAL, Reutilities.common(name));
    }
}
