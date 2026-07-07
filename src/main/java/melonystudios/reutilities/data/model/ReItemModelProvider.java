package melonystudios.reutilities.data.model;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.time.Month;
import java.util.LinkedHashMap;

import static melonystudios.reutilities.util.ReCommonConstants.*;

public abstract class ReItemModelProvider extends ItemModelProvider {
    /// List of all trim materials that armor models will generate with. New trim materials can be added through the class constructor:
    /// ```java
    /// public ExItemModelProvider(PackOutput output, ExistingFileHelper fileHelper) {
    ///     super(output, ExampleMod.MOD_ID, fileHelper);
    ///     TRIM_MATERIALS.put(ExTrimMaterials.RUBY, 0.4F);
    /// }
    /// ```
    public static LinkedHashMap<ResourceKey<TrimMaterial>, Float> TRIM_MATERIALS = new LinkedHashMap<>();
    protected final ModelFile generated = this.getExistingFile(this.mcLoc("item/generated"));
    protected final ModelFile handheld = this.getExistingFile(this.mcLoc("item/handheld"));
    protected final ModelFile handheld32x = this.getExistingFile(Reutilities.reutilities("item/handheld_32x"));
    protected final ModelFile spyglassInHand = this.getExistingFile(Reutilities.reutilities("item/template_spyglass_in_hand"));

    public ReItemModelProvider(PackOutput output, String modID, ExistingFileHelper fileHelper) {
        super(output, modID, fileHelper);
    }

    /// Makes a model for an item.
    /// @param name The item's registry ID, used to locate the texture.
    public void standard(String name) {
        this.standard(this.generated, name);
    }

    /// Makes a model for an item.
    /// @param parent The location of the parent model, usually `item/generated` or `item/handheld`.
    /// @param name The item's registry ID, used to locate the texture.
    public void standard(ModelFile parent, String name) {
        this.getBuilder(name).parent(parent).texture("layer0", this.modLoc("item/" + name));
    }

    public void blockItem(String name) {
        this.blockItem(this.generated, name);
    }

    public void blockItem(ModelFile parent, String name) {
        this.getBuilder(name).parent(parent).texture("layer0", this.modLoc("block/" + name));
    }

    public void blockItem(ModelFile parent, String name, String addition) {
        this.getBuilder(name).parent(parent).texture("layer0", this.modLoc("block/" + name + addition));
    }

    /// Makes a model for a block, using the `models/block` folder as the source.
    /// @param name The block's registry ID and model file name.
    public void block(String name) {
        this.withExistingParent(name, this.modLoc("block/" + name));
    }

    /// Makes a model for a block, using the `models/block` folder as the source.
    /// @param name The block's registry ID and model file name.
    /// @param addition An extra string used to find the model, for when the model name isn't exactly the item's name.
    public void block(String name, String addition) {
        this.withExistingParent(name, this.modLoc("block/" + name + addition));
    }

    /// Makes the model for a glass pane, using the block texture of the original glass block.
    /// @param parent The location of the parent model, usually {@link #generated minecraft:item/generated} or {@link #handheld minecraft:item/handheld}.
    /// @param name The name of the glass block texture. The `_pane` suffix is added by this method.
    public void glassPane(ModelFile parent, String name) {
        this.getBuilder(name + "_pane").parent(parent).texture("layer0", this.modLoc("block/" + name));
    }

    /// Makes the models for a whole armor set, with trims.
    ///
    /// New trims can be added using the {@link #TRIM_MATERIALS} map.
    /// @param parent The location of the parent model, usually {@link #generated minecraft:item/generated}.
    /// @param helmet The helmet item.
    /// @param chestplate The chestplate item.
    /// @param leggings The leggings item.
    /// @param boots The boots item.
    public void armorSet(ModelFile parent, Item helmet, Item chestplate, Item leggings, Item boots) {
        this.trimmedArmorItem(parent, helmet);
        this.trimmedArmorItem(parent, chestplate);
        this.trimmedArmorItem(parent, leggings);
        this.trimmedArmorItem(parent, boots);
    }

