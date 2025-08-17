package melonystudios.reutilities.data.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.data.models.model.TextureMapping.getBlockTexture;

public class BlockFamilyModelProvider {
    /// Map of block types (strings) to {@linkplain FamilyModelEntry family model entries}, used to create all the base block models.
    protected static final Map<String, FamilyModelEntry> PROVIDERS = new LinkedHashMap<>();
    /// Map of all blocks that should be generated, paired with their respective types.
    protected static final Map<String, Block> BLOCK_TYPES = new LinkedHashMap<>();
    /// Map of extra properties for a specified block. This is used in case of the method requiring another parameter that can't be filled by this class,
    /// like the `orientable` parameter in {@link net.neoforged.neoforge.client.model.generators.BlockStateProvider#trapdoorBlock(TrapDoorBlock, ResourceLocation, boolean) trapdoorBlock()} method.
    protected static final Map<String, List<Object>> EXTRA_PROPERTIES = new LinkedHashMap<>();
    private final ReBlockStateProvider stateProvider;
    private final String modID;
    private final String materialName;
    private final ResourceLocation texture;

    /// Helper data generator class for generating whole families of blocks.
    /// @param stateProvider {@linkplain ReBlockStateProvider *Reutilities*' block state provider}, used as a base for generating the models.
    /// @param modID The current mod using this provider.
    /// @param materialName The name of the material being generated, like "`oak`" or "`aljanstone`".
    /// @param texture A resource location of the default material texture, like "`minecraft:block/oak_planks`.
    public BlockFamilyModelProvider(ReBlockStateProvider stateProvider, String modID, String materialName, ResourceLocation texture) {
        this.stateProvider = stateProvider;
        this.modID = modID;
        this.materialName = materialName;
        this.texture = texture;
        this.addDefaultProviders();
    }

    public ReBlockStateProvider stateProvider() {
        return this.stateProvider;
    }

    public String modID() {
        return this.modID;
    }

    public String materialName() {
        return this.materialName;
    }

    public ResourceLocation defaultTexture() {
        return this.texture;
    }

    public BlockFamilyModelProvider add(String type, Block block) {
        return this.add(type, block, List.of());
    }

    public BlockFamilyModelProvider add(String type, Block block, List<Object> properties) {
        BLOCK_TYPES.put(type, block);
        if (!properties.isEmpty()) EXTRA_PROPERTIES.put(type, properties);
        return this;
    }

    public BlockFamilyModelProvider log(Block log, Block wood, Block strippedLog, Block strippedWood) {
        BLOCK_TYPES.put("log", log);
        BLOCK_TYPES.put("wood", wood);
        BLOCK_TYPES.put("stripped_log", strippedLog);
        BLOCK_TYPES.put("stripped_wood", strippedWood);
        return this;
    }

    public BlockFamilyModelProvider leaves(Block block) {
        BLOCK_TYPES.put("leaves", block);
        return this;
    }

    public BlockFamilyModelProvider plant(Block plant) {
        BLOCK_TYPES.put("plant", plant);
        return this;
    }

    public BlockFamilyModelProvider plant(Block plant, Block pottedPlant) {
        BLOCK_TYPES.put("plant", plant);
        BLOCK_TYPES.put("potted_plant", pottedPlant);
        return this;
    }

    public BlockFamilyModelProvider fullBlock(Block block) {
        BLOCK_TYPES.put("full_block", block);
        return this;
    }

    public BlockFamilyModelProvider planks(Block block) {
        BLOCK_TYPES.put("planks", block);
        return this;
    }

    public BlockFamilyModelProvider stairs(Block stairs) {
        BLOCK_TYPES.put("stairs", stairs);
        return this;
    }

    public BlockFamilyModelProvider slab(Block slab) {
        BLOCK_TYPES.put("slab", slab);
        return this;
    }

    public BlockFamilyModelProvider doubleSlab(Block slab, ResourceLocation sideTexture) {
        BLOCK_TYPES.put("double_slab", slab);
        EXTRA_PROPERTIES.put("double_slab", List.of(sideTexture));
        return this;
    }

    public BlockFamilyModelProvider wall(Block wall) {
        BLOCK_TYPES.put("wall", wall);
        return this;
    }

    public BlockFamilyModelProvider fence(Block fence) {
        BLOCK_TYPES.put("fence", fence);
        return this;
    }

    public BlockFamilyModelProvider fenceGate(Block fenceGate) {
        BLOCK_TYPES.put("fence_gate", fenceGate);
        return this;
    }

    public BlockFamilyModelProvider door(Block door) {
        BLOCK_TYPES.put("door", door);
        return this;
    }

    public BlockFamilyModelProvider trapdoor(Block trapdoor) {
        this.trapdoor(trapdoor, true);
        return this;
    }

