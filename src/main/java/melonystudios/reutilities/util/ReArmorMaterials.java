package melonystudios.reutilities.util;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.ReAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Reutilities.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EMPTY = MATERIALS.register("empty", () -> new ArmorMaterial(ReAPI.defenseMap(0, 0, 0, 0),
            10, SoundEvents.ARMOR_EQUIP_LEATHER, Ingredient::of, ReAPI.defaultLayers(Reutilities.reutilities("empty")), 0, 0));
}
