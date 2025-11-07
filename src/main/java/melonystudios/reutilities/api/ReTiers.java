package melonystudios.reutilities.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/// A default implementation of vanilla's {@link Tier} interface.
/// @param incorrectBlocksForDrops A {@linkplain net.minecraft.tags.BlockTags block tag} defining which blocks cannot be broken by this harvest level.
/// @param durability The base durability value of this item tier.
/// @param harvestSpeed How fast this item tier can break blocks, influenced by whether the block can be broken using the tool.
/// @param attackDamage How much attack damage this item tier does. This is summed up with the item's attack damage to get the final damage.
/// @param enchantmentValue How enchantable this item is. Higher values mean better and/or more enchantments.
/// @param repairIngredient An {@linkplain Ingredient ingredient} supplier for getting this tier's repair materials.
public record ReTiers(TagKey<Block> incorrectBlocksForDrops, int durability, float harvestSpeed, float attackDamage, int enchantmentValue, Supplier<Ingredient> repairIngredient) implements Tier {
    @Override
    public int getUses() {
        return this.durability;
    }

    @Override
    public float getSpeed() {
        return this.harvestSpeed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    @NotNull
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    @NotNull
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
