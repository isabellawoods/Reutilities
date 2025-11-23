package melonystudios.reutilities.util.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class ReFluidTags {
    public static final TagKey<Fluid> HYDRATES_WATER_BASED_FARMLAND = reutilities("hydrates_water_based_farmland");
    public static final TagKey<Fluid> HYDRATES_LAVA_BASED_FARMLAND = reutilities("hydrates_lava_based_farmland");

    /// Creates a new fluid tag with a specified name.
    /// @param name The tag's name, under ***Reutilities***' namespace.
    public static TagKey<Fluid> reutilities(String name) {
        return TagKey.create(Registries.FLUID, Reutilities.reutilities(name));
    }
}