    public BlockFamilyModelProvider trapdoor(Block trapdoor, boolean orientable) {
        BLOCK_TYPES.put("trapdoor", trapdoor);
        EXTRA_PROPERTIES.put("trapdoor", List.of(orientable));
        return this;
    }

    public BlockFamilyModelProvider craftingTable(Block craftingTable) {
        BLOCK_TYPES.put("crafting_table", craftingTable);
        return this;
    }

    public BlockFamilyModelProvider bookshelf(Block bookshelf) {
        BLOCK_TYPES.put("bookshelf", bookshelf);
        return this;
    }

    public BlockFamilyModelProvider pressurePlate(Block pressurePlate) {
        BLOCK_TYPES.put("pressure_plate", pressurePlate);
        return this;
    }

    public BlockFamilyModelProvider weightedPressurePlate(Block pressurePlate) {
        BLOCK_TYPES.put("weighted_pressure_plate", pressurePlate);
        return this;
    }

    public BlockFamilyModelProvider button(Block button) {
        BLOCK_TYPES.put("button", button);
        return this;
    }

    public BlockFamilyModelProvider sign(Block sign, Block wallSign) {
        BLOCK_TYPES.put("sign", sign);
        BLOCK_TYPES.put("wall_sign", wallSign);
        return this;
    }

    public BlockFamilyModelProvider hangingSign(Block hangingSign, Block wallHangingSign) {
        BLOCK_TYPES.put("hanging_sign", hangingSign);
        BLOCK_TYPES.put("wall_hanging_sign", wallHangingSign);
        return this;
    }

    public BlockFamilyModelProvider ladder(Block ladder) {
        BLOCK_TYPES.put("ladder", ladder);
        return this;
    }

    public BlockFamilyModelProvider concrete(Block concretePowder, Block concrete) {
        BLOCK_TYPES.put("concrete_powder", concretePowder);
        BLOCK_TYPES.put("concrete", concrete);
        return this;
    }

    public BlockFamilyModelProvider terracotta(Block terracotta, Block glazedTerracotta) {
        BLOCK_TYPES.put("terracotta", terracotta);
        BLOCK_TYPES.put("glazed_terracotta", glazedTerracotta);
        return this;
    }

    public BlockFamilyModelProvider wool(Block wool, Block carpet) {
        BLOCK_TYPES.put("wool", wool);
        BLOCK_TYPES.put("carpet", carpet);
        return this;
    }

    public BlockFamilyModelProvider stainedGlass(Block stainedGlass, Block stainedGlassPane) {
        BLOCK_TYPES.put("stained_glass", stainedGlass);
        BLOCK_TYPES.put("stained_glass_pane", stainedGlassPane);
        return this;
    }

    public void build() {
        for (String type : BLOCK_TYPES.keySet()) {
            Block block = BLOCK_TYPES.get(type);
            PROVIDERS.get(type).makeModel(block, BuiltInRegistries.BLOCK.getKey(block));
        }
        PROVIDERS.clear();
        BLOCK_TYPES.clear();
        EXTRA_PROPERTIES.clear();
    }

