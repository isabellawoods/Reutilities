package melonystudios.reutilities.data.recipe;

import com.mojang.datafixers.util.Pair;
import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;

import java.util.*;

/// Helper data generator class for generating whole families of recipes.
public class RecipeFamilyProvider {
    /// Map of item types (strings) to {@linkplain RecipeFamilyProviderEntry recipe family provider entries}, used to create all the base recipes.
    private static final Map<String, RecipeFamilyProviderEntry> PROVIDERS = new LinkedHashMap<>();
    /// Map of all items that should be generated, paired with their respective types.
    private static final Map<String, RecipeFamilyEntry> RECIPES = new LinkedHashMap<>();
    private final RecipeOutput output;
    private final ItemLike material;
    private final TagKey<Item> rods;

    /// Helper data generator class for generating whole families of recipes.
    /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
    /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
    public RecipeFamilyProvider(RecipeOutput output, ItemLike material) {
        this(output, material, Tags.Items.RODS_WOODEN);
    }

    /// Helper data generator class for generating whole families of recipes.
    /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
    /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
    /// @param rods An item tag that's used as the default "stick" item for recipes, used for fences and fence gates.
    public RecipeFamilyProvider(RecipeOutput output, ItemLike material, TagKey<Item> rods) {
        this.output = output;
        this.material = material;
        this.rods = rods;
        this.addDefaultProviders();
    }

    /// Makes a new instance of the {@linkplain Builder recipe builder}.
    /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
    /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
    public static Builder builder(RecipeOutput output, ItemLike material) {
        return builder(output, material, Tags.Items.RODS_WOODEN);
    }

    /// Makes a new instance of the {@linkplain Builder recipe builder}.
    /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
    /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
    /// @param rods An item tag that's used as the default "stick" item for recipes, used for fences and fence gates.
    public static Builder builder(RecipeOutput output, ItemLike material, TagKey<Item> rods) {
        return new Builder(output, material, rods);
    }

