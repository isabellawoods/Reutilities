package melonystudios.reutilities.util.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ReItemTags {
    public static final TagKey<Item> EMISSIVE_LIGHTING = common("emissive_lighting");
    public static final TagKey<Item> DUAL_WIELDED = common("dual_wielded");
    public static final TagKey<Item> CHAINS = minecraft("chains");

    public static TagKey<Item> common(String name) {
        return ItemTags.create(Reutilities.common(name));
    }

    public static TagKey<Item> minecraft(String name) {
        return ItemTags.create(ResourceLocation.withDefaultNamespace(name));
    }
}