    /// Adds all default {@linkplain FamilyModelEntry family model entries} to the providers map.
    /// New providers can be added using the {@link #addProviders()} method below.
    protected final void addDefaultProviders() {
        // Common blocks
        PROVIDERS.put("full_block", (block, identifier) -> this.stateProvider.simpleBlock(block));
        PROVIDERS.put("stairs", (block, identifier) -> this.stateProvider.stairsBlock((StairBlock) block, this.texture));
        PROVIDERS.put("slab", (block, identifier) -> this.stateProvider.slabBlock((SlabBlock) block, this.texture, this.texture));
        PROVIDERS.put("double_slab", (block, identifier) -> this.stateProvider.doubleSlab(block, this.texture, (ResourceLocation) EXTRA_PROPERTIES.get("double_slab").getFirst()));
        PROVIDERS.put("wall", (block, identifier) -> this.stateProvider.wallBlock(block, this.texture));
        PROVIDERS.put("fence", (block, identifier) -> this.stateProvider.fenceBlock(block, this.texture));
        PROVIDERS.put("fence_gate", (block, identifier) -> this.stateProvider.fenceGateBlock((FenceGateBlock) block, this.texture));
        PROVIDERS.put("door", (block, identifier) -> this.stateProvider.doorBlock((DoorBlock) block,
                this.mod("block/" + this.materialName + "_door_bottom"),
                this.mod("block/" + this.materialName + "_door_top")));
        PROVIDERS.put("trapdoor", (block, identifier) -> this.stateProvider.trapdoorBlock((TrapDoorBlock) block,
                this.mod("block/" + this.materialName + "_trapdoor"),
                (Boolean) EXTRA_PROPERTIES.get("trapdoor").getFirst()));
        PROVIDERS.put("pressure_plate", (block, identifier) -> this.stateProvider.pressurePlateBlock((PressurePlateBlock) block, this.texture));
        PROVIDERS.put("weighted_pressure_plate", (block, identifier) -> this.stateProvider.weightedPressurePlateBlock(block, this.texture));
        PROVIDERS.put("button", (block, identifier) -> this.stateProvider.buttonBlock(block, this.texture));
        PROVIDERS.put("plant", (block, identifier) -> this.stateProvider.simpleBlock(block, this.models().cross(identifier.getPath(), getBlockTexture(block))));
        PROVIDERS.put("potted_plant", (block, identifier) -> this.stateProvider.simpleBlock(block, this.models().withExistingParent(identifier.getPath(), this.mcLoc("block/flower_pot_cross")).texture("plant", getBlockTexture(BLOCK_TYPES.get("plant")))));

        // Wood blocks
        PROVIDERS.put("planks", (block, identifier) -> this.stateProvider.simpleBlock(block));
        PROVIDERS.put("wood", (block, identifier) -> this.stateProvider.axisBlock((RotatedPillarBlock) block,
                this.mod("block/" + this.materialName + "_log"),
                this.mod("block/" + this.materialName + "_log")));
        PROVIDERS.put("stripped_log", (block, identifier) -> this.stateProvider.logBlock((RotatedPillarBlock) block));
        PROVIDERS.put("stripped_wood", (block, identifier) -> this.stateProvider.axisBlock((RotatedPillarBlock) block,
                this.mod("block/stripped_" + this.materialName + "_log"),
                this.mod("block/stripped_" + this.materialName + "_log")));
        PROVIDERS.put("leaves", (block, identifier) -> this.stateProvider.simpleBlock(block, this.models().leaves(identifier.getPath(), getBlockTexture(block))));
        PROVIDERS.put("log", (block, identifier) -> this.stateProvider.logBlock((RotatedPillarBlock) block));
        PROVIDERS.put("ladder", (block, identifier) -> this.stateProvider.ladder(block));
        PROVIDERS.put("crafting_table", (block, identifier) -> this.stateProvider.craftingTable(block, this.materialName));
        PROVIDERS.put("bookshelf", (block, identifier) -> this.stateProvider.bookshelf(block, this.materialName));
        FamilyModelEntry sign = (block, identifier) -> this.stateProvider.simpleBlock(block, this.models().sign(identifier.getPath(), this.texture));
        PROVIDERS.put("sign", sign);
        PROVIDERS.put("wall_sign", sign);
        PROVIDERS.put("hanging_sign", sign);
        PROVIDERS.put("wall_hanging_sign", sign);

        // Colored blocks
        FamilyModelEntry fromTexture = (block, identifier) -> this.fromTexture(block);
        PROVIDERS.put("concrete", fromTexture);
        PROVIDERS.put("concrete_powder", fromTexture);
        PROVIDERS.put("terracotta", fromTexture);
        PROVIDERS.put("glazed_terracotta", (block, identifier) -> this.stateProvider.horizontalBlock(block,
                getBlockTexture(block), getBlockTexture(block), getBlockTexture(block)));
        PROVIDERS.put("wool", fromTexture);
        PROVIDERS.put("carpet", ((block, identifier) -> this.stateProvider.simpleBlock(block, this.models().carpet(identifier.getPath(),
                getBlockTexture(BLOCK_TYPES.get("wool"))))));
        PROVIDERS.put("stained_glass", fromTexture);
        PROVIDERS.put("stained_glass_pane", (block, identifier) -> this.stateProvider.paneBlock((IronBarsBlock) block,
                getBlockTexture(BLOCK_TYPES.get("stained_glass")),
                this.mod("block/" + identifier.getPath() + "_top")));

        this.addProviders();
    }

    /// Adds custom providers to the provider entries map. This is usually paired with the {@link #add(String, Block) add()} methods.
    public void addProviders() {}

    /// Makes a block model based on the block's registry name.
    /// @param block The block to generate.
    public void fromTexture(Block block) {
        this.stateProvider.simpleBlock(block, this.models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath(), getBlockTexture(block)));
    }

    /// Redirect call to the block model provider.
    public BlockModelProvider models() {
        return this.stateProvider.models();
    }

    /// Makes a resource location using the current mod's identifier.
    /// @param name The resource location path.
    protected ResourceLocation mod(String name) {
        return ResourceLocation.fromNamespaceAndPath(this.modID, name);
    }

    /// Makes a resource location using the default "`minecraft`" namespace. Copied from {@link net.neoforged.neoforge.client.model.generators.BlockStateProvider#mcLoc(String) BlockStateProvider.mcLoc()}.
    /// @param name The resource location path.
    protected ResourceLocation mcLoc(String name) {
        return ResourceLocation.withDefaultNamespace(name);
    }
}