    /// Adds all default {@linkplain RecipeFamilyProviderEntry recipe family provider entries} to the providers map.
    /// New providers can be added using the {@link #addProviders()} method below.
    private void addDefaultProviders() {
        // Common blocks
        PROVIDERS.put("stairs", (item, ingredient, stonecuttingRecipes) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 6).define('#', this.material)
                    .pattern("#  ").pattern("## ").pattern("###").unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output);
            if (stonecuttingRecipes) SingleItemRecipeBuilder.stonecutting(Ingredient.of(this.material), RecipeCategory.BUILDING_BLOCKS, item)
                    .unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output, saveLocation(item, "stonecutting"));
        });
        PROVIDERS.put("slab", (item, ingredient, stonecuttingRecipes) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 6).define('#', this.material)
                    .pattern("###").unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output);
            if (stonecuttingRecipes) SingleItemRecipeBuilder.stonecutting(Ingredient.of(this.material), RecipeCategory.BUILDING_BLOCKS, item, 2)
                    .unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output, saveLocation(item, "stonecutting"));
        });
        PROVIDERS.put("wall", (item, ingredient, stonecuttingRecipes) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item, 6).define('#', this.material)
                    .pattern("###").pattern("###").unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output);
            if (stonecuttingRecipes) SingleItemRecipeBuilder.stonecutting(Ingredient.of(this.material), RecipeCategory.BUILDING_BLOCKS, item)
                    .unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output, saveLocation(item, "stonecutting"));
        });
        PROVIDERS.put("pressure_plate", (item, ingredient, stonecuttingRecipes) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item).define('#', this.material)
                    .pattern("##").unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output);
            if (stonecuttingRecipes) SingleItemRecipeBuilder.stonecutting(Ingredient.of(this.material), RecipeCategory.REDSTONE, item)
                    .unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output, saveLocation(item, "stonecutting"));
        });
        PROVIDERS.put("button", (item, ingredient, stonecuttingRecipes) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, item).requires(this.material)
                    .unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output);
            if (stonecuttingRecipes) SingleItemRecipeBuilder.stonecutting(Ingredient.of(this.material), RecipeCategory.REDSTONE, item)
                    .unlockedBy(hasItem(this.material), has(this.material))
                    .save(this.output, saveLocation(item, "stonecutting"));
        });

        // Wood blocks
        PROVIDERS.put("sapling", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item).define('#', ingredient.entryGetter().getSecond()).define('S', this.rods)
                        .pattern("#").pattern("S").unlockedBy("has_leaves", has(ingredient))
                        .group("sapling").save(this.output)
        );
        PROVIDERS.put("wood", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 3).define('#', ingredient.entryGetter().getSecond())
                        .pattern("##").pattern("##").unlockedBy("has_logs", has(ingredient))
                        .group("bark").save(this.output)
        );
        PROVIDERS.put("stripped_wood", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 3).define('#', ingredient.entryGetter().getSecond())
                        .pattern("##").pattern("##").unlockedBy("has_logs", has(ingredient))
                        .group("bark").save(this.output)
        );
        PROVIDERS.put("planks", (item, ingredient, stonecuttingRecipes) ->
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, this.material, 4).requires(ingredient.entryGetter().getSecond())
                        .unlockedBy("has_logs", has(ingredient))
                        .group("planks").save(this.output)
        );
        PROVIDERS.put("wooden_stairs", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 6).define('#', this.material)
                        .pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_planks", has(this.material))
                        .group("wooden_stairs").save(this.output)
        );
        PROVIDERS.put("wooden_slab", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 6).define('#', this.material)
                        .pattern("###").unlockedBy("has_planks", has(this.material))
                        .group("wooden_slab").save(this.output)
        );
        PROVIDERS.put("wooden_fence", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item, 3).define('#', this.material).define('S', this.rods)
                        .pattern("#S#").pattern("#S#").unlockedBy("has_planks", has(this.material))
                        .group("wooden_fence").save(this.output)
        );
        PROVIDERS.put("wooden_fence_gate", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item).define('#', this.material).define('S', this.rods)
                        .pattern("S#S").pattern("S#S").unlockedBy("has_planks", has(this.material))
                        .group("wooden_fence_gate").save(this.output)
        );
        PROVIDERS.put("wooden_door", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item, 3).define('#', this.material)
                        .pattern("##").pattern("##").pattern("##").unlockedBy("has_planks", has(this.material))
                        .group("wooden_door").save(this.output)
        );
        PROVIDERS.put("wooden_trapdoor", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item, 2).define('#', this.material)
                        .pattern("###").pattern("###").unlockedBy("has_planks", has(this.material))
                        .group("wooden_trapdoor").save(this.output)
        );
        PROVIDERS.put("wooden_pressure_plate", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item).define('#', this.material)
                        .pattern("##").unlockedBy("has_planks", has(this.material))
                        .group("wooden_pressure_plate").save(this.output)
        );
        PROVIDERS.put("wooden_button", (item, ingredient, stonecuttingRecipes) ->
                ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, item).requires(this.material)
                        .unlockedBy("has_planks", has(this.material))
                        .group("wooden_button").save(this.output)
        );
        PROVIDERS.put("sign", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item, 3).define('#', this.material).define('S', this.rods)
                        .pattern("###").pattern("###").pattern(" S ").unlockedBy("has_planks", has(this.material))
                        .group("wooden_sign").save(this.output)
        );
        PROVIDERS.put("hanging_sign", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item, 6).define('#', ingredient.entryGetter().getSecond()).define('S', Tags.Items.CHAINS)
                        .pattern("S S").pattern("###").pattern("###").unlockedBy("has_stripped_logs", has(ingredient))
                        .group("hanging_sign").save(this.output)
        );
        PROVIDERS.put("crafting_table", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item).define('#', this.material)
                        .pattern("##").pattern("##").unlockedBy("has_planks", has(this.material))
                        .group("crafting_tables").save(this.output)
        );
        PROVIDERS.put("bookshelf", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item).define('#', this.material).define('B', ItemTags.BOOKSHELF_BOOKS)
                        .pattern("###").pattern("BBB").pattern("###").unlockedBy("has_books", has(ItemTags.BOOKSHELF_BOOKS))
                        .group("bookshelves").save(this.output)
        );
        PROVIDERS.put("chest", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item).define('#', this.material)
                        .pattern("###").pattern("# #").pattern("###").unlockedBy("has_planks", has(this.material))
                        .group("chests").save(this.output)
        );
        PROVIDERS.put("barrel", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item).define('#', this.material).define('S', ingredient.entryGetter().getSecond())
                        .pattern("#S#").pattern("# #").pattern("#S#").unlockedBy("has_planks", has(this.material)).unlockedBy("has_slabs", has(ingredient))
                        .group("barrels").save(this.output)
        );

        // Wood items
        PROVIDERS.put("stick", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item).define('#', this.material)
                        .pattern("#").pattern("#").unlockedBy("has_planks", has(this.material))
                        .group("sticks").save(this.output)
        );
        PROVIDERS.put("boat", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, item).define('#', this.material)
                        .pattern("# #").pattern("###").unlockedBy("in_water", insideOf(Blocks.WATER))
                        .group("boat").save(this.output)
        );
        PROVIDERS.put("chest_boat", (item, ingredient, stonecuttingRecipes) ->
                ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, item).requires(DifferenceIngredient.of(Ingredient.of(Tags.Items.CHESTS_WOODEN), Ingredient.of(Tags.Items.CHESTS_TRAPPED))).requires(ingredient.entryGetter().getSecond())
                        .unlockedBy("has_boat", has(ItemTags.BOATS))
                        .group("chest_boat").save(this.output)
        );
        PROVIDERS.put("sword", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, item).define('#', this.material).define('S', this.rods)
                        .pattern("#").pattern("#").pattern("S").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("pickaxe", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods)
                        .pattern("###").pattern(" S ").pattern(" S ").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("shovel", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods)
                        .pattern("#").pattern("S").pattern("S").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("axe", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods)
                        .pattern("##").pattern("#S").pattern(" S").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("hoe", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods)
                        .pattern("##").pattern(" S").pattern(" S").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );

        // Modded items
        PROVIDERS.put("stancements/shelf", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item).define('#', this.material).define('S', this.rods)
                        .pattern("###").pattern("S S").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("stackedgoods/hammer", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods)
                        .pattern(" ##").pattern(" S#").pattern("S  ").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("stackedgoods/scraper", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods)
                        .pattern("#").pattern("#").pattern("S").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("stackedgoods/gem_cutter", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods)
                        .pattern("#").pattern("S").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("backmath/mortar_and_pestle", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods).define('F', ReItemTags.FLINT)
                        .pattern("  S").pattern("#F#").pattern(" # ").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("backmath/knife", (item, ingredient, stonecuttingRecipes) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item).define('#', this.material).define('S', this.rods)
                        .pattern("S ").pattern(" #").unlockedBy(hasItem(this.material), has(this.material))
                        .save(this.output)
        );

        this.addProviders();
    }

    /// Makes the save location of a recipe: `namespace:folder/recipe.json`.
    public static ResourceLocation saveLocation(ItemLike material, String folder) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(material.asItem());
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(), folder + "/" + location.getPath());
    }

    /// Makes the advancement condition name for the recipe: `has_<item_name>`.
    /// @param material The material used in the recipe.
    public static String hasItem(ItemLike material) {
        return "has_" + BuiltInRegistries.ITEM.getKey(material.asItem()).getPath();
    }

    /// Adds custom providers to the provider entries map. This is usually paired with the `add()` methods in the {@linkplain Builder builder} below.
    public void addProviders() {}

    /// A builder for all the recipes. This is where entries can be added to the {@link #RECIPES} map.
    public static class Builder {
        private final RecipeOutput output;
        private final ItemLike material;
        private final TagKey<Item> rods;
        private boolean stonecuttingRecipes = false;

        /// A builder for all the recipes. This is where entries can be added to the {@link #RECIPES} map.
        /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
        /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
        /// @param rods An item tag that's used as the default "stick" item for recipes, used for fences and fence gates.
        public Builder(RecipeOutput output, ItemLike material, TagKey<Item> rods) {
            this.output = output;
            this.material = material;
            this.rods = rods;
        }

        /// Makes the recipe builder generate stonecutting recipes for the following recipes:
        /// `stairs`, `slab`, `wall`, `pressure_plate` and `button`.
        public Builder withStonecuttingRecipes() {
            this.stonecuttingRecipes = true;
            return this;
        }

        public Builder add(String type, ItemLike item) {
            RECIPES.put(type, () -> Pair.of(item, Ingredient.of()));
            return this;
        }

        public Builder add(String type, ItemLike item, Ingredient ingredient) {
            RECIPES.put(type, () -> Pair.of(item, ingredient));
            return this;
        }

        // Common blocks
        public Builder stairs(ItemLike stairs) {
            RECIPES.put("stairs", () -> Pair.of(stairs, Ingredient.of()));
            return this;
        }

        public Builder slab(ItemLike slab) {
            RECIPES.put("slab", () -> Pair.of(slab, Ingredient.of()));
            return this;
        }

        public Builder wall(ItemLike wall) {
            RECIPES.put("wall", () -> Pair.of(wall, Ingredient.of()));
            return this;
        }

        public Builder pressurePlate(ItemLike pressurePlate) {
            RECIPES.put("pressure_plate", () -> Pair.of(pressurePlate, Ingredient.of()));
            return this;
        }

        public Builder button(ItemLike button) {
            RECIPES.put("button", () -> Pair.of(button, Ingredient.of()));
            return this;
        }

        // Wood blocks
        public Builder sapling(ItemLike sapling, ItemLike leaves) {
            RECIPES.put("sapling", this.itemEntry(sapling, leaves));
            return this;
        }

        public Builder wood(ItemLike wood, ItemLike log) {
            RECIPES.put("wood", this.itemEntry(wood, log));
            return this;
        }

        public Builder wood(ItemLike wood, TagKey<Item> log) {
            RECIPES.put("wood", this.tagEntry(wood, log));
            return this;
        }

        public Builder strippedWood(ItemLike strippedWood, ItemLike strippedLog) {
            RECIPES.put("stripped_wood", this.itemEntry(strippedWood, strippedLog));
            return this;
        }

        public Builder strippedWood(ItemLike strippedWood, TagKey<Item> strippedLog) {
            RECIPES.put("stripped_wood", this.tagEntry(strippedWood, strippedLog));
            return this;
        }

        public Builder planks(ItemLike planks, TagKey<Item> logs) {
            RECIPES.put("planks", this.tagEntry(planks, logs));
            return this;
        }

        public Builder woodenStairs(ItemLike woodenStairs) {
            RECIPES.put("wooden_stairs", () -> Pair.of(woodenStairs, Ingredient.of()));
            return this;
        }

        public Builder woodenSlab(ItemLike woodenSlab) {
            RECIPES.put("wooden_slab", () -> Pair.of(woodenSlab, Ingredient.of()));
            return this;
        }

        public Builder woodenFence(ItemLike woodenFence) {
            RECIPES.put("wooden_fence", () -> Pair.of(woodenFence, Ingredient.of()));
            return this;
        }

        public Builder woodenFenceGate(ItemLike woodenFenceGate) {
            RECIPES.put("wooden_fence_gate", () -> Pair.of(woodenFenceGate, Ingredient.of()));
            return this;
        }

        public Builder woodenDoor(ItemLike woodenDoor) {
            RECIPES.put("wooden_door", () -> Pair.of(woodenDoor, Ingredient.of()));
            return this;
        }

        public Builder woodenTrapdoor(ItemLike woodenTrapdoor) {
            RECIPES.put("wooden_trapdoor", () -> Pair.of(woodenTrapdoor, Ingredient.of()));
            return this;
        }

        public Builder woodenPressurePlate(ItemLike woodenPressurePlate) {
            RECIPES.put("wooden_pressure_plate", () -> Pair.of(woodenPressurePlate, Ingredient.of()));
            return this;
        }

        public Builder woodenButton(ItemLike woodenButton) {
            RECIPES.put("wooden_button", () -> Pair.of(woodenButton, Ingredient.of()));
            return this;
        }

        public Builder sign(ItemLike sign) {
            RECIPES.put("sign", () -> Pair.of(sign, Ingredient.of()));
            return this;
        }

        public Builder hangingSign(ItemLike hangingSign, ItemLike strippedLog) {
            RECIPES.put("hanging_sign", this.itemEntry(hangingSign, strippedLog));
            return this;
        }

        public Builder craftingTable(ItemLike craftingTable) {
            RECIPES.put("crafting_table", () -> Pair.of(craftingTable, Ingredient.of()));
            return this;
        }

        public Builder bookshelf(ItemLike bookshelf) {
            RECIPES.put("bookshelf", () -> Pair.of(bookshelf, Ingredient.of()));
            return this;
        }

        public Builder chest(ItemLike chest) {
            RECIPES.put("chest", () -> Pair.of(chest, Ingredient.of()));
            return this;
        }

        public Builder barrel(ItemLike barrel, ItemLike slab) {
            RECIPES.put("barrel", this.itemEntry(barrel, slab));
            return this;
        }

        // Wood items
        public Builder stick(ItemLike stick) {
            RECIPES.put("stick", () -> Pair.of(stick, Ingredient.of()));
            return this;
        }

        public Builder boat(ItemLike boat) {
            RECIPES.put("boat", () -> Pair.of(boat, Ingredient.of()));
            return this;
        }

        public Builder chestBoat(ItemLike chestBoat, ItemLike boat) {
            RECIPES.put("chest_boat", () -> Pair.of(chestBoat, Ingredient.of(boat)));
            return this;
        }

        public Builder sword(ItemLike sword) {
            RECIPES.put("sword", () -> Pair.of(sword, Ingredient.of()));
            return this;
        }

        public Builder pickaxe(ItemLike pickaxe) {
            RECIPES.put("pickaxe", () -> Pair.of(pickaxe, Ingredient.of()));
            return this;
        }

        public Builder shovel(ItemLike shovel) {
            RECIPES.put("shovel", () -> Pair.of(shovel, Ingredient.of()));
            return this;
        }

        public Builder axe(ItemLike axe) {
            RECIPES.put("axe", () -> Pair.of(axe, Ingredient.of()));
            return this;
        }

        public Builder hoe(ItemLike hoe) {
            RECIPES.put("hoe", () -> Pair.of(hoe, Ingredient.of()));
            return this;
        }

        // Modded items
        public Builder shelf(ItemLike shelf) {
            RECIPES.put("stancements/shelf", () -> Pair.of(shelf, Ingredient.of()));
            return this;
        }

        public Builder hammer(ItemLike hammer) {
            RECIPES.put("stackedgoods/hammer", () -> Pair.of(hammer, Ingredient.of()));
            return this;
        }

        public Builder scraper(ItemLike scraper) {
            RECIPES.put("stackedgoods/scraper", () -> Pair.of(scraper, Ingredient.of()));
            return this;
        }

        public Builder gemCutter(ItemLike gemCutter) {
            RECIPES.put("stackedgoods/gem_cutter", () -> Pair.of(gemCutter, Ingredient.of()));
            return this;
        }

        public Builder mortarAndPestle(ItemLike mortarAndPestle) {
            RECIPES.put("backmath/mortar_and_pestle", () -> Pair.of(mortarAndPestle, Ingredient.of()));
            return this;
        }

        public Builder knife(ItemLike knife) {
            RECIPES.put("backmath/knife", () -> Pair.of(knife, Ingredient.of()));
            return this;
        }

        public RecipeFamilyEntry.ItemEntry itemEntry(ItemLike item, ItemLike ingredient) {
            return new RecipeFamilyEntry.ItemEntry() {
                @Override
                public Pair<ItemLike, Item> entry() {
                    return Pair.of(item, ingredient.asItem());
                }
            };
        }

        public RecipeFamilyEntry.TagEntry tagEntry(ItemLike item, TagKey<Item> ingredient) {
            return new RecipeFamilyEntry.TagEntry() {
                @Override
                public Pair<ItemLike, TagKey<Item>> entry() {
                    return Pair.of(item, ingredient);
                }
            };
        }

        public RecipeFamilyProvider build() {
            RecipeFamilyProvider provider = new RecipeFamilyProvider(this.output, this.material, this.rods);
            for (String entry : RECIPES.keySet()) {
                RecipeFamilyEntry recipeEntry = RECIPES.get(entry);
                PROVIDERS.get(entry).makeRecipe(recipeEntry.entryGetter().getFirst(), recipeEntry, this.stonecuttingRecipes);
            }
            PROVIDERS.clear();
            RECIPES.clear();
            return provider;
        }
    }

    @SuppressWarnings("deprecation")
    protected static Criterion<EnterBlockTrigger.TriggerInstance> insideOf(Block block) {
        return CriteriaTriggers.ENTER_BLOCK.createCriterion(new EnterBlockTrigger.TriggerInstance(Optional.empty(), Optional.of(block.builtInRegistryHolder()), Optional.empty()));
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(RecipeFamilyEntry entry) {
        if (entry instanceof RecipeFamilyEntry.ItemEntry itemEntry) {
            return inventoryTrigger(ItemPredicate.Builder.item().of(itemEntry.entry().getSecond()));
        } else if (entry instanceof RecipeFamilyEntry.TagEntry tagEntry) {
            return inventoryTrigger(ItemPredicate.Builder.item().of(tagEntry.entry().getSecond()));
        }
        return has(Items.BARRIER);
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> itemTag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemTag));
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike itemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike));
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate.Builder... items) {
        return inventoryTrigger(Arrays.stream(items).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... predicates) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(predicates)));
    }
}