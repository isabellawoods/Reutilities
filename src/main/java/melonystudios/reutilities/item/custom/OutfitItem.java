package melonystudios.reutilities.item.custom;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.component.custom.ComponentOutfit;
import melonystudios.reutilities.util.ReArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class OutfitItem extends ArmorItem {
    public OutfitItem(Type slot, Properties properties) {
        super(ReArmorMaterials.EMPTY, slot, properties);
    }

    public OutfitItem(Holder<ArmorMaterial> material, Type slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (ReAPI.shouldDisplay(stack, Reutilities.reutilities("outfit"))) {
            ComponentOutfit outfit = stack.get(ReDataComponents.OUTFIT);
            if (outfit != null) outfit.addToTooltip(context, tooltip::add, flag);
        }
    }
}
