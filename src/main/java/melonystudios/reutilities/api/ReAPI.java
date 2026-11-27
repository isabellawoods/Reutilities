package melonystudios.reutilities.api;

import melonystudios.behaviorapi.BehaviorDataComponents;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.configuration.ItemBehaviorConfig;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.boat.BoatType;
import melonystudios.reutilities.api.color.Recolor;
import melonystudios.reutilities.api.outfit.OutfitDefinition;
import melonystudios.reutilities.compat.femalegender.BreastArmorData;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.component.custom.ComponentOutfit;
import melonystudios.reutilities.component.custom.TooltipStyle;
import melonystudios.reutilities.entity.attachment.FullBodyOutfit;
import melonystudios.reutilities.entity.attachment.WornRecape;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.neoforge.capabilities.EntityCapability;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static melonystudios.reutilities.util.ReCommonConstants.*;
import static net.minecraft.client.renderer.item.ItemProperties.register;

/// ***Reutilities'*** **API** class, used by my mods to add new boats and signs, register item overrides, get light emission values, etc.
@SuppressWarnings("deprecation")
public class ReAPI {
    /// The `reutilities:full_body_outfit` entity capability, used to store full-body outfits on entities.
    public static final EntityCapability<FullBodyOutfit, Void> OUTFIT_CAPABILITY = EntityCapability.createVoid(Reutilities.reutilities("full_body_outfit"), FullBodyOutfit.class);
    /// The `reutilities:cape` entity capability, used to store capes on entities.
    public static final EntityCapability<WornRecape, Void> CAPE_CAPABILITY = EntityCapability.createVoid(Reutilities.reutilities("cape"), WornRecape.class);

    /// Adds various boat types to *Reutilities'* boat map.
    ///
    /// This method should be called during the [common setup event][net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent].
    /// @param types The boat types to be added.
    public static void addBoats(BoatType... types) {
        for (BoatType type : types) addBoat(type);
    }

    /// Adds a boat and chest boat to *Reutilities'* boat map.
    ///
    /// This method should be called during the [common setup event][net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent].
    /// @param type The boat type to add.
    public static void addBoat(BoatType type) {
        BOATS.put(type.woodType(), type);
    }

    /// Adds various recolors to (*Reutilities* color) to the mod's colors map.
    ///
    /// This method should be called during the [common setup event][net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent].
    /// @param colors The recolors to be added.
    public static void addRecolors(Recolor... colors) {
        for (Recolor color : colors) COLORS.put(color.colorLocation(), color);
    }

    /// Defines how an item, usually a chestplate, behaves when the wearer has breasts.
    ///
    /// This method should be called during the [common setup event][net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent].
    /// @param item The item to add the `wildfire_gender:gender_armor` capability, usually a chestplate.
    /// @param physicsResistance The physical resistance provided to the wearer's breasts when calculating physics.
    /// @param tightness How "tight" the armor is. Breasts will appear up to **15% smaller** at full tightness.
    /// @param alwaysHidesBreasts Hides the wearer's breasts regardless of the **"Show Breasts in Armor"** option.
    /// @see BreastArmorData
    public static void defineBreastArmorData(Item item, float physicsResistance, float tightness, boolean alwaysHidesBreasts) {
        defineBreastArmorData(item, physicsResistance, tightness, alwaysHidesBreasts, null);
    }

    /// Defines how an item, usually a chestplate behaves when the wearer has breasts.
    ///
    /// This method should be called during the [common setup event][net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent].
    /// @param item The item to add the `wildfire_gender:gender_armor` capability, usually a chestplate.
    /// @param physicsResistance The physical resistance provided to the wearer's breasts when calculating physics.
    /// @param tightness How "tight" the armor is. Breasts will appear up to **15% smaller** at full tightness.
    /// @param alwaysHidesBreasts Hides the wearer's breasts regardless of the **"Show Breasts in Armor"** option.
    /// @param armorStandsCopySettings Whether armor stands show the chestplate's breasts when worn. Settings this to `null` uses the default logic.
    /// @see BreastArmorData
    public static void defineBreastArmorData(Item item, float physicsResistance, float tightness, boolean alwaysHidesBreasts, Boolean armorStandsCopySettings) {
        BREAST_ARMOR_CAPABILITIES.put(item, new BreastArmorData(physicsResistance, tightness, alwaysHidesBreasts, armorStandsCopySettings));
    }

