package melonystudios.reutilities.entity.outfit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.component.custom.ComponentOutfit;
import melonystudios.reutilities.util.ReRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

import java.util.*;

/// An **outfit definition** is a set of textures to use for an outfit item or `outfit` tag. Outfit definitions can be defined using JSON files in a data pack at the path `data/<namespace>/reutilities/outfit_definition/`.
/// @author isabellawoods. Copied from [*Back Math*'s documentation on **IMF**](https://github.com/isabellawoods/Informational-Mod-Features/blob/main/Back%20Math/Docs/Outfit%20Definition.md).
/// @see OutfitSlot
public class OutfitDefinition {
    public static final Codec<OutfitDefinition> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            OutfitSlot.CODEC.listOf().fieldOf("slots").forGetter(OutfitDefinition::slots)
    ).apply(instance, OutfitDefinition::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, OutfitDefinition> DIRECT_STREAM_CODEC = StreamCodec.composite(
            OutfitSlot.STREAM_CODEC.apply(ByteBufCodecs.list()), OutfitDefinition::slots,
            OutfitDefinition::new
    );
    public static final Codec<Holder<OutfitDefinition>> CODEC = RegistryFileCodec.create(ReRegistries.OUTFIT_DEFINITION, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<OutfitDefinition>> STREAM_CODEC = ByteBufCodecs.holder(ReRegistries.OUTFIT_DEFINITION, DIRECT_STREAM_CODEC);
    public static final int DEFAULT_OUTFIT_COLOR = 0xFFFFFF;
    private final List<OutfitSlot> slots;
    @Nullable
    private final OutfitSlot handFallbackSlot;

    /// An **outfit definition** is a set of textures to use for an outfit item or `outfit` tag.
    /// @param slots A list of outfit slot. This defines the properties used for each {@linkplain EquipmentSlot equipment slot}.
    public OutfitDefinition(List<OutfitSlot> slots) {
        this.slots = new ArrayList<>(slots);
        this.handFallbackSlot = this.slots().stream().filter(slot -> slot.slot() == EquipmentSlot.CHEST).findFirst().orElse(null);
        this.slots().sort(Collections.reverseOrder(Comparator.comparingInt(slot -> slot.slot().ordinal())));
    }

    /// @return A list of outfit slot. This defines the properties used for each {@linkplain EquipmentSlot equipment slot}.
    public List<OutfitSlot> slots() {
        return this.slots;
    }

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
        ComponentOutfit definitionName = stack.get(ReDataComponents.OUTFIT);
        if (definitionName == null) return null;
        return definitionName.definition().unwrap(definitions(world)).orElse(null);
    }

    /// Gets an outfit slot from a definition based on an equipment slot.
    /// @param definition The outfit definition to get the slots from.
    /// @param slotType An equipment slot to get the slot.
    public static Optional<OutfitSlot> byEquipmentSlot(@Nullable OutfitDefinition definition, EquipmentSlot slotType) {
        if (definition == null) return Optional.empty();
        for (OutfitSlot slot : definition.slots()) {
            if (slot.slot() == slotType) {
                return Optional.of(slot);
            } else if (slot.slot().getType() == EquipmentSlot.Type.HAND) {
                return definition.slots().stream().filter(slot1 -> slot1.slot() == EquipmentSlot.CHEST).findFirst();
            }
        }
        return Optional.empty();
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
        if (definition == null) return DEFAULT_OUTFIT_COLOR;
        Optional<OutfitSlot> slot = byEquipmentSlot(definition, slotType);
        if (slot.isPresent() && slot.get().color().isPresent()) {
            return slot.get().color().get();
        } else if (stack != null) {
            DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);
            if (dyedColor != null) return dyedColor.rgb();
        }
        return DEFAULT_OUTFIT_COLOR;
    }

    /// Gets the default outfit texture for an entity.
    /// @param slotType The equipment slot of the outfit, used to get the texture.
    /// @param definition The outfit definition to get the textures from.
    /// @param slimArms Whether the entity has slim arms, used to get the texture.
    /// @return A resource location of the outfit texture to be rendered, or null if `definition` is null.
    @Nullable
    public static ResourceLocation getOutfitTexture(EquipmentSlot slotType, OutfitDefinition definition, boolean slimArms) {
        if (definition == null) return null;

        ResourceLocation location = null;
        for (OutfitSlot slot : definition.slots()) {
            if (slot.slot() == slotType) {
                if (hasSlimVariant(slotType)) {
                    location = ResourceLocation.fromNamespaceAndPath(slot.texture().getNamespace(), "textures/" + slot.texture().getPath() + (slimArms ? "_slim" : "_classic") + ".png");
                } else {
                    location = ResourceLocation.fromNamespaceAndPath(slot.texture().getNamespace(), "textures/" + slot.texture().getPath() + ".png");
                }
                return location;
            } else if (slot.slot().getType() == EquipmentSlot.Type.HAND && definition.handFallbackSlot != null) {
                location = ResourceLocation.fromNamespaceAndPath(definition.handFallbackSlot.texture().getNamespace(), "textures/" + definition.handFallbackSlot.texture().getPath() + (slimArms ? "_slim" : "_classic") + ".png");
                return location;
            }
        }
        return location;
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
        for (OutfitSlot slot : definition.slots()) {
            if (slot.emissiveTexture().isEmpty()) continue;

            if (slot.slot() == slotType) {
                if (hasSlimVariant(slotType)) {
                    location = ResourceLocation.fromNamespaceAndPath(slot.emissiveTexture().get().getNamespace(), "textures/" + slot.emissiveTexture().get().getPath() + (slimArms ? "_slim" : "_classic") + ".png");
                } else {
                    location = ResourceLocation.fromNamespaceAndPath(slot.emissiveTexture().get().getNamespace(), "textures/" + slot.emissiveTexture().get().getPath() + ".png");
                }
                return location;
            } else if (slot.slot().getType() == EquipmentSlot.Type.HAND && definition.handFallbackSlot != null && definition.handFallbackSlot.emissiveTexture().isPresent()) {
                location = ResourceLocation.fromNamespaceAndPath(definition.handFallbackSlot.emissiveTexture().get().getNamespace(), "textures/" + definition.handFallbackSlot.emissiveTexture().get().getPath() + (slimArms ? "_slim" : "_classic") + ".png");
                return location;
            }
        }
        return null;
    }

    public static boolean hasSlimVariant(EquipmentSlot slotType) {
        return slotType.getType() == EquipmentSlot.Type.HAND || slotType == EquipmentSlot.CHEST;
    }

    @Override
    @NotNull
    public String toString() {
        return "OutfitDefinition[slots=" + this.slots() + "]";
    }
}
