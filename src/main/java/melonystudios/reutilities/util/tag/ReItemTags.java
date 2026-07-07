package melonystudios.reutilities.util.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus;

public class ReItemTags {
    // Reutilities' tags
    public static final TagKey<Item> COBBLESTONE_CRAFTING_MATERIALS = reutilities("cobblestone_crafting_materials");
    public static final TagKey<Item> STONE_CRAFTING_MATERIALS = reutilities("stone_crafting_materials");

    // Common tags
    /// Items in this tag glow at light level 15 when displayed in any context where darkness is a possibility.
    public static final TagKey<Item> EMISSIVE_LIGHTING = common("emissive_lighting");
    /// Items in this tag are held using the "crossbow charge" animation. This does have some issues when eating since it plays the charging animation.
    public static final TagKey<Item> DUAL_WIELDED = common("dual_wielded");
    /// Items in this tag hide the wearer's cape when worn in their chest slot.
    public static final TagKey<Item> HIDES_CAPE_WHEN_WORN = common("hides_cape_when_worn");
    /// Contains all logos and icons for mods and creative tabs.
    public static final TagKey<Item> LOGOS = common("logos");
    public static final TagKey<Item> FLINT = common("flint");
    /// @deprecated Use {@link net.neoforged.neoforge.common.Tags.Items#NETHER_STARS #c:nether_stars} (from *NeoForge*) instead.
    @Deprecated(since = "1.5.1", forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.6.0")
    public static final TagKey<Item> NETHER_STARS = common("nether_stars");

    public static final TagKey<Item> SUGAR_DUSTS = common("dusts/sugar");
    public static final TagKey<Item> GUNPOWDER_DUSTS = common("dusts/gunpowder");
    public static final TagKey<Item> BLAZE_DUSTS = common("dusts/blaze");

    /// Creates a new item tag with a specified name.
    /// @param name The tag's name, under **Reutilities**' namespace.
    public static TagKey<Item> reutilities(String name) {
        return TagKey.create(Registries.ITEM, Reutilities.reutilities(name));
    }

    /// Creates a new item tag with a specified name.
    /// @param name The tag's name, under the **Common** (`c`) namespace.
    public static TagKey<Item> common(String name) {
        return TagKey.create(Registries.ITEM, Reutilities.common(name));
    }
}