    /// Adds signs to the valid list of blocks of the [`SIGN`][melonystudios.reutilities.blockentity.ReBlockEntities#SIGN] block entity.
    ///
    /// This method should be called during the [common setup event][net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent].
    /// @param signs An array of signs to add.
    /// @see melonystudios.reutilities.block.custom.ReStandingSignBlock ReStandingSignBlock
    /// @see melonystudios.reutilities.block.custom.ReWallSignBlock ReWallSignBlock
    /// @deprecated Use *NeoForge*'s [`BlockEntityTypeAddBlocksEvent`][net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent] instead.
   @Deprecated(since = "1.6.0", forRemoval = true)
    public static void addSigns(Block... signs) {
        SIGNS.addAll(List.of(signs));
    }

    /// Adds hanging signs to the valid list of blocks of the [`HANGING_SIGN`][melonystudios.reutilities.blockentity.ReBlockEntities#HANGING_SIGN] block entity.
    ///
    /// This method should be called during the [common setup event][net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent].
    /// @param hangingSigns An array of hanging signs to add.
    /// @see melonystudios.reutilities.block.custom.ReCeilingHangingSignBlock ReCeilingHangingSignBlock
    /// @see melonystudios.reutilities.block.custom.ReWallHangingSignBlock ReWallHangingSignBlock
    /// @deprecated Use *NeoForge*'s [`BlockEntityTypeAddBlocksEvent`][net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent] instead.
    @Deprecated(since = "1.6.0", forRemoval = true)
    public static void addHangingsSigns(Block... hangingSigns) {
        HANGING_SIGNS.addAll(List.of(hangingSigns));
    }

    /// Adds a block to the flammability map.
    ///
    /// This method should be called during the [common setup event][net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent].
    /// @param block The block.
    /// @param encouragement The chance of fire wanting to spread to this block.
    /// @param flammability The chance that this block will actually burn out when on fire.
    public static void flammable(Block block, int encouragement, int flammability) {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(block, encouragement, flammability);
    }

    /// Whether a tooltip can be displayed on an item, or is hidden by the [`reutilities:hide_components`][ReDataComponents#HIDE_COMPONENTS] component.
    ///
    /// Recommended to use [#shouldDisplay(DataComponentHolder, ResourceLocation)] instead.
    /// @param stack An item stack.
    /// @param name An identifier of the tooltip name, like `reutilities:item_components`.
    public static boolean shouldDisplay(ItemStack stack, ResourceLocation name) {
        return shouldDisplay((DataComponentHolder) stack, name);
    }

    /// Whether a tooltip can be displayed on an item, or is hidden by the [`reutilities:hide_components`][ReDataComponents#HIDE_COMPONENTS] component.
    /// @param holder A data component holder, such as an item stack.
    /// @param name An identifier of the tooltip name, like `reutilities:item_components`.
    public static boolean shouldDisplay(DataComponentHolder holder, ResourceLocation name) {
        List<ResourceLocation> hiddenComponents = holder.get(ReDataComponents.HIDE_COMPONENTS.get());
        if (hiddenComponents == null || hiddenComponents.isEmpty()) return true;
        return !hiddenComponents.contains(name);
    }

