package melonystudios.reutilities.compat.femalegender;

import com.wildfire.api.IGenderArmor;
import com.wildfire.api.WildfireAPI;
import melonystudios.reutilities.util.ReCommonConstants;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class BreastArmorCapabilities {
    public static void register(RegisterCapabilitiesEvent event) {
        for (Item item : ReCommonConstants.BREAST_ARMOR_CAPABILITIES.keySet()) {
            event.registerItem(WildfireAPI.GENDER_ARMOR_CAPABILITY, (stack, context) -> toGenderArmor(ReCommonConstants.BREAST_ARMOR_CAPABILITIES.get(item)), item);
        }
    }

    /// Transforms a `BreastArmorData` instance into an {@link IGenderArmor}.
    /// @param armorData The *Reutilities* armor data.
    public static IGenderArmor toGenderArmor(BreastArmorData armorData) {
        return new IGenderArmor() {
            @Override
            public float tightness() {
                return armorData.tightness();
            }

            @Override
            public float physicsResistance() {
                return armorData.physicsResistance();
            }

            @Override
            public boolean alwaysHidesBreasts() {
                return armorData.alwaysHidesBreasts();
            }

            @Override
            public boolean armorStandsCopySettings() {
                return armorData.armorStandsCopySettings() != null ? armorData.armorStandsCopySettings() : IGenderArmor.super.armorStandsCopySettings();
            }
        };
    }
}
