package melonystudios.reutilities.data;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.data.family.BlockFamilyModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class ReBlockStateProvider extends BlockStateProvider {
    protected final String modID;

    public ReBlockStateProvider(PackOutput output, String modID, ExistingFileHelper fileHelper) {
        super(output, modID, fileHelper);
        this.modID = modID;
    }

    public BlockFamilyModelProvider blockFamily(ResourceLocation texture, String materialName) {
        return new BlockFamilyModelProvider(this, this.modID, materialName, texture);
    }

    public void fluid(Block block, ResourceLocation stillTexture) {
        this.simpleBlock(block, this.models().getBuilder(BuiltInRegistries.BLOCK.getKey(block).getPath()).texture("particle", stillTexture));
    }

    public void simplePlantWithPotted(Block block, Block pottedBlock, ResourceLocation texture) {
        this.simpleBlock(block, this.models().cross(BuiltInRegistries.BLOCK.getKey(block).getPath(), texture));
        this.simpleBlock(pottedBlock, this.models().withExistingParent(BuiltInRegistries.BLOCK.getKey(pottedBlock).getPath(), this.mcLoc("block/flower_pot_cross")).texture("plant", texture));
    }

    public void leaves(Block leaves, ResourceLocation texture) {
        this.simpleBlock(leaves, this.models().withExistingParent(BuiltInRegistries.BLOCK.getKey(leaves).getPath(), this.mcLoc("block/leaves")).texture("all", texture));
    }

    public void doubleSlab(Block block, ResourceLocation texture, ResourceLocation sideTexture) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        this.models().cubeBottomTop("double_" + name.getPath(), sideTexture, texture, texture);
        this.slabBlock((SlabBlock) block, ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "double_" + name.getPath()), sideTexture, texture, texture);
    }

    /// Copy of NeoForge's {@link #fenceBlock(FenceBlock, ResourceLocation) fenceBlock()} method that also generates the inventory model.
    ///
    /// @param fence The fence block to generate.
    /// @param texture A resource location pointing to the fence's texture.
    public void fenceBlock(Block fence, ResourceLocation texture) {
        this.fenceBlock((FenceBlock) fence, texture);
        this.models().withExistingParent(BuiltInRegistries.BLOCK.getKey(fence).getPath() + "_inventory", this.mcLoc("block/fence_inventory")).texture("texture", texture);
    }

    /// Copy of NeoForge's {@link #wallBlock(WallBlock, ResourceLocation) wallBlock()} method that also generates the inventory model.
    ///
    /// @param wall The wall block to generate.
    /// @param texture A resource location pointing to the wall's texture.
    public void wallBlock(Block wall, ResourceLocation texture) {
        this.wallBlock((WallBlock) wall, texture);
        models().withExistingParent(BuiltInRegistries.BLOCK.getKey(wall).getPath() + "_inventory", this.mcLoc("block/wall_inventory")).texture("wall", texture);
    }

    /// Copy of NeoForge's {@link #buttonBlock(ButtonBlock, ResourceLocation) buttonBlock()} method that also generates the inventory model.
    ///
    /// @param button  The button block to generate.
    /// @param texture A resource location pointing to the button's texture.
    public void buttonBlock(Block button, ResourceLocation texture) {
        this.buttonBlock((ButtonBlock) button, texture);
        this.models().withExistingParent(BuiltInRegistries.BLOCK.getKey(button).getPath() + "_inventory", this.mcLoc("block/button_inventory")).texture("texture", texture);
    }

    public void weightedPressurePlateBlock(Block pressurePlate, ResourceLocation texture) {
        this.getVariantBuilder(pressurePlate).forAllStates(state -> {
            int weight = state.getValue(BlockStateProperties.POWER);
            return ConfiguredModel.builder().modelFile(this.models().getBuilder(BuiltInRegistries.BLOCK.getKey(pressurePlate).getPath() + (weight > 0 ? "_down" : ""))
                    .parent(this.models().getExistingFile(this.mcLoc("block/pressure_plate_" + (weight > 0 ? "down" : "up"))))
                    .texture("texture", texture)).build();
        });
    }

    public void ladder(Block ladder) {
        this.getVariantBuilder(ladder).forAllStatesExcept(state -> {
            String ladderPath = BuiltInRegistries.BLOCK.getKey(ladder).getPath();
            return ConfiguredModel.builder()
                    .modelFile(this.models().withExistingParent(ladderPath, Reutilities.reutilities("block/template_ladder")).texture("ladder", this.modLoc("block/" + ladderPath)))
                    .rotationY((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().toYRot())
                    .build();
        }, BlockStateProperties.WATERLOGGED);
    }

    public void craftingTable(Block block, String type) {
        this.craftingTable(block, this.modLoc("block/" + type + "_crafting_table_side"), this.modLoc("block/" + type + "_planks"), this.modLoc("block/" + type + "_crafting_table_top"));
    }

    public void craftingTable(Block block, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        this.simpleBlock(block, this.models().cubeBottomTop(BuiltInRegistries.BLOCK.getKey(block).getPath(), sideTexture, bottomTexture, topTexture));
    }

    public void chain(Block chain) {
        this.getVariantBuilder(chain).forAllStatesExcept(state -> {
            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
            String chainPath = BuiltInRegistries.BLOCK.getKey(chain).getPath();

            return ConfiguredModel.builder()
                    .modelFile(models().withExistingParent(chainPath, Reutilities.reutilities("block/template_chain")).texture("chain", this.modLoc("block/" + chainPath)))
                    .rotationX(axis == Direction.Axis.X || axis == Direction.Axis.Z ? 90 : 0)
                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                    .build();
        }, BlockStateProperties.WATERLOGGED);
    }

    public void lantern(Block lantern, boolean topDownModel) {
        String lanternPath = BuiltInRegistries.BLOCK.getKey(lantern).getPath();
        ModelFile standingLantern = this.models().withExistingParent(lanternPath, topDownModel ? Reutilities.reutilities("block/template_mid_term_lantern") : this.mcLoc("block/template_lantern")).texture("lantern", "block/" + lanternPath);
        ModelFile hangingLantern = this.models().withExistingParent(lanternPath + "_hanging", topDownModel ? Reutilities.reutilities("block/template_hanging_mid_term_lantern") : this.mcLoc("block/template_hanging_lantern")).texture("lantern", "block/" + lanternPath);

        this.getVariantBuilder(lantern).forAllStatesExcept(state -> ConfiguredModel.builder()
                .modelFile(state.getValue(BlockStateProperties.HANGING) ? hangingLantern : standingLantern).build(),
                BlockStateProperties.WATERLOGGED);
    }

    public void grassBlock(Block block, ResourceLocation baseTexture, ResourceLocation bottomTexture) {
        this.getVariantBuilder(block).forAllStates(state -> {
            String blockPath = BuiltInRegistries.BLOCK.getKey(block).getPath();
            boolean snowy = state.getValue(BlockStateProperties.SNOWY);

            ModelFile grassBlockModel = this.models().withExistingParent(blockPath + (snowy ? "_snowy" : ""), snowy ? this.modLoc("block/cube_bottom_top") : Reutilities.reutilities("block/template_grass_block"))
                    .texture("bottom", bottomTexture).texture("top", baseTexture + "_top")
                    .texture("side", baseTexture + "_side" + (snowy ? "_snowy" : ""))
                    .texture("overlay", baseTexture + "_side_overlay");
            return ConfiguredModel.builder().modelFile(grassBlockModel).nextModel().modelFile(grassBlockModel).rotationY(90).nextModel().modelFile(grassBlockModel).rotationY(180).nextModel().modelFile(grassBlockModel).rotationY(270).build();
        });
    }

    public void pixelShortBlock(Block block, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        this.simpleBlock(block, this.models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), Reutilities.reutilities("block/template_pixel_short_block"))
                .texture("side", sideTexture).texture("dirt", bottomTexture).texture("top", topTexture));
    }

    public void cake(Block cake) {
        this.getVariantBuilder(cake).forAllStates(state -> {
            String cakePath = BuiltInRegistries.BLOCK.getKey(cake).getPath();
            int bites = state.getValue(BlockStateProperties.BITES);
            String bite = bites == 0 ? "" : "_slice" + bites;

            ModelFile cakeModel = this.models().withExistingParent(cakePath + bite, Reutilities.reutilities("block/template_cake" + bite))
                    .texture("inside", this.modLoc("block/" + cakePath + "_inner"))
                    .texture("side", this.modLoc("block/" + cakePath + "_side"))
                    .texture("bottom", this.modLoc("block/" + cakePath + "_bottom"))
                    .texture("top", this.modLoc("block/" + cakePath + "_top"));
            return ConfiguredModel.builder().modelFile(cakeModel).build();
        });
    }

    public void wheat(Block block, String texture) {
        this.getVariantBuilder(block).forAllStates(state -> {
            int ageIndex = wheatAgeIndex(state.getValue(BlockStateProperties.AGE_7));
            return ConfiguredModel.builder().modelFile(this.models().crop(texture + "_stage" + ageIndex, this.modLoc("block/" + texture + "_stage" + ageIndex))).build();
        });
    }

    public void potato(Block block, String texture) {
        this.getVariantBuilder(block).forAllStates(state -> {
            int ageIndex = potatoAgeIndex(state.getValue(BlockStateProperties.AGE_7));
            return ConfiguredModel.builder().modelFile(this.models().crop(texture + "_stage" + ageIndex, this.modLoc("block/" + texture + "_stage" + ageIndex))).build();
        });
    }

    public void wildCrop(Block wildCrop) {
        String wildCropPath = BuiltInRegistries.BLOCK.getKey(wildCrop).getPath();
        this.simpleBlock(wildCrop, this.models().withExistingParent(wildCropPath, Reutilities.reutilities("block/template_wild_crop")).texture("crop", this.modLoc("block/" + wildCropPath)));
    }

    public static int potatoAgeIndex(int age) {
        if (age > 6) return 3;
        if (age > 3) return 2;
        if (age > 1) return 1;
        return 0;
    }

    public static int wheatAgeIndex(int age) {
        if (age > 7) return 0;
        return age;
    }

    public static String moistIndex(int moistLevel) {
        if (moistLevel != 7) return "";
        return "_moist";
    }
}