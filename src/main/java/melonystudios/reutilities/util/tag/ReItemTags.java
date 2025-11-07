package melonystudios.reutilities.util.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ReItemTags {
    /// Items in this tag glow at light level 15 when displayed in any context where darkness is a possibility.
    public static final TagKey<Item> EMISSIVE_LIGHTING = common("emissive_lighting");
    /// Items in this tag are held using the "crossbow charge" animation. This does have some issues when eating since it plays the charging animation.
    public static final TagKey<Item> DUAL_WIELDED = common("dual_wielded");
    public static final TagKey<Item> LOGOS = common("logos");
    public static final TagKey<Item> FLINT = common("flint");
    public static final TagKey<Item> NETHER_STARS = common("nether_stars");

    public static final TagKey<Item> SUGAR_DUSTS = common("dusts/sugar");
    public static final TagKey<Item> GUNPOWDER_DUSTS = common("dusts/gunpowder");
    public static final TagKey<Item> BLAZE_DUSTS = common("dusts/blaze");

    /// Creates a new item tag with a specified name.
    /// @param name The tag's name, under the **Common** (`c`) namespace.
    public static TagKey<Item> common(String name) {
        return TagKey.create(Registries.ITEM, Reutilities.common(name));
    }
}
