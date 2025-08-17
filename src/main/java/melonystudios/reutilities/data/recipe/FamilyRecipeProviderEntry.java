package melonystudios.reutilities.data.recipe;

import net.minecraft.world.level.ItemLike;

/// Represents a single provider in a {@linkplain FamilyRecipeProvider family recipe provider}.
/// A provider is a "factory" that makes the recipes for a specified item type, like "`planks`" or "`wooden_slab`".
public interface FamilyRecipeProviderEntry {
    /// Makes a recipe for an item.
    /// @param item The item being generated.
    /// @param entry An ingredient, be it an item or item tag, used in some recipes when the default `material` doesn't work.
    void makeRecipe(ItemLike item, FamilyRecipeEntry entry);
}
