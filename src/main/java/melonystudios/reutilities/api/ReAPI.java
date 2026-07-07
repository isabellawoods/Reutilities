package melonystudios.reutilities.api;

import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.block.custom.*;
import melonystudios.reutilities.blockentity.ReBlockEntities;
import melonystudios.reutilities.compat.femalegender.BreastArmorData;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.component.custom.ComponentOutfit;
import melonystudios.reutilities.component.custom.TooltipStyle;
import melonystudios.reutilities.entity.cape.WornRecape;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.option.ReCommonOptions;
import melonystudios.reutilities.entity.outfit.FullBodyOutfit;
import melonystudios.reutilities.util.debug.ReDebuggingFlags;
import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.EntityCapability;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static melonystudios.reutilities.util.ReClientConstants.*;
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
    /// This method should be called during the {@linkplain FMLCommonSetupEvent common setup event}.
    /// @param types The boat types to be added.
    public static void addBoats(BoatType... types) {
        for (BoatType type : types) addBoat(type);
    }

    /// Adds a boat and chest boat to *Reutilities'* boat map.
    ///
    /// This method should be called during the {@linkplain FMLCommonSetupEvent common setup event}.
    /// @param type The boat type to add.
    public static void addBoat(BoatType type) {
        BOATS.put(type.woodType(), type);
    }

    /// Adds various recolors to (*Reutilities* color) to the mod's colors map.
    ///
    /// This method should be called during the {@linkplain FMLCommonSetupEvent common setup event}.
    /// @param colors The recolors to be added.
    public static void addRecolors(Recolor... colors) {
        for (Recolor color : colors) COLORS.put(color.colorLocation(), color);
    }

    /// Defines how an item (usually a chestplate) behaves when the wearer has breasts.
    ///
    /// This method should be called during the {@linkplain FMLCommonSetupEvent common setup event}.
    /// @param item The item to add the `wildfire_gender:gender_armor` capability, usually a chestplate.
    /// @param physicsResistance The physical resistance provided to the wearer's breasts when calculating physics.
    /// @param tightness How "tight" the armor is. Breasts will appear up to **15%** smaller at full tightness.
    /// @param alwaysHidesBreasts Hides the wearer's breasts regardless of the **"Show Breasts in Armor"** option.
    /// @see BreastArmorData
    public static void defineBreastArmorData(Item item, float physicsResistance, float tightness, boolean alwaysHidesBreasts) {
        defineBreastArmorData(item, physicsResistance, tightness, alwaysHidesBreasts, null);
    }

    /// Defines how an item (usually a chestplate) behaves when the wearer has breasts.
    ///
    /// This method should be called during the {@linkplain FMLCommonSetupEvent common setup event}.
    /// @param item The item to add the `wildfire_gender:gender_armor` capability, usually a chestplate.
    /// @param physicsResistance The physical resistance provided to the wearer's breasts when calculating physics.
    /// @param tightness How "tight" the armor is. Breasts will appear up to **15%** smaller at full tightness.
    /// @param alwaysHidesBreasts Hides the wearer's breasts regardless of the **"Show Breasts in Armor"** option.
    /// @param armorStandsCopySettings Whether armor stands show the chestplate's breasts when worn. Settings this to `null` uses the default logic.
    /// @see BreastArmorData
    public static void defineBreastArmorData(Item item, float physicsResistance, float tightness, boolean alwaysHidesBreasts, Boolean armorStandsCopySettings) {
        BREAST_ARMOR_CAPABILITIES.put(item, new BreastArmorData(physicsResistance, tightness, alwaysHidesBreasts, armorStandsCopySettings));
    }

    /// Adds signs to the valid list of blocks of the {@link ReBlockEntities#SIGN SIGN} block entity.
    ///
    /// This method should be called during the {@linkplain FMLCommonSetupEvent common setup event}.
    /// @param signs An array of signs to add.
    /// @see ReStandingSignBlock
    /// @see ReWallSignBlock
    public static void addSigns(Block... signs) {
        SIGNS.addAll(List.of(signs));
    }

    /// Adds hanging signs to the valid list of blocks of the {@link ReBlockEntities#HANGING_SIGN HANGING_SIGN} block entity.
    ///
    /// This method should be called during the {@linkplain FMLCommonSetupEvent common setup event}.
    /// @param hangingSigns An array of hanging signs to add.
    /// @see ReCeilingHangingSignBlock
    /// @see ReWallHangingSignBlock
    public static void addHangingsSigns(Block... hangingSigns) {
        HANGING_SIGNS.addAll(List.of(hangingSigns));
    }

    /// Adds a block to the flammability map.
    ///
    /// This method should be called during the {@linkplain FMLCommonSetupEvent common setup event}.
    /// @param block The block.
    /// @param encouragement The chance of fire wanting to spread to this block.
    /// @param flammability The chance that this block will actually burn out when on fire.
    public static void flammable(Block block, int encouragement, int flammability) {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(block, encouragement, flammability);
    }

    /// Gets the translated text for a translation key, and uses a fallback if not available.
    /// @param key The translation key to use and check.
    /// @param fallback A fallback string to use, using `%s` for arguments.
    /// @param args An optional array of arguments.
    public static String translate(String key, String fallback, Object... args) {
        return I18n.exists(key) ? I18n.get(key, args) : String.format(fallback, args);
    }

    /// Whether a tooltip can be displayed on an item, or is hidden by the {@link ReDataComponents#HIDE_COMPONENTS reutilities:hide_components} component.
    /// @param holder A data component holder, such as an item stack.
    /// @param name A resource location of the tooltip name, like `reutilities:item_components`.
    public static boolean shouldDisplay(DataComponentHolder holder, ResourceLocation name) {
        List<ResourceLocation> hiddenComponents = holder.get(ReDataComponents.HIDE_COMPONENTS.get());
        if (hiddenComponents == null || hiddenComponents.isEmpty()) return true;
        return !hiddenComponents.contains(name);
    }

    /// Whether a tooltip can be displayed on an item, or is hidden by the {@link ReDataComponents#HIDE_COMPONENTS reutilities:hide_components} component.
    /// @param patch A data component patch, usually taken from an item stack.
    /// @param name A resource location of the tooltip name, like `reutilities:item_components`.
    public static boolean shouldDisplay(DataComponentPatch patch, ResourceLocation name) {
        Optional<? extends List<ResourceLocation>> hiddenComponents = patch.get(ReDataComponents.HIDE_COMPONENTS.get());
        // noinspection OptionalAssignedToNull
        if (hiddenComponents == null || hiddenComponents.isEmpty() || hiddenComponents.get().isEmpty()) return true;
        return !hiddenComponents.get().contains(name);
    }

    /// Gets the brightness that should be applied to an item, based on its presence in the {@link ReItemTags#EMISSIVE_LIGHTING #c:emissive_lighting} item tag,
    /// its {@link ReDataComponents#LIGHT_EMISSION reutilities:light_emission} component, and the block item's brightness.
    ///
    /// {@linkplain #getSkylight(Level, int) Skylight} is taken into consideration only when the world and position aren't `null`.
    /// @param stack The item stack to make emissive.
    /// @param lightEmission The old light value of this item, usually the `packedLight` parameter.
    /// @param world *(optional)* The world.
    /// @param pos *(optional)* The location in the world this item is in.
    /// @param applySkylight Whether skylight should be considered when calculating the light.
    public static int getItemBrightness(ItemStack stack, int lightEmission, Level world, BlockPos pos, boolean applySkylight) {
        float skylight = applySkylight ? getSkylight(world, lightEmission) : 15;
        int emittedBlockLight = getEmittedBlockLight(stack, world, pos);
        int ambientBlockLight = LightTexture.block(lightEmission);

        if (ReDebuggingFlags.DEBUG_LIGHT_EMISSION_DISPLAY && world.isClientSide()) {
            Player player = Minecraft.getInstance().player;
            if (player != null && !player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && ItemStack.isSameItemSameComponents(stack, player.getItemBySlot(EquipmentSlot.MAINHAND))) {
                player.displayClientMessage(Component.translatable(
                        "debug.reutilities.light_emission_display",
                        emittedBlockLight,
                        skylight,
                        ambientBlockLight), true);
            }
        }

        var emissionComponent = stack.get(ReDataComponents.LIGHT_EMISSION);
        if (emissionComponent != null) {
            int maxLight = (int) Math.max(ambientBlockLight, Math.max(emissionComponent, skylight));
            return LightTexture.pack(maxLight, maxLight);
        } else if (stack.is(ReItemTags.EMISSIVE_LIGHTING)) {
            return EMISSIVE_LIGHT_VALUE;
        } else if (ReCommonOptions.LIGHT_EMITTING_EMISSIVES.get() && emittedBlockLight > 0) {
            int maxLight = (int) Math.max(ambientBlockLight, Math.max(emittedBlockLight, skylight));
            return LightTexture.pack(maxLight, maxLight);
        }
        return lightEmission;
    }

    /// Gets the skylight at a given point in the world, or fully lit if the world doesn't exist.
    /// @param world *(optional)* The world.
    /// @param lightEmission The old light value for this item, usually the `packedLight` parameter.
    public static float getSkylight(@Nullable Level world, int lightEmission) {
        float skyLight = LightTexture.sky(lightEmission);
        if (world != null && world.isClientSide()) skyLight *= ((ClientLevel) world).getSkyDarken(1);
        return skyLight;
    }

    /// Gets the block light emitted from a given block, using its location in the world for reference if possible.
    /// @param stack The item stack to get the block item.
    /// @param world *(optional)* The world.
    /// @param pos *(optional)* The location in the world this item is in.
    public static int getEmittedBlockLight(ItemStack stack, @Nullable Level world, @Nullable BlockPos pos) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) return 0;
        var state = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);

        if (world == null || pos == null) {
            return state.apply(blockItem.getBlock().defaultBlockState()).getLightEmission();
        } else {
            return blockItem.getBlock().getLightEmission(state.apply(blockItem.getBlock().defaultBlockState()), world, pos);
        }
    }

    /// Tags all items from a specified namespace that have the {@link ReDataComponents#OUTFIT reutilities:outfit} component.
    /// @param modID The namespace of the mod.
    /// @param applier A {@link Consumer} that applies the tag changes to all items.
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
        register(bow, pullProgress(), (stack, world, livEntity, seed) -> {
            if (livEntity == null) {
                return 0;
            } else {
                return livEntity.getUseItem() != stack ? 0 : (float) (bow.getUseDuration(stack, livEntity) - livEntity.getUseItemRemainingTicks()) / Math.min(stack.getUseDuration(livEntity), 20);
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

    /// Adds the `reutilities:month_check/<month>` property to an item, used by *Back Math*'s carewni sword to check if it's June.
    /// @param item The item.
    /// @param month The month to check for.
    public static void addMonthCheckProperty(Item item, Month month) {
        register(item, monthCheck(month), (stack, world, livEntity, seed) -> LocalDate.now().getMonth() == month ? 1 : 0);
    }

    /// Transforms the provided resource location into a texture path.
    /// @param location The resource location.
    /// @return A new location, with the `textures/` prefix and `.png` suffix added.
    public static ResourceLocation toTexturePath(ResourceLocation location) {
        return location.withPath(path -> (path.startsWith("textures/") ? "" : "textures/") + path + (path.endsWith(".png") ? "" : ".png"));
    }

    /// Runs a list of {@linkplain ItemBehavior **item behaviors**} from an item stack, based on the provided environment.
    /// @param stack The item stack containing the `reutilities:behaviors` component.
    /// @param world The world.
    /// @param entity The entity running the behavior.
    /// @param environment The environment this behavior is being run on. Can be one of `eating` or `attacking`.
    /// @see ItemBehavior#runBehavior ItemBehavior.runBehavior()
    public static void runItemBehavior(ItemStack stack, Level world, Entity entity, String environment) {
        List<ItemBehavior> behaviors = stack.get(ReDataComponents.BEHAVIORS);
        if (behaviors != null && !behaviors.isEmpty() && entity instanceof LivingEntity livEntity) {
            behaviors.forEach(behavior -> {
                if (behavior.settings().environment().equals(environment)) behavior.runBehavior(stack, world, livEntity);
            });
        }
    }

    /// Adds an outfit item to a creative tab, specifying its definition and {@linkplain TooltipStyle tooltip style}.
    /// @param output The creative tab's item consumer.
    /// @param item The item to add.
    /// @param definition The outfit definition to put into the item.
    public static void addOutfit(CreativeModeTab.Output output, ItemLike item, ResourceKey<OutfitDefinition> definition) {
        addOutfit(output, item, definition, TooltipStyle.OUTFIT);
    }

    /// Adds an outfit item to a creative tab, specifying its definition and {@linkplain TooltipStyle tooltip style}.
    /// @param output The creative tab's item consumer.
    /// @param item The item to add.
    /// @param definition The outfit definition to put into the item.
    /// @param style The tooltip style for the tooltip.
    public static void addOutfit(CreativeModeTab.Output output, ItemLike item, ResourceKey<OutfitDefinition> definition, TooltipStyle style) {
        output.accept(Util.make(new ItemStack(item), stack -> stack.set(ReDataComponents.OUTFIT, ComponentOutfit.of(definition, style))));
    }

    /// Makes a list of {@linkplain ArmorMaterial.Layer armor layers} with a single entry using the specified name.
    /// @param name A {@linkplain ResourceLocation resource location} of the layer's name.
    public static List<ArmorMaterial.Layer> defaultLayers(ResourceLocation name) {
        return List.of(new ArmorMaterial.Layer(name));
    }

    /// Makes an {@link EnumMap} with the defense values for a full armor set.
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

    /// Makes an {@link EnumMap} with the defense values for a full armor set, including the body armor.
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