    /// Whether a tooltip can be displayed on an item, or is hidden by the [`reutilities:hide_components`][ReDataComponents#HIDE_COMPONENTS] component.
    /// @param patch A data component patch, usually taken from an item stack.
    /// @param name An identifier of the tooltip name, like `reutilities:item_components`.
    public static boolean shouldDisplay(DataComponentPatch patch, ResourceLocation name) {
        Optional<? extends List<ResourceLocation>> hiddenComponents = patch.get(ReDataComponents.HIDE_COMPONENTS.get());
        // noinspection OptionalAssignedToNull
        if (hiddenComponents == null || hiddenComponents.isEmpty() || hiddenComponents.get().isEmpty()) return true;
        return !hiddenComponents.get().contains(name);
    }

    /// Gets the block light emitted from a given block, using its location in the world for reference if possible.
    /// @param stack The item stack to get the block item.
    /// @param level *(optional)* The world.
    /// @param pos *(optional)* The location in the world this item is in.
    public static int getEmittedBlockLight(ItemStack stack, @Nullable Level level, @Nullable BlockPos pos) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) return 0;
        var state = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);

        if (level == null || pos == null) {
            return state.apply(blockItem.getBlock().defaultBlockState()).getLightEmission();
        } else {
            return blockItem.getBlock().getLightEmission(state.apply(blockItem.getBlock().defaultBlockState()), level, pos);
        }
    }

    /// Tags all items from a specified namespace that have the [`reutilities:outfit`][ReDataComponents#OUTFIT] component.
    /// @param modID The namespace of the mod.
    /// @param applier A [Consumer] that applies the tag changes to all items.
    public static void tagAllOutfits(String modID, Consumer<ResourceKey<Item>> applier) {
        List<Item> outfittedFits = BuiltInRegistries.ITEM.stream()
                .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(modID))
                .filter(item -> item.getDefaultInstance().has(ReDataComponents.OUTFIT))
                .toList();
        for (Item item : outfittedFits) applier.accept(item.builtInRegistryHolder().key());
    }

    /// Adds all the model properties for a regular bow item (`pull` and `pulling`).
    /// @param bow The bow item.
    public static void addBowProperties(Item bow) {
        register(bow, pullProgress(), (stack, level, livEntity, seed) -> {
            if (livEntity == null) {
                return 0;
            } else {
                return livEntity.getUseItem() != stack ? 0 : (float) (bow.getUseDuration(stack, livEntity) - livEntity.getUseItemRemainingTicks()) / Math.min(stack.getUseDuration(livEntity), 20);
            }
        });
        register(bow, pulling(), (stack, level, livEntity, seed) -> livEntity != null && livEntity.isUsingItem() && livEntity.getUseItem() == stack ? 1 : 0);
    }

    /// Adds all the model properties for a regular crossbow item (`pull`, `pulling`, `charged`, and `firework`).
    /// @param crossbow The crossbow item.
    public static void addCrossbowProperties(Item crossbow) {
        register(crossbow, pullProgress(), (stack, level, livEntity, seed) -> {
            if (livEntity != null) return CrossbowItem.isCharged(stack) ? 0 : (float) (stack.getUseDuration(livEntity) - livEntity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, livEntity);
            return 0;
        });
        register(crossbow, pulling(), (stack, level, livEntity, seed) -> livEntity != null && livEntity.isUsingItem() && livEntity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1 : 0);
        register(crossbow, charged(), (stack, level, livEntity, seed) -> livEntity != null && CrossbowItem.isCharged(stack) ? 1 : 0);
        register(crossbow, fireworkRocketLoaded(), (stack, level, livEntity, seed) -> {
            ChargedProjectiles projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
            return projectiles != null && projectiles.contains(Items.FIREWORK_ROCKET) ? 1 : 0;
        });
    }

    /// Adds all the model properties for a regular shield item (`blocking`).
    /// @param shield The shield item.
    public static void addShieldProperties(Item shield) {
        register(shield, blocking(), (stack, level, livEntity, seed) -> livEntity != null && livEntity.isUsingItem() && livEntity.getUseItem() == stack ? 1 : 0);
    }

    /// Adds the `reutilities:month_check/<month>` property to an item, used by *Back Math*'s carewni sword to check if it's June.
    /// @param item The item.
    /// @param month The month to check for.
    public static void addMonthCheckProperty(Item item, Month month) {
        register(item, monthCheck(month), (stack, level, livEntity, seed) -> LocalDate.now().getMonth() == month ? 1 : 0);
    }

    /// Transforms the provided identifier into a texture path.
    /// @param texture The identifier.
    /// @return A new identifier, with the `textures/` prefix and `.png` suffix added.
    public static ResourceLocation toTexturePath(ResourceLocation texture) {
        return texture.withPath(path -> (path.startsWith("textures/") ? "" : "textures/") + path + (path.endsWith(".png") ? "" : ".png"));
    }

    /// Runs a list of [**item behaviors**][ItemBehavior] from an item stack, based on the provided environment.
    /// @param stack The item stack containing the `behaviorapi:behaviors` component.
    /// @param level The world.
    /// @param entity The entity running the behavior.
    /// @param environment The environment this behavior is being run on. Can be one of `behaviorapi:eating` or `behaviorapi:attacking`.
    /// @see ItemBehavior#runBehavior ItemBehavior.runBehavior()
    public static void runItemBehavior(ItemStack stack, Level level, Entity entity, ResourceLocation environment) {
        Holder<ItemBehaviorConfig> configuration = stack.get(BehaviorDataComponents.BEHAVIORS);

        if (configuration != null && entity instanceof LivingEntity livEntity) {
            configuration.value().behaviors().forEach(behavior -> {
                if (behavior.settings().environment().equals(environment)) behavior.runBehavior(stack, level, livEntity);
            });
        }
    }

    /// Adds an outfit item to a creative tab, specifying its definition and [tooltip style][TooltipStyle].
    /// @param output The creative tab's item consumer.
    /// @param item The item to add.
    /// @param definition The outfit definition to put into the item.
    public static void addOutfit(CreativeModeTab.Output output, ItemLike item, ResourceKey<OutfitDefinition> definition) {
        addOutfit(output, item, definition, TooltipStyle.OUTFIT);
    }

    /// Adds an outfit item to a creative tab, specifying its definition and [tooltip style][TooltipStyle].
    /// @param output The creative tab's item consumer.
    /// @param item The item to add.
    /// @param definition The outfit definition to put into the item.
    /// @param style The tooltip style for the tooltip.
    public static void addOutfit(CreativeModeTab.Output output, ItemLike item, ResourceKey<OutfitDefinition> definition, TooltipStyle style) {
        output.accept(Util.make(new ItemStack(item), stack -> stack.set(ReDataComponents.OUTFIT, ComponentOutfit.of(definition, style))));
    }

    /// Makes a list of [armor layers][ArmorMaterial.Layer] with a single entry using the specified name.
    /// @param name An [identifier][ResourceLocation] of the layer's name.
    public static List<ArmorMaterial.Layer> defaultLayers(ResourceLocation name) {
        return List.of(new ArmorMaterial.Layer(name));
    }

    /// Makes an [EnumMap] with the defense values for a full armor set.
    /// @param helmet The helmet's armor points.
    /// @param chestplate The chestplate's armor points.
    /// @param leggings The leggings' armor points.
    /// @param boots The boots' armor points.
    public static EnumMap<ArmorItem.Type, Integer> defenceMap(int helmet, int chestplate, int leggings, int boots) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.HELMET, helmet);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.BOOTS, boots);
        return map;
    }

    /// Makes an [EnumMap] with the defense values for a full armor set, including the body armor.
    /// @param helmet The helmet's armor points.
    /// @param chestplate The chestplate's armor points.
    /// @param leggings The leggings' armor points.
    /// @param boots The boots' armor points.
    /// @param body The body's armor points.
    public static EnumMap<ArmorItem.Type, Integer> defenceMap(int helmet, int chestplate, int leggings, int boots, int body) {
        EnumMap<ArmorItem.Type, Integer> map = defenceMap(helmet, chestplate, leggings, boots);
        map.put(ArmorItem.Type.BODY, body);
        return map;
    }
}
