package melonystudios.reutilities.data.recipe;

import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;

import static melonystudios.reutilities.Reutilities.reutilities;

@ApiStatus.Internal
public final class InternalReRecipeProvider extends ReRecipeProvider {
    public InternalReRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output, HolderLookup.Provider registries) {
        // Cobblestone replacements
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.PISTON).define('#', ReItemTags.COBBLESTONE_CRAFTING_MATERIALS).define('P', ItemTags.PLANKS).define('I', Tags.Items.INGOTS_IRON).define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("PPP").pattern("#I#").pattern("#R#").unlockedBy("has_redstone_dust", has(Tags.Items.DUSTS_REDSTONE))
                .save(output, reutilities("piston"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.DISPENSER).define('#', ReItemTags.COBBLESTONE_CRAFTING_MATERIALS).define('R', Tags.Items.DUSTS_REDSTONE).define('B', Tags.Items.TOOLS_BOW)
                .pattern("###").pattern("#B#").pattern("#R#").unlockedBy("has_bow", has(Tags.Items.TOOLS_BOW))
                .save(output, reutilities("dispenser"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.DROPPER).define('#', ReItemTags.COBBLESTONE_CRAFTING_MATERIALS).define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("###").pattern("# #").pattern("#R#").unlockedBy("has_redstone_dust", has(Tags.Items.DUSTS_REDSTONE))
                .save(output, reutilities("dropper"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.OBSERVER).define('#', ReItemTags.COBBLESTONE_CRAFTING_MATERIALS).define('R', Tags.Items.DUSTS_REDSTONE).define('Q', Tags.Items.GEMS_QUARTZ)
                .pattern("###").pattern("RRQ").pattern("###").unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .save(output, reutilities("observer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.LEVER).define('#', ReItemTags.COBBLESTONE_CRAFTING_MATERIALS).define('S', Tags.Items.RODS_WOODEN)
                .pattern("S").pattern("#").unlockedBy("has_cobblestone", has(ReItemTags.COBBLESTONE_CRAFTING_MATERIALS))
                .save(output, reutilities("lever"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, Items.BREWING_STAND).define('#', ReItemTags.COBBLESTONE_CRAFTING_MATERIALS).define('B', Tags.Items.RODS_BLAZE)
                .pattern(" B ").pattern("###").unlockedBy("has_blaze_rod", has(Tags.Items.RODS_BLAZE))
                .save(output, reutilities("brewing_stand"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.FURNACE).define('#', ReItemTags.COBBLESTONE_CRAFTING_MATERIALS)
                .pattern("###").pattern("# #").pattern("###").unlockedBy("has_cobblestone", has(ReItemTags.COBBLESTONE_CRAFTING_MATERIALS))
                .save(output, reutilities("furnace"));

        // Stone replacements
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.STONECUTTER).define('#', ReItemTags.STONE_CRAFTING_MATERIALS).define('I', Tags.Items.INGOTS_IRON)
                .pattern(" I ").pattern("###").unlockedBy("has_stone", has(ReItemTags.STONE_CRAFTING_MATERIALS))
                .save(output, reutilities("stonecutter"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.REPEATER).define('#', ReItemTags.STONE_CRAFTING_MATERIALS).define('R', Tags.Items.DUSTS_REDSTONE).define('T', Items.REDSTONE_TORCH)
                .pattern("TRT").pattern("###").unlockedBy("has_redstone_torch", has(Items.REDSTONE_TORCH))
                .save(output, reutilities("redstone_repeater"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.COMPARATOR).define('#', ReItemTags.STONE_CRAFTING_MATERIALS).define('Q', Tags.Items.GEMS_QUARTZ).define('T', Items.REDSTONE_TORCH)
                .pattern(" T ").pattern("TQT").pattern("###").unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
                .save(output, reutilities("redstone_comparator"));
    }
}
