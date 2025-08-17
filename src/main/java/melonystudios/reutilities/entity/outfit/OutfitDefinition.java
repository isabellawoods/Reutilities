package melonystudios.reutilities.entity.outfit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.util.ReRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/// An **outfit definition** is a set of textures to use for an outfit item or `outfit` tag. Outfit definitions can be defined using JSON files in a data pack at the path `data/<namespace>/reutilities/outfit_definition/`.
/// @param headSlot *(optional)* The head outfit slot. Used when getting a texture for the entity's head.
/// @param chestSlot Same as `headSlot`.
/// @param legsSlot Same as `headSlot`.
/// @param feetSlot Same as `headSlot`.
/// @see OutfitSlot Outfit Slots
/// @author isabellawoods. Copied from [Back Math's documentation on **IMF**](https://github.com/isabellawoods/Informational-Mod-Features/blob/main/Back%20Math/Docs/Outfit%20Definition.md).
public record OutfitDefinition(Optional<OutfitSlot> headSlot, Optional<OutfitSlot> chestSlot, Optional<OutfitSlot> legsSlot, Optional<OutfitSlot> feetSlot) {
    public static final Codec<OutfitDefinition> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            OutfitSlot.CODEC.optionalFieldOf("head").forGetter(OutfitDefinition::headSlot),
            OutfitSlot.CODEC.optionalFieldOf("chest").forGetter(OutfitDefinition::chestSlot),
            OutfitSlot.CODEC.optionalFieldOf("legs").forGetter(OutfitDefinition::legsSlot),
            OutfitSlot.CODEC.optionalFieldOf("feet").forGetter(OutfitDefinition::feetSlot)
    ).apply(instance, OutfitDefinition::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, OutfitDefinition> DIRECT_STREAM_CODEC = StreamCodec.composite(
            OutfitSlot.STREAM_CODEC.apply(ByteBufCodecs::optional), OutfitDefinition::headSlot,
            OutfitSlot.STREAM_CODEC.apply(ByteBufCodecs::optional), OutfitDefinition::chestSlot,
            OutfitSlot.STREAM_CODEC.apply(ByteBufCodecs::optional), OutfitDefinition::legsSlot,
            OutfitSlot.STREAM_CODEC.apply(ByteBufCodecs::optional), OutfitDefinition::feetSlot,
            OutfitDefinition::new
    );
    public static final Codec<Holder<OutfitDefinition>> CODEC = RegistryFileCodec.create(ReRegistries.OUTFIT_DEFINITION, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<OutfitDefinition>> STREAM_CODEC = ByteBufCodecs.holder(ReRegistries.OUTFIT_DEFINITION, DIRECT_STREAM_CODEC);

    /// Gets the outfit definitions registry.
    /// @param world The world.
    /// @throws IllegalStateException If the registry does not exist.
    public static Registry<OutfitDefinition> definitions(Level world) {
        return world.registryAccess().registryOrThrow(ReRegistries.OUTFIT_DEFINITION);
    }

    /// Gets the outfit definition from an item stack's "{@link ReDataComponents#OUTFIT reutilities:outfit}" component.
    /// @param world The world to get the registries.
    /// @param stack The item stack.
    @Nullable
    public static OutfitDefinition getDefinition(Level world, ItemStack stack) {
        Holder<OutfitDefinition> definitionName = stack.get(ReDataComponents.OUTFIT);
        if (definitionName != null && definitionName.getRegisteredName().equals("[unregistered]")) return null;
        return definitions(world).get(ResourceLocation.parse(definitionName.getRegisteredName()));
    }

    /// Gets an outfit slot from a definition based on an equipment slot.
    /// @param definition The outfit definition to get the slots from.
    /// @param slotType An equipment slot to get the slot.
    /// @throws IllegalArgumentException If the equipment slot is not one of {@link EquipmentSlot#HEAD HEAD}, {@link EquipmentSlot#CHEST CHEST}, {@link EquipmentSlot#LEGS LEGS} or {@link EquipmentSlot#FEET FEET}.
    public static Optional<OutfitSlot> byEquipmentSlot(OutfitDefinition definition, EquipmentSlot slotType) {
        return switch (slotType) {
            case HEAD -> definition.headSlot;
            case CHEST -> definition.chestSlot;
            case LEGS -> definition.legsSlot;
            case FEET -> definition.feetSlot;
            default -> throw new IllegalArgumentException(Component.translatable("logger.reutilities.outfit_definition.wrong_equipment_slot", slotType.getName()).getString());
        };
    }

    /// Whether to hide the skin layers for the player or entity when wearing this outfit on this slot.
    /// @param slotType Which slot the outfit is being rendered in.
    /// @param definition The outfit definition. Used to get the "`hides_skin_layers`" boolean field on the slot.
    /// @param slimArms Whether the entity has slim arms, used to find the correct texture.
    public static boolean shouldHideLayer(EquipmentSlot slotType, OutfitDefinition definition, boolean slimArms) {
        ResourceLocation outfitLocation = getOutfitTexture(slotType, definition, slimArms);
        Optional<OutfitSlot> slot = byEquipmentSlot(definition, slotType);
        return outfitLocation != null && slot.isPresent() && slot.get().hidesSkinLayers();
    }

    /// Gets the color of an outfit slot. If the outfit slot has a color, it chooses that, if it doesn't, but the item stack does, it picks the item stack's color.
    /// @param definition The outfit definition to get the textures from.
    /// @param stack An optional item stack to check for colors.
    /// @param slotType The slot being checked for the colors.
    public static int getOutfitColors(OutfitDefinition definition, @Nullable ItemStack stack, EquipmentSlot slotType) {
        if (definition == null) return 0xFFFFFF;
        Optional<OutfitSlot> slot = byEquipmentSlot(definition, slotType);
        if (slot.isPresent() && slot.get().color().isPresent()) {
            return slot.get().color().get();
        } else if (stack != null) {
            DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);
            if (dyedColor != null) return dyedColor.rgb();
        }
        return 0xFFFFFF;
    }

    /// Gets the default outfit texture for an entity.
    /// @param slotType The equipment slot of the outfit, used to get the texture.
    /// @param definition The outfit definition to get the textures from.
    /// @param slimArms Whether the entity has slim arms, used to get the texture.
    /// @return A resource location of the outfit texture to be rendered, or null if `definition` is null.
    @Nullable
    public static ResourceLocation getOutfitTexture(EquipmentSlot slotType, OutfitDefinition definition, boolean slimArms) {
        if (definition == null) return null;

        ResourceLocation location;
        switch (slotType) {
            case HEAD: {
                if (definition.headSlot().isPresent()) {
                    location = ResourceLocation.fromNamespaceAndPath(definition.headSlot().get().texture().getNamespace(), "textures/" + definition.headSlot().get().texture().getPath() + ".png");
                    return location;
                }
                break;
            }
            case CHEST: {
                if (definition.chestSlot().isPresent()) {
                    location = ResourceLocation.fromNamespaceAndPath(definition.chestSlot().get().texture().getNamespace(), "textures/" + definition.chestSlot().get().texture().getPath() + (slimArms ? "_slim" : "_classic") + ".png");
                    return location;
                }
                break;
            }
            case LEGS: {
                if (definition.legsSlot().isPresent()) {
                    location = ResourceLocation.fromNamespaceAndPath(definition.legsSlot().get().texture().getNamespace(), "textures/" + definition.legsSlot().get().texture().getPath() + ".png");
                    return location;
                }
                break;
            }
            case FEET: {
                if (definition.feetSlot().isPresent()) {
                    location = ResourceLocation.fromNamespaceAndPath(definition.feetSlot().get().texture().getNamespace(), "textures/" + definition.feetSlot().get().texture().getPath() + ".png");
                    return location;
                }
                break;
            }
        }
        return null;
    }

    /// Gets the emissive outfit texture for an entity.
    /// @param slotType The equipment slot of the outfit, used to get the texture.
    /// @param definition The outfit definition to get the textures from.
    /// @param slimArms Whether the entity has slim arms, used to get the texture.
    /// @return A resource location of the emissive outfit texture to be rendered, or null if `definition` is null.
    @Nullable
    public static ResourceLocation getEmissiveOutfitTexture(EquipmentSlot slotType, OutfitDefinition definition, boolean slimArms) {
        if (definition == null) return null;

        ResourceLocation location;
        switch (slotType) {
            case HEAD: {
                if (definition.headSlot().isPresent() && definition.headSlot().get().emissiveTexture().isPresent()) {
                    location = ResourceLocation.fromNamespaceAndPath(definition.headSlot().get().emissiveTexture().get().getNamespace(), "textures/" + definition.headSlot().get().emissiveTexture().get().getPath() + ".png");
                    return location;
                }
                break;
            }
            case CHEST: {
                if (definition.chestSlot().isPresent() && definition.chestSlot().get().emissiveTexture().isPresent()) {
                    location = ResourceLocation.fromNamespaceAndPath(definition.chestSlot().get().emissiveTexture().get().getNamespace(), "textures/" + definition.chestSlot().get().emissiveTexture().get().getPath() + (slimArms ? "_slim" : "_classic") + ".png");
                    return location;
                }
                break;
            }
            case LEGS: {
                if (definition.legsSlot().isPresent() && definition.legsSlot().get().emissiveTexture().isPresent()) {
                    location = ResourceLocation.fromNamespaceAndPath(definition.legsSlot().get().emissiveTexture().get().getNamespace(), "textures/" + definition.legsSlot().get().emissiveTexture().get().getPath() + ".png");
                    return location;
                }
                break;
            }
            case FEET: {
                if (definition.feetSlot().isPresent() && definition.feetSlot().get().emissiveTexture().isPresent()) {
                    location = ResourceLocation.fromNamespaceAndPath(definition.feetSlot().get().emissiveTexture().get().getNamespace(), "textures/" + definition.feetSlot().get().emissiveTexture().get().getPath() + ".png");
                    return location;
                }
                break;
            }
        }
        return null;
    }

    @Override
    @NotNull
    public String toString() {
        return "OutfitDefinition[head=" + this.headSlot() + ", chest=" + this.chestSlot() + ", legs=" + this.legsSlot() + ", feet=" + this.feetSlot() + "]";
    }
}
