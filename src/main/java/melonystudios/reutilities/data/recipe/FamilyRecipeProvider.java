package melonystudios.reutilities.data.recipe;

import com.mojang.datafixers.util.Pair;
import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.*;

public class FamilyRecipeProvider {
    /// Map of item types (strings) to {@linkplain FamilyRecipeProviderEntry family recipe provider entries}, used to create all the base recipes.
    protected static final Map<String, FamilyRecipeProviderEntry> PROVIDERS = new LinkedHashMap<>();
    /// Map of all items that should be generated, paired with their respective types.
    protected static final Map<String, FamilyRecipeEntry> RECIPES = new LinkedHashMap<>();
    protected final RecipeOutput output;
    protected final ItemLike material;
    protected final TagKey<Item> rods;

    /// Helper data generator class for generating whole families of recipes.
    /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
    /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
    public FamilyRecipeProvider(RecipeOutput output, ItemLike material) {
        this(output, material, Tags.Items.RODS_WOODEN);
    }

    /// Helper data generator class for generating whole families of recipes.
    /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
    /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
    /// @param rods An item tag that's used as the default "stick" item for recipes, used for fences and fence gates.
    public FamilyRecipeProvider(RecipeOutput output, ItemLike material, TagKey<Item> rods) {
        this.output = output;
        this.material = material;
        this.rods = rods;
        this.addDefaultProviders();
    }

    /// Makes a new instance of the {@linkplain Builder recipe builder}.
    /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
    /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
    public static FamilyRecipeProvider.Builder builder(RecipeOutput output, ItemLike material) {
        return builder(output, material, Tags.Items.RODS_WOODEN);
    }

    /// Makes a new instance of the {@linkplain Builder recipe builder}.
    /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
    /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
    /// @param rods An item tag that's used as the default "stick" item for recipes, used for fences and fence gates.
    public static FamilyRecipeProvider.Builder builder(RecipeOutput output, ItemLike material, TagKey<Item> rods) {
        return new FamilyRecipeProvider.Builder(output, material, rods);
    }

    public RecipeOutput recipeOutput() {
        return this.output;
    }

    public ItemLike material() {
        return this.material;
    }

    public TagKey<Item> rods() {
        return this.rods;
    }

    /// Adds all default {@linkplain FamilyRecipeProviderEntry family recipe provider entries} to the providers map.
    /// New providers can be added using the {@link #addProviders()} method below.
    protected final void addDefaultProviders() {
        // Common blocks
        PROVIDERS.put("stairs", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 6).define('#', this.material)
                        .pattern("  #").pattern(" ##").pattern("###").unlockedBy("has_material", has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("slab", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 6).define('#', this.material)
                        .pattern("###").unlockedBy("has_material", has(this.material))
                        .save(this.output)
        );
        PROVIDERS.put("wall", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item, 6).define('#', this.material)
                        .pattern("###").pattern("###").unlockedBy("has_material", has(this.material))
                        .save(this.output)
        );

