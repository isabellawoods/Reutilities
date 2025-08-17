package melonystudios.reutilities.api;

import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.util.Reconstants;
import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static melonystudios.reutilities.util.Reconstants.*;
import static net.minecraft.client.renderer.item.ItemProperties.register;

/// ***Reutilities'*** **API** class, used by my mods to add new boats and signs, register item overrides, get light emission values, etc.
public class ReAPI {
    /// Adds a boat and chest boat to *Reutilities*' boat map.
    /// @param type The boat type to add.
    public static void addBoat(BoatType type) {
        Reconstants.BOATS.put(type.woodType().toString(), type);
    }

    /// Adds a boat and chest boat to *Reutilities*' boat map.
    /// @param boat The boat item.
    /// @param chestBoat The boat with chest item.
    /// @param woodType A resource location of the boat's wood type, like `minecraft:oak`.
    public static void addBoat(Supplier<Item> boat, Supplier<Item> chestBoat, ResourceLocation woodType) {
        Reconstants.BOATS.put(woodType.toString(), new BoatType(boat, chestBoat, woodType));
    }

    /// Adds signs to the valid list of blocks of the {@link melonystudios.reutilities.blockentity.ReBlockEntities#SIGN SIGN} block entity.
    /// @param signs An array of signs to add.
    public static void addSigns(Block... signs) {
        Reconstants.SIGNS.addAll(List.of(signs));
    }

    /// Adds hanging signs to the valid list of blocks of the {@link melonystudios.reutilities.blockentity.ReBlockEntities#HANGING_SIGN HANGING_SIGN} block entity.
    /// @param hangingSigns An array of hanging signs to add.
    public static void addHangingsSigns(Block... hangingSigns) {
        Reconstants.HANGING_SIGNS.addAll(List.of(hangingSigns));
    }

    /// Adds a block to the flammability map.
    /// @param block The block.
    /// @param encouragement The chance of fire wanting to spread to this block.
    /// @param flammability The chance that this block will actually burn out when on fire.
    public static void flammable(Block block, int encouragement, int flammability) {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(block, encouragement, flammability);
    }

    /// Gets the brightness that should be applied to an item, based on its presence in the {@link ReItemTags#EMISSIVE_LIGHTING #c:emissive_lighting} item tag,
    /// its {@link ReDataComponents#LIGHT_EMISSION reutilities:light_emission} component, and the block item's brightness.
    ///
    /// {@linkplain #getSkylight(Level, BlockPos, int) Skylight} is taken into consideration only when the world and position aren't `null`.
    /// @param stack The item stack to make emissive.
    /// @param lightEmission The old light value of this item, usually the `packedLight` parameter.
    /// @param world *(optional)* The world.
    /// @param pos *(optional)* The location in the world this item is in.
    /// @param applySkylight Whether skylight should be considered when calculating the light.
    public static int getLightOutputFromItem(ItemStack stack, int lightEmission, Level world, BlockPos pos, boolean applySkylight) {
        int skylight = getSkylight(world, pos, lightEmission);
        int light = applySkylight ? skylight : 15;
        int blockLight = getBlockLight(stack, world, pos);

        var emissionComponent = stack.get(ReDataComponents.LIGHT_EMISSION);
        if (emissionComponent != null) {
            return LightTexture.pack(emissionComponent, light);
        } else if (stack.is(ReItemTags.EMISSIVE_LIGHTING)) {
            return LightTexture.pack(15, light);
        } else if (ReConfigs.LIGHT_EMITTING_EMISSIVES.get() && blockLight > 0) {
            return LightTexture.pack(blockLight, light);
        }
        return lightEmission;
    }

    /// Gets the skylight at a given point in the world, or fully lit if the world doesn't exist.
    /// @param world *(optional)* The world.
    /// @param pos The location in the world this item is in.
    /// @param lightEmission The old light value for this item, usually the `packedLight` parameter.
    public static int getSkylight(Level world, BlockPos pos, int lightEmission) {
        return world == null || pos == null ? LightTexture.sky(lightEmission) : world.getRawBrightness(pos, LightTexture.sky(lightEmission));
    }

