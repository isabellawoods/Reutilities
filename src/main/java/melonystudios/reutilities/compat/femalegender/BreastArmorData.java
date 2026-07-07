package melonystudios.reutilities.compat.femalegender;

/// Stand-in for {@link com.wildfire.render.armor.SimpleGenderArmor SimpleGenderArmor} from *Female Gender Mod*, since most of my mods wouldn't have it installed during development.
///
/// Parameter descriptions were taken directly from {@link com.wildfire.api.IGenderArmor IGenderArmor}.
/// @param physicsResistance The percent of physical resistance this armor provides to the wearer's breasts when calculating the corresponding physics.
/// `0` has no resistance and full physics, whereas `1` has total resistance and no physics.
/// @param tightness Represents how "tight" this armor is. Tightness "compresses" the breasts against the wearer, causing the breasts to appear up to **15% smaller**.
/// `0` has no tightness and no size reduction, whereas `1` has full tightness and a reduction of 15%.
/// @param alwaysHidesBreasts Hides the wearer's breasts regardless of the **"Show Breasts in Armor"** option.
/// @param armorStandsCopySettings Determines whether armor stands should copy the breast settings of the player equipping this chestplate onto it.
public record BreastArmorData(float physicsResistance, float tightness, boolean alwaysHidesBreasts, Boolean armorStandsCopySettings) {
}
