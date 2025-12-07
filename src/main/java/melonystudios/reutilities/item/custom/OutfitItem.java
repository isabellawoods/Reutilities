package melonystudios.reutilities.item.custom;

import melonystudios.reutilities.util.ReArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class OutfitItem extends ArmorItem {
    public OutfitItem(Type slot, Properties properties) {
        super(ReArmorMaterials.EMPTY, slot, properties);
    }

    public OutfitItem(Holder<ArmorMaterial> material, Type slot, Properties properties) {
        super(material, slot, properties);
    }
}