        // Wood blocks
        PROVIDERS.put("sapling", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item).define('#', ingredient.entryGetter().getSecond()).define('S', this.rods)
                        .pattern("#").pattern("S").unlockedBy("has_leaves", has(ingredient))
                        .group("sapling").save(this.output)
        );
        PROVIDERS.put("wood", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 3).define('#', ingredient.entryGetter().getSecond())
                        .pattern("##").pattern("##").unlockedBy("has_logs", has(ingredient))
                        .group("bark").save(this.output)
        );
        PROVIDERS.put("stripped_wood", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 3).define('#', ingredient.entryGetter().getSecond())
                        .pattern("##").pattern("##").unlockedBy("has_logs", has(ingredient))
                        .group("bark").save(this.output)
        );
        PROVIDERS.put("planks", (item, ingredient) ->
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, this.material, 4).requires(ingredient.entryGetter().getSecond())
                        .unlockedBy("has_logs", has(ingredient))
                        .group("planks").save(this.output)
        );
        PROVIDERS.put("wooden_stairs", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 6).define('#', this.material)
                        .pattern("  #").pattern(" ##").pattern("###").unlockedBy("has_planks", has(this.material))
                        .group("wooden_stairs").save(this.output)
        );
        PROVIDERS.put("wooden_slab", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 6).define('#', this.material)
                        .pattern("###").unlockedBy("has_planks", has(this.material))
                        .group("wooden_slab").save(this.output)
        );
        PROVIDERS.put("wooden_fence", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item, 3).define('#', this.material).define('S', this.rods)
                        .pattern("#S#").pattern("#S#").unlockedBy("has_planks", has(this.material))
                        .group("wooden_fence").save(this.output)
        );
        PROVIDERS.put("wooden_fence_gate", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item).define('#', this.material).define('S', this.rods)
                        .pattern("S#S").pattern("S#S").unlockedBy("has_planks", has(this.material))
                        .group("wooden_fence_gate").save(this.output)
        );
        PROVIDERS.put("wooden_door", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item, 3).define('#', this.material)
                        .pattern("##").pattern("##").pattern("##").unlockedBy("has_planks", has(this.material))
                        .group("wooden_door").save(this.output)
        );
        PROVIDERS.put("wooden_trapdoor", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item, 2).define('#', this.material)
                        .pattern("###").pattern("###").unlockedBy("has_planks", has(this.material))
                        .group("wooden_trapdoor").save(this.output)
        );
        PROVIDERS.put("wooden_pressure_plate", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, item).define('#', this.material)
                        .pattern("##").unlockedBy("has_planks", has(this.material))
                        .group("wooden_pressure_plate").save(this.output)
        );
        PROVIDERS.put("wooden_button", (item, ingredient) ->
                ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, item).requires(this.material)
                        .unlockedBy("has_planks", has(this.material))
                        .group("wooden_button").save(this.output)
        );
        PROVIDERS.put("sign", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item, 3).define('#', this.material).define('S', this.rods)
                        .pattern("###").pattern("###").pattern(" S ").unlockedBy("has_planks", has(this.material))
                        .group("wooden_sign").save(this.output)
        );
        PROVIDERS.put("hanging_sign", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item, 6).define('#', ingredient.entryGetter().getSecond()).define('S', ReItemTags.CHAINS)
                        .pattern("S S").pattern("###").pattern("###").unlockedBy("has_stripped_logs", has(ingredient))
                        .group("hanging_sign").save(this.output)
        );
        PROVIDERS.put("crafting_table", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item).define('#', this.material)
                        .pattern("##").pattern("##").unlockedBy("has_planks", has(this.material))
                        .group("crafting_tables").save(this.output)
        );
        PROVIDERS.put("bookshelf", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item).define('#', this.material).define('B', ItemTags.BOOKSHELF_BOOKS)
                        .pattern("###").pattern("BBB").pattern("###").unlockedBy("has_books", has(ItemTags.BOOKSHELF_BOOKS))
                        .group("bookshelves").save(this.output)
        );
        PROVIDERS.put("chest", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item).define('#', this.material)
                        .pattern("###").pattern("# #").pattern("###").unlockedBy("has_planks", has(this.material))
                        .group("chests").save(this.output)
        );
        PROVIDERS.put("barrel", (item, ingredient) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item).define('#', this.material).define('S', ingredient.entryGetter().getSecond())
                        .pattern("#S#").pattern("# #").pattern("#S#").unlockedBy("has_planks", has(this.material)).unlockedBy("has_slabs", has(ingredient))
                        .group("barrels").save(this.output)
        );

        this.addProviders();
    }

    /// Adds custom providers to the provider entries map. This is usually paired with the `add()` methods in the {@linkplain Builder builder} below.
    public void addProviders() {}

    /// A builder for all the recipes. This is where entries can be added to the {@link #RECIPES} map.
    // stuff's looking weird with the TagEntry/ItemEntry code down there, but I'll fix it in post, don't have time for this ~isa 17-8-25
    public static class Builder {
        protected final RecipeOutput output;
        protected final ItemLike material;
        protected final TagKey<Item> rods;

        /// @param output The default {@linkplain RecipeOutput output} for recipes, used for saving.
        /// @param material An item that's used as the base material for all recipes, like planks or cobblestone.
        /// @param rods An item tag that's used as the default "stick" item for recipes, used for fences and fence gates.
        public Builder(RecipeOutput output, ItemLike material, TagKey<Item> rods) {
            this.output = output;
            this.material = material;
            this.rods = rods;
        }

        public FamilyRecipeProvider.Builder add(String type, ItemLike item) {
            RECIPES.put(type, () -> Pair.of(item, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder add(String type, ItemLike item, Ingredient ingredients) {
            RECIPES.put(type, () -> Pair.of(item, ingredients));
            return this;
        }

        // Common blocks
        public FamilyRecipeProvider.Builder stairs(ItemLike stairs) {
            RECIPES.put("stairs", () -> Pair.of(stairs, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder slab(ItemLike slab) {
            RECIPES.put("slab", () -> Pair.of(slab, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder wall(ItemLike wall) {
            RECIPES.put("wall", () -> Pair.of(wall, Ingredient.of()));
            return this;
        }

        // Wood blocks
        public FamilyRecipeProvider.Builder sapling(ItemLike sapling, ItemLike leaves) {
            RECIPES.put("sapling", () -> Pair.of(sapling, Ingredient.of(leaves)));
            return this;
        }

        public FamilyRecipeProvider.Builder wood(ItemLike wood, ItemLike log) {
            RECIPES.put("wood", new FamilyRecipeEntry.ItemEntry() {
                @Override
                public Pair<ItemLike, Item> entry() {
                    return new Pair<>(wood, log.asItem());
                }
            });
            return this;
        }

        public FamilyRecipeProvider.Builder wood(ItemLike wood, TagKey<Item> log) {
            RECIPES.put("wood", new FamilyRecipeEntry.TagEntry() {
                @Override
                public Pair<ItemLike, TagKey<Item>> entry() {
                    return new Pair<>(wood, log);
                }
            });
            return this;
        }

        public FamilyRecipeProvider.Builder strippedWood(ItemLike strippedWood, ItemLike strippedLog) {
            RECIPES.put("stripped_wood", new FamilyRecipeEntry.ItemEntry() {
                @Override
                public Pair<ItemLike, Item> entry() {
                    return new Pair<>(strippedWood, strippedLog.asItem());
                }
            });
            return this;
        }

        public FamilyRecipeProvider.Builder strippedWood(ItemLike strippedWood, TagKey<Item> strippedLog) {
            RECIPES.put("stripped_wood", new FamilyRecipeEntry.TagEntry() {
                @Override
                public Pair<ItemLike, TagKey<Item>> entry() {
                    return new Pair<>(strippedWood, strippedLog);
                }
            });
            return this;
        }

        public FamilyRecipeProvider.Builder planks(ItemLike planks, TagKey<Item> logs) {
            RECIPES.put("planks", new FamilyRecipeEntry.TagEntry() {
                @Override
                public Pair<ItemLike, TagKey<Item>> entry() {
                    return new Pair<>(planks, logs);
                }
            });
            return this;
        }

        public FamilyRecipeProvider.Builder woodenStairs(ItemLike woodenStairs) {
            RECIPES.put("wooden_stairs", () -> Pair.of(woodenStairs, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder woodenSlab(ItemLike woodenSlab) {
            RECIPES.put("wooden_slab", () -> Pair.of(woodenSlab, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder woodenFence(ItemLike woodenFence) {
            RECIPES.put("wooden_fence", () -> Pair.of(woodenFence, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder woodenFenceGate(ItemLike woodenFenceGate) {
            RECIPES.put("wooden_fence_gate", () -> Pair.of(woodenFenceGate, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder woodenDoor(ItemLike woodenDoor) {
            RECIPES.put("wooden_door", () -> Pair.of(woodenDoor, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder woodenTrapdoor(ItemLike woodenTrapdoor) {
            RECIPES.put("wooden_trapdoor", () -> Pair.of(woodenTrapdoor, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder woodenPressurePlate(ItemLike woodenPressurePlate) {
            RECIPES.put("wooden_pressure_plate", () -> Pair.of(woodenPressurePlate, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder woodenButton(ItemLike woodenButton) {
            RECIPES.put("wooden_button", () -> Pair.of(woodenButton, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder sign(ItemLike sign) {
            RECIPES.put("sign", () -> Pair.of(sign, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder hangingSign(ItemLike hangingSign, ItemLike strippedLog) {
            RECIPES.put("hanging_sign", new FamilyRecipeEntry.ItemEntry() {
                @Override
                public Pair<ItemLike, Item> entry() {
                    return new Pair<>(hangingSign, strippedLog.asItem());
                }
            });
            return this;
        }

        public FamilyRecipeProvider.Builder craftingTable(ItemLike craftingTable) {
            RECIPES.put("crafting_table", () -> Pair.of(craftingTable, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder bookshelf(ItemLike bookshelf) {
            RECIPES.put("bookshelf", () -> Pair.of(bookshelf, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder chest(ItemLike chest) {
            RECIPES.put("chest", () -> Pair.of(chest, Ingredient.of()));
            return this;
        }

        public FamilyRecipeProvider.Builder barrel(ItemLike barrel, ItemLike slab) {
            RECIPES.put("barrel", new FamilyRecipeEntry.ItemEntry() {
                @Override
                public Pair<ItemLike, Item> entry() {
                    return new Pair<>(barrel, slab.asItem());
                }
            });
            return this;
        }

        public FamilyRecipeProvider build() {
            FamilyRecipeProvider provider = new FamilyRecipeProvider(this.output, this.material, this.rods);
            for (String entry : RECIPES.keySet()) {
                FamilyRecipeEntry recipeEntry = RECIPES.get(entry);
                PROVIDERS.get(entry).makeRecipe(recipeEntry.entryGetter().getFirst(), recipeEntry);
            }
            PROVIDERS.clear();
            RECIPES.clear();
            return provider;
        }
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(FamilyRecipeEntry entry) {
        if (entry instanceof FamilyRecipeEntry.ItemEntry itemEntry) {
            return inventoryTrigger(ItemPredicate.Builder.item().of(itemEntry.entry().getSecond()));
        } else if (entry instanceof FamilyRecipeEntry.TagEntry tagEntry) {
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