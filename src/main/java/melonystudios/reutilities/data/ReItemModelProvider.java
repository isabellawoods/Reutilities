package melonystudios.reutilities.data;

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
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;

import static melonystudios.reutilities.util.Reconstants.*;

public abstract class ReItemModelProvider extends ItemModelProvider {
    public static LinkedHashMap<ResourceKey<TrimMaterial>, Float> TRIM_MATERIALS = new LinkedHashMap<>();
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

    protected final ModelFile generated = this.getExistingFile(this.mcLoc("item/generated"));
    protected final ModelFile handheld = this.getExistingFile(this.mcLoc("item/handheld"));
    protected final ModelFile handheld32x = this.getExistingFile(Reutilities.reutilities("item/handheld_32x"));
    protected final ModelFile spyglassInHand = this.getExistingFile(Reutilities.reutilities("item/template_spyglass_in_hand"));

    public ReItemModelProvider(PackOutput output, String modID, ExistingFileHelper fileHelper) {
        super(output, modID, fileHelper);
    }

    public void standard(String name) {
        this.standard(this.generated, name);
    }

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

    public void block(String name) {
        this.withExistingParent(name, this.modLoc("block/" + name));
    }

    public void block(String name, String addition) {
        this.withExistingParent(name, this.modLoc("block/" + name + addition));
    }

    public void glassPane(ModelFile parent, String name) {
        this.getBuilder(name + "_pane").parent(parent).texture("layer0", this.modLoc("block/" + name));
    }

    public void armorSet(ModelFile parent, Item helmet, Item chestplate, Item leggings, Item boots) {
        this.trimmedArmorItem(parent, helmet);
        this.trimmedArmorItem(parent, chestplate);
        this.trimmedArmorItem(parent, leggings);
        this.trimmedArmorItem(parent, boots);
    }

    public void toolSet(ModelFile parent, String material) {
        this.getBuilder(material + "_sword").parent(parent).texture("layer0", this.modLoc("item/" + material + "_sword"));
        this.getBuilder(material + "_pickaxe").parent(parent).texture("layer0", this.modLoc("item/" + material + "_pickaxe"));
        this.getBuilder(material + "_shovel").parent(parent).texture("layer0", this.modLoc("item/" + material + "_shovel"));
        this.getBuilder(material + "_axe").parent(parent).texture("layer0", this.modLoc("item/" + material + "_axe"));
        this.getBuilder(material + "_hoe").parent(parent).texture("layer0", this.modLoc("item/" + material + "_hoe"));
    }

    public void dualWieldedSword(ModelFile parent, ModelFile parent32x, String name) {
        this.getBuilder(name + "_inventory").parent(parent).texture("layer0", this.modLoc("item/" + name + "_inventory"));
        this.getBuilder(name + "_in_hand").parent(parent32x).texture("layer0", this.modLoc("item/" + name));

        this.withExistingParent(name, parent.getLocation()).customLoader(SeparateTransformsModelBuilder::begin)
                .base(nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_in_hand"))))
                .perspective(ItemDisplayContext.GUI, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory"))))
                .perspective(ItemDisplayContext.GROUND, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory"))))
                .perspective(ItemDisplayContext.FIXED, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory")))).end();
    }

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

    public void shield(String name, ResourceLocation shieldTexture, ResourceLocation particleTexture) {
        this.getBuilder(name + "_blocking").parent(this.getExistingFile(Reutilities.reutilities("item/template_shield_blocking"))).texture("shield", shieldTexture).texture("particle", particleTexture);

        this.getBuilder(name).parent(this.getExistingFile(Reutilities.reutilities("item/template_shield"))).texture("shield", shieldTexture).texture("particle", particleTexture)
                .override().predicate(blocking(), 1).model(this.getExistingFile(this.modLoc("item/" + name + "_blocking"))).end();
    }

    public void spyglass(ModelFile baseParent, String name) {
        this.getBuilder(name + "_inventory").parent(baseParent).texture("layer0", this.modLoc("item/" + name));
        this.getBuilder(name + "_in_hand").parent(this.spyglassInHand).texture("spyglass", this.modLoc("item/" + name + "_model"));

        this.withExistingParent(name, baseParent.getLocation()).customLoader(SeparateTransformsModelBuilder::begin)
                .base(this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_in_hand"))))
                .perspective(ItemDisplayContext.GUI, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory"))))
                .perspective(ItemDisplayContext.GROUND, this.nested().parent(this.getExistingFile(this.modLoc("item/" + name + "_inventory")))).end();
    }

    /// Copied from [Kaupenjoe's NeoForge 1.21 tutorials](https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X/blob/75229679f5195c7c726c5be5dc939b2f4a0baf9f/src/main/java/net/kaupenjoe/tutorialmod/datagen/ModItemModelProvider.java).
    /// @author El_Redstoniano
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
                String trimPath = "trims/items/" + armorType + "_trim_" + material.location().getPath();
                String currentTrimName = itemPath + "_" + material.location().getPath() + "_trim";
                ResourceLocation itemLocation = ResourceLocation.parse(itemPath);
                ResourceLocation trimLocation = ResourceLocation.parse(trimPath); // minecraft namespace
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
}