    /// Gets the block light for a given block, using its location in the world for reference if possible.
    /// @param stack The item stack to get the block item.
    /// @param world *(optional)* The world.
    /// @param pos The location in the world this item is in.
    public static int getBlockLight(ItemStack stack, Level world, BlockPos pos) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) return 0;

        if (world == null || pos == null) {
            return blockItem.getBlock().defaultBlockState().getLightEmission();
        } else {
            return blockItem.getBlock().getLightEmission(blockItem.getBlock().defaultBlockState(), world, pos);
        }
    }

    /// Adds all the model properties for a regular bow item (`pull` and `pulling`).
    /// @param bow The bow item.
    public static void addBowProperties(BowItem bow) {
        register(bow, pullProgress(), (stack, world, livEntity, seed) -> {
            if (livEntity == null) {
                return 0;
            } else {
                return livEntity.getUseItem() != stack ? 0 : (float) (bow.getUseDuration(stack, livEntity) - livEntity.getUseItemRemainingTicks()) / Math.min(bow.getUseDuration(stack, livEntity), 20);
            }
        });
        register(bow, pulling(), (stack, world, livEntity, seed) -> livEntity != null && livEntity.isUsingItem() && livEntity.getUseItem() == stack ? 1 : 0);
    }

    /// Adds all the model properties for a regular crossbow item (`pull`, `pulling`, `charged`, and `firework`).
    /// @param crossbow The crossbow item.
    public static void addCrossbowProperties(Item crossbow) {
        register(crossbow, pullProgress(), (stack, world, livEntity, seed) -> {
            if (livEntity != null) return CrossbowItem.isCharged(stack) ? 0 : (float) (stack.getUseDuration(livEntity) - livEntity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, livEntity);
            return 0;
        });
        register(crossbow, pulling(), (stack, world, livEntity, seed) -> livEntity != null && livEntity.isUsingItem() && livEntity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1 : 0);
        register(crossbow, charged(), (stack, world, livEntity, seed) -> livEntity != null && CrossbowItem.isCharged(stack) ? 1 : 0);
        register(crossbow, fireworkRocketLoaded(), (stack, world, livEntity, seed) -> {
            ChargedProjectiles projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
            return projectiles != null && projectiles.contains(Items.FIREWORK_ROCKET) ? 1 : 0;
        });
    }

    /// Adds all the model properties for a regular shield item (`blocking`).
    /// @param shield The shield item.
    public static void addShieldProperties(Item shield) {
        register(shield, blocking(), (stack, world, livEntity, seed) -> livEntity != null && livEntity.isUsingItem() && livEntity.getUseItem() == stack ? 1 : 0);
    }

    /// Adds the `reutilities:month_check` property to an item, used by *Back Math*'s carewni sword to check if it's June.
    /// @param item The item.
    public static void addMonthCheckProperty(Item item) {
        register(item, monthCheck(), (stack, world, livEntity, seed) -> LocalDate.now().get(ChronoField.MONTH_OF_YEAR));
    }

    /// Makes a list of {@linkplain ArmorMaterial.Layer armor layers} with a single entry using the specified name.
    /// @param name A {@linkplain ResourceLocation resource location} of the layer's name.
    public static List<ArmorMaterial.Layer> defaultLayers(ResourceLocation name) {
        return List.of(new ArmorMaterial.Layer(name));
    }

    /// Makes an {@link EnumMap} with the defense values for an armor set.
    /// @param helmet The helmet's armor points;
    /// @param chestplate The chestplate's armor points;
    /// @param leggings The leggings' armor points;
    /// @param boots The boots' armor points;
    public static Map<ArmorItem.Type, Integer> defenseMap(int helmet, int chestplate, int leggings, int boots) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.HELMET, helmet);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.BOOTS, boots);
        return map;
    }
}
