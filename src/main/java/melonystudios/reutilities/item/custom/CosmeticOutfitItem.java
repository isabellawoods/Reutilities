package melonystudios.reutilities.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

public class CosmeticOutfitItem extends OutfitItem {
    public CosmeticOutfitItem(Holder<ArmorMaterial> material, Type slot, Properties properties) {
        super(material, slot, properties.durability(slot.getDurability(20)));
    }
}