    /// Makes the models for a whole tool set (sword, pickaxe, shovel, axe and hoe).
    /// @param parent The location of the parent model, usually {@link #generated minecraft:item/generated}.
    /// @param material The base material name of the tool set, such as `diamond` or `numeed`.
    public void toolSet(ModelFile parent, String material) {
        this.getBuilder(material + "_sword").parent(parent).texture("layer0", this.modLoc("item/" + material + "_sword"));
        this.getBuilder(material + "_pickaxe").parent(parent).texture("layer0", this.modLoc("item/" + material + "_pickaxe"));
        this.getBuilder(material + "_shovel").parent(parent).texture("layer0", this.modLoc("item/" + material + "_shovel"));
        this.getBuilder(material + "_axe").parent(parent).texture("layer0", this.modLoc("item/" + material + "_axe"));
        this.getBuilder(material + "_hoe").parent(parent).texture("layer0", this.modLoc("item/" + material + "_hoe"));
    }

    /// Makes the model for a dual-wielded sword, using two separate models for the inventory and when held.
    /// @param parent The location of the inventory parent model, usually {@link #handheld minecraft:item/handheld}.
    /// @param parent32x The location of the held parent model, usually {@link #handheld32x reutilities:item/handheld_32x}.
    /// @param name The ID of the sword item, without the namespace.
    public void dualWieldedSword(ModelFile parent, ModelFile parent32x, String name) {
        this.getBuilder(name + "_inventory").parent(parent).texture("layer0", this.modLoc("item/" + name + "_inventory"));
        this.getBuilder(name + "_in_hand").parent(parent32x).texture("layer0", this.modLoc("item/" + name));

        this.withExistingParent(name, parent.getLocation()).customLoader(SeparateTransformsModelBuilder::begin)
                .base(nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_in_hand"))))
                .perspective(ItemDisplayContext.GUI, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory"))))
                .perspective(ItemDisplayContext.GROUND, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory"))))
                .perspective(ItemDisplayContext.FIXED, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory")))).end();
    }

    /// Makes the model for a dual-wielded sword which has a pride flag texture during the specified month,
    /// using two separate models for the inventory and when held.
    /// @param parent The location of the inventory parent model, usually {@link #handheld minecraft:item/handheld}.
    /// @param parent32x The location of the held parent model, usually {@link #handheld32x reutilities:item/handheld_32x}.
    /// @param name The ID of the sword item, without the namespace.
    /// @param prideMonth Which month to use the pride texture in.
    public void prideDualWieldedSword(ModelFile parent, ModelFile parent32x, String name, int prideMonth) {
        this.getBuilder(name + "_inventory").parent(parent).texture("layer0", this.modLoc("item/" + name + "_inventory"));
        this.getBuilder(name + "_in_hand").parent(parent32x).texture("layer0", this.modLoc("item/" + name));
        this.getBuilder(name + "_pride_inventory").parent(parent).texture("layer0", this.modLoc("item/" + name + "_pride_inventory"));
        this.getBuilder(name + "_pride_in_hand").parent(parent32x).texture("layer0", this.modLoc("item/" + name + "_pride"));

        this.withExistingParent(name + "_pride", parent.getLocation()).customLoader(SeparateTransformsModelBuilder::begin)
                .base(this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_pride_in_hand"))))
                .perspective(ItemDisplayContext.GUI, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_pride_inventory"))))
                .perspective(ItemDisplayContext.GROUND, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_pride_inventory"))))
                .perspective(ItemDisplayContext.FIXED, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_pride_inventory")))).end();

        this.withExistingParent(name, parent.getLocation()).customLoader(SeparateTransformsModelBuilder::begin)
                .base(this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_in_hand"))))
                .perspective(ItemDisplayContext.GUI, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory"))))
                .perspective(ItemDisplayContext.GROUND, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory"))))
                .perspective(ItemDisplayContext.FIXED, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory")))).end()
                .override().predicate(monthCheck(Month.of(prideMonth)), 1).model(this.getExistingFile(this.modLoc("item/" + name + "_pride")));
    }

    /// Makes the model for a vanilla bow, using the `reutilities:item/template_bow` parent.
    /// @param name The ID of the bow, without the namespace.
    public void bow(String name) {
        ModelFile templateBow = this.getExistingFile(Reutilities.reutilities("item/template_bow"));
        this.standard(templateBow, name + "_pulling_0");
        this.standard(templateBow, name + "_pulling_1");
        this.standard(templateBow, name + "_pulling_2");

        this.getBuilder(name).parent(templateBow).texture("layer0", this.modLoc("item/" + name))
                .override().predicate(pulling(), 1).model(this.getExistingFile(this.modLoc("item/" + name + "_pulling_0"))).end()
                .override().predicate(pulling(), 1).predicate(pullProgress(), 0.65F).model(this.getExistingFile(this.modLoc("item/" + name + "_pulling_1"))).end()
                .override().predicate(pulling(), 1).predicate(pullProgress(), 0.9F).model(this.getExistingFile(this.modLoc("item/" + name + "_pulling_2"))).end();
    }

    /// Makes the model for a vanilla crossbow, using the `reutilities:item/template_crossbow` parent.
    /// @param name The ID of the crossbow, without the namespace.
    public void crossbow(String name) {
        ModelFile templateCrossbow = this.getExistingFile(Reutilities.reutilities("item/template_crossbow"));
        this.standard(templateCrossbow, name + "_pulling_0");
        this.standard(templateCrossbow, name + "_pulling_1");
        this.standard(templateCrossbow, name + "_pulling_2");
        this.standard(templateCrossbow, name + "_arrow");
        this.standard(templateCrossbow, name + "_firework");

        this.getBuilder(name).parent(templateCrossbow).texture("layer0", this.modLoc("item/" + name + "_standby"))
                .override().predicate(pulling(), 1).model(this.getExistingFile(this.modLoc("item/" + name + "_pulling_0"))).end()
                .override().predicate(pulling(), 1).predicate(pullProgress(), 0.58F).model(this.getExistingFile(this.modLoc("item/" + name + "_pulling_1"))).end()
                .override().predicate(pulling(), 1).predicate(pullProgress(), 1).model(this.getExistingFile(this.modLoc("item/" + name + "_pulling_2"))).end()
                .override().predicate(charged(), 1).model(this.getExistingFile(this.modLoc("item/" + name + "_arrow"))).end()
                .override().predicate(charged(), 1).predicate(fireworkRocketLoaded(), 1).model(this.getExistingFile(this.modLoc("item/" + name + "_arrow"))).end();
    }

    /// Makes the model for a shield, using the `reutilities:item/template_shield<_blocking>` parent.
    ///
    /// Since this uses models for rendering the shield, its positioning is a bit inaccurate to the original.
    /// @param name The ID of the shield, without the namespace.
    /// @param shieldTexture The location of the shield texture, within the `item/` or `block/` folder.
    /// @param particleTexture The location of the breaking particle texture, within the `item/` or `block/` folder.
    public void shield(String name, ResourceLocation shieldTexture, ResourceLocation particleTexture) {
        this.getBuilder(name + "_blocking").parent(this.getExistingFile(Reutilities.reutilities("item/template_shield_blocking"))).texture("shield", shieldTexture).texture("particle", particleTexture);

        this.getBuilder(name).parent(this.getExistingFile(Reutilities.reutilities("item/template_shield"))).texture("shield", shieldTexture).texture("particle", particleTexture)
                .override().predicate(blocking(), 1).model(this.getExistingFile(this.modLoc("item/" + name + "_blocking"))).end();
    }

    /// Makes the model for a spyglass, using the {@link #spyglassInHand reutilities:item/template_spyglass_in_hand}.
    ///
    /// Since this uses models for rendering the spyglass, its positioning is a bit inaccurate to the original.
    /// @param baseParent The location of the inventory model, usually {@link #generated minecraft:item/generated}.
    /// @param name The ID of the spyglass, without the namespace.
    public void spyglass(ModelFile baseParent, String name) {
        this.getBuilder(name + "_inventory").parent(baseParent).texture("layer0", this.modLoc("item/" + name));
        this.getBuilder(name + "_in_hand").parent(this.spyglassInHand).texture("spyglass", this.modLoc("item/" + name + "_model"));

        this.withExistingParent(name, baseParent.getLocation()).customLoader(SeparateTransformsModelBuilder::begin)
                .base(this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_in_hand"))))
                .perspective(ItemDisplayContext.GUI, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory"))))
                .perspective(ItemDisplayContext.GROUND, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory")))).end();
    }

    /// Creates the model for a *NeoForge* filled bucket.
    /// @param parent The parent model file. Usually, this is set to {@link #generated minecraft:item/generated}.
    /// @param name The item model file name.
    /// @param fluid The {@link Fluid} contained in this bucket.
    /// @param applyTint Whether to apply a colored tint to the bucket contents. Defaults to `null`.
    /// @param gaseous Whether this fluid is considered as gas, thus flipping the model upside-down. Defaults to `null`. Requires that the fluid itself is gaseous.
    public void filledBucket(ModelFile parent, String name, Fluid fluid, Boolean applyTint, Boolean gaseous) {
        this.getBuilder(name).parent(parent).customLoader(DynamicFluidContainerModelBuilder::begin).fluid(fluid).applyTint(applyTint).flipGas(gaseous).end();
    }

    /// Copied from [Kaupenjoe's *NeoForge* 1.21 tutorials](https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X/blob/75229679f5195c7c726c5be5dc939b2f4a0baf9f/src/main/java/net/kaupenjoe/tutorialmod/datagen/ModItemModelProvider.java).
    ///
    /// New trims can be added using the {@link #TRIM_MATERIALS} map.
    /// @param parent The parent model file. Usually, this is set to {@link #generated minecraft:item/generated}.
    /// @param item The armor item to create the (trimmed) item models for.
    /// @author El_Redstoniano, Kaupenjoe
    public void trimmedArmorItem(ModelFile parent, Item item) {
        if (item instanceof ArmorItem armorItem) {
            TRIM_MATERIALS.forEach((material, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String itemPath = armorItem.toString();
                String currentTrimName = itemPath + "_" + material.location().getPath() + "_trim";
                ResourceLocation itemLocation = ResourceLocation.parse(itemPath);
                // fix custom trim materials having wrong location ~isa 09-09-25
                ResourceLocation trimLocation = ResourceLocation.fromNamespaceAndPath(material.location().getNamespace(), "trims/items/" + armorType + "_trim_" + material.location().getPath());
                ResourceLocation currentTrimLocation = ResourceLocation.parse(currentTrimName);

                // This is used for making the existing file helper acknowledge that this texture exist, so this will avoid an IllegalArgumentException.
                this.existingFileHelper.trackGenerated(trimLocation, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armor item files
                this.getBuilder(currentTrimName).parent(parent)
                        .texture("layer0", itemLocation.getNamespace() + ":item/" + itemLocation.getPath())
                        .texture("layer1", trimLocation);

                // Non-trimmed armor item file (normal variant)
                this.withExistingParent(BuiltInRegistries.ITEM.getKey(item).getPath(), parent.getLocation()).override()
                        .model(new ModelFile.UncheckedModelFile(currentTrimLocation.getNamespace()  + ":item/" + currentTrimLocation.getPath()))
                        .predicate(trimType(), trimValue).end()
                        .texture("layer0", this.modLoc("item/" + BuiltInRegistries.ITEM.getKey(item).getPath()));
            });
        }
    }

    static {
        TRIM_MATERIALS.put(TrimMaterials.QUARTZ, 0.1F);
        TRIM_MATERIALS.put(TrimMaterials.IRON, 0.2F);
        TRIM_MATERIALS.put(TrimMaterials.NETHERITE, 0.3F);
        TRIM_MATERIALS.put(TrimMaterials.REDSTONE, 0.4F);
        TRIM_MATERIALS.put(TrimMaterials.COPPER, 0.5F);
        TRIM_MATERIALS.put(TrimMaterials.GOLD, 0.6F);
        TRIM_MATERIALS.put(TrimMaterials.EMERALD, 0.7F);
        TRIM_MATERIALS.put(TrimMaterials.DIAMOND, 0.8F);
        TRIM_MATERIALS.put(TrimMaterials.LAPIS, 0.9F);
        TRIM_MATERIALS.put(TrimMaterials.AMETHYST, 1F);
    }
}
