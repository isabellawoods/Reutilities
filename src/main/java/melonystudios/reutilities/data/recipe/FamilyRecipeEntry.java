package melonystudios.reutilities.data.recipe;

import com.mojang.datafixers.util.Pair;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface FamilyRecipeEntry {
    /// Gets the ingredient for the recipe provider, while also providing a workaround for properly using the {@link FamilyRecipeProvider#has(ItemLike) has()} methods,
    /// which only support items and item tags.
    Pair<ItemLike, Ingredient> entryGetter();

    /// Represents a single item entry in the {@link FamilyRecipeProvider#RECIPES RECIPES} map.
    abstract class ItemEntry implements FamilyRecipeEntry {
        @Override
        public Pair<ItemLike, Ingredient> entryGetter() {
            return new Pair<>(this.entry().getFirst(), Ingredient.of(this.entry().getSecond()));
        }

        /// Adds an item entry to the `RECIPES` map.
        public abstract Pair<ItemLike, Item> entry();
    }

    /// Represents a single item tag entry in the {@link FamilyRecipeProvider#RECIPES RECIPES} map.
    abstract class TagEntry implements FamilyRecipeEntry {
        @Override
        public Pair<ItemLike, Ingredient> entryGetter() {
            return new Pair<>(this.entry().getFirst(), Ingredient.of(this.entry().getSecond()));
        }

        /// Adds an item tag entry to the `RECIPES` map.
        public abstract Pair<ItemLike, TagKey<Item>> entry();
    }
}
