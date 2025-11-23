package melonystudios.reutilities.data.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ReRecipeProvider extends RecipeProvider {
    public ReRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    /// Creates a shapeless recipe for an ingot.
    /// @param output The {@linkplain RecipeOutput recipe output}, used for saving the recipe
    /// @param input The item tag used to craft this item.
    /// @param outputItem The output (or result) item for this recipe.
    /// @param group *(optional)* The group to put this recipe in.
    /// @param condition The condition of the material, used for the advancement condition.
    public static void toIngot(RecipeOutput output, TagKey<Item> input, ItemLike outputItem, @Nullable String group, String condition) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItem).requires(input).requires(input).requires(input).requires(input).requires(input)
                .requires(input).requires(input).requires(input).requires(input).unlockedBy(condition, has(input))
                .group(group).save(output);
    }

    /// Creates a shapeless recipe for an ingot.
    /// @param output The {@linkplain RecipeOutput recipe output}, used for saving the recipe
    /// @param input The item used to craft this item.
    /// @param outputItem The output (or result) item for this recipe.
    /// @param group *(optional)* The group to put this recipe in.
    /// @param condition The condition of the material, used for the advancement condition.
    public static void toIngot(RecipeOutput output, ItemLike input, ItemLike outputItem, @Nullable String group, String condition) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItem).requires(input).requires(input).requires(input).requires(input).requires(input)
                .requires(input).requires(input).requires(input).requires(input).unlockedBy(condition, has(input))
                .group(group).save(output);
    }

    /// Creates a shapeless recipe for a block.
    /// @param output The {@linkplain RecipeOutput recipe output}, used for saving the recipe
    /// @param input The item tag used to craft this item.
    /// @param outputItem The output (or result) item for this recipe.
    /// @param group *(optional)* The group to put this recipe in.
    /// @param condition The condition of the material, used for the advancement condition.
    public static void toBlock(RecipeOutput output, TagKey<Item> input, ItemLike outputItem, @Nullable String group, String condition) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, outputItem).requires(input).requires(input).requires(input).requires(input).requires(input)
                .requires(input).requires(input).requires(input).requires(input).unlockedBy(condition, has(input))
                .group(group).save(output);
    }

    /// Creates a shapeless recipe for a block.
    /// @param output The default {@linkplain RecipeOutput recipe output}, used for saving the recipe.
    /// @param input The item used to craft this item.
    /// @param outputItem The output or result item from this recipe.
    public static void toBlock(RecipeOutput output, ItemLike input, ItemLike outputItem) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, outputItem).requires(input).requires(input).requires(input).requires(input).requires(input)
                .requires(input).requires(input).requires(input).requires(input).unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(input.asItem()).getPath(), has(input))
                .save(output, BuiltInRegistries.ITEM.getKey(outputItem.asItem()));
    }

    /// Creates a shapeless recipe for making blocks into ingots, or ingots into nuggets.
    /// @param output The default {@linkplain RecipeOutput recipe output}, used for saving the recipe;
    /// @param input The item tag used to craft this item.
    /// @param count The amount of items in the result.
    /// @param group *(optional)* The group to put this recipe in.
    /// @param condition The condition of the material, used for the advancement condition.
    public static void fromBlock(RecipeOutput output, TagKey<Item> input, ItemLike outputItem, int count, @Nullable String group, String condition) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItem, count).requires(input)
                .unlockedBy(condition, has(input)).group(group).save(output);
    }

    /// Creates a shapeless recipe for making blocks into ingots, or ingots into nuggets.
    /// @param output The default {@linkplain RecipeOutput recipe output}, used for saving the recipe;
    /// @param input The item tag used to craft this item.
    /// @param count The amount of items in the result.
    /// @param group *(optional)* The group to put this recipe in.
    public static void fromBlock(RecipeOutput output, ItemLike input, ItemLike outputItem, int count, @Nullable String group) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, input, count).requires(outputItem)
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(outputItem.asItem()).getPath(), has(outputItem))
                .group(group).save(output);
    }
}
