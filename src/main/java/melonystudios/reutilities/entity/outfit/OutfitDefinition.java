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

import java.util.Optional;

/// An **outfit definition** is a set of textures to use for an outfit item or `outfit` tag. Outfit definitions can be defined using JSON files in a data pack at the path `data/<namespace>/reutilities/outfit_definition/`.
/// @author isabellawoods. Copied from [*Back Math*'s documentation on **IMF**](https://github.com/isabellawoods/Informational-Mod-Features/blob/main/Back%20Math/Docs/Outfit%20Definition.md).
/// @param headSlot *(optional)* The head outfit slot. Used when getting a texture for the entity's head.
/// @param chestSlot Same as `headSlot`.
/// @param legsSlot Same as `headSlot`.
/// @param feetSlot Same as `headSlot`.
/// @param bodySlot Same as `headSlot`. This is provided by *Reutilities* only to have all equipment slots covered, as the mod doesn't provide any use cases for these.
/// @param mainhandSlot Same as `headSlot`. Fallbacks to `chestSlot` if not defined.
/// @param offhandSlot Same as `headSlot`. Fallbacks to `chestSlot` if not defined.
/// @see OutfitSlot
public record OutfitDefinition(Optional<OutfitSlot> headSlot, Optional<OutfitSlot> chestSlot, Optional<OutfitSlot> legsSlot, Optional<OutfitSlot> feetSlot, Optional<OutfitSlot> bodySlot, Optional<OutfitSlot> mainhandSlot, Optional<OutfitSlot> offhandSlot) {
    public static final Codec<OutfitDefinition> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            OutfitSlot.CODEC.optionalFieldOf("head").forGetter(OutfitDefinition::headSlot),
            OutfitSlot.CODEC.optionalFieldOf("chest").forGetter(OutfitDefinition::chestSlot),
            OutfitSlot.CODEC.optionalFieldOf("legs").forGetter(OutfitDefinition::legsSlot),
            OutfitSlot.CODEC.optionalFieldOf("feet").forGetter(OutfitDefinition::feetSlot),
            OutfitSlot.CODEC.optionalFieldOf("body").forGetter(OutfitDefinition::bodySlot),
            OutfitSlot.CODEC.optionalFieldOf("mainhand").forGetter(OutfitDefinition::mainhandSlot),
            OutfitSlot.CODEC.optionalFieldOf("offhand").forGetter(OutfitDefinition::offhandSlot)
    ).apply(instance, OutfitDefinition::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, OutfitDefinition> DIRECT_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public OutfitDefinition decode(RegistryFriendlyByteBuf buffer) {
            Optional<OutfitSlot> head = ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).decode(buffer);
            Optional<OutfitSlot> chest = ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).decode(buffer);
            Optional<OutfitSlot> legs = ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).decode(buffer);
            Optional<OutfitSlot> feet = ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).decode(buffer);
            Optional<OutfitSlot> body = ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).decode(buffer);
            Optional<OutfitSlot> mainhand = ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).decode(buffer);
            Optional<OutfitSlot> offhand = ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).decode(buffer);
            return new OutfitDefinition(head, chest, legs, feet, body, mainhand, offhand);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, OutfitDefinition definition) {
            ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).encode(buffer, definition.headSlot());
            ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).encode(buffer, definition.chestSlot());
            ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).encode(buffer, definition.legsSlot());
            ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).encode(buffer, definition.feetSlot());
            ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).encode(buffer, definition.bodySlot());
            ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).encode(buffer, definition.mainhandSlot());
            ByteBufCodecs.optional(OutfitSlot.STREAM_CODEC).encode(buffer, definition.offhandSlot());
        }
    };
    public static final Codec<Holder<OutfitDefinition>> CODEC = RegistryFileCodec.create(ReRegistries.OUTFIT_DEFINITION, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<OutfitDefinition>> STREAM_CODEC = ByteBufCodecs.holder(ReRegistries.OUTFIT_DEFINITION, DIRECT_STREAM_CODEC);
    public static final int DEFAULT_OUTFIT_COLOR = 0xFFFFFF;

    /// Creates an instance of the **outfit definition builder**.
    public static DefinitionBuilder definition() {
        return new DefinitionBuilder();
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
        return switch (slotType) {
            case HEAD -> definition.headSlot();
            case LEGS -> definition.legsSlot();
            case FEET -> definition.feetSlot();
            case BODY -> definition.bodySlot();
            case MAINHAND -> definition.mainhandSlot().or(definition::chestSlot);
            case OFFHAND -> definition.offhandSlot().or(definition::chestSlot);
            default -> definition.chestSlot();
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

    /// Gets the texture of an outfit.
    /// @param texture The original texture provided by the outfit slot.
    /// @param slimArms Which variation of the texture to use. `false` for classic arms, `true` for slim arms, and `null` for no variation.
    /// @return A resource location of the texture to use, with the `textures/` prefix and `.png` suffix.
    public static ResourceLocation textureForSlot(ResourceLocation texture, Boolean slimArms) {
        if (slimArms != null) {
            return ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), "textures/" + texture.getPath() + (slimArms ? "_slim" : "_classic") + ".png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), "textures/" + texture.getPath() + ".png");
        }
    }

    /// Whether this equipment slot can variate between the classic and slim arm types.
    /// @param slotType The equipment slot.
    public static boolean hasSlimVariant(EquipmentSlot slotType) {
        return slotType.getType() == EquipmentSlot.Type.HAND || slotType == EquipmentSlot.CHEST;
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
        return byEquipmentSlot(definition, slotType)
                .map(slot -> textureForSlot(slot.texture(), hasSlimVariant(slotType) ? slimArms : null))
                .orElse(null);
    }

    /// Gets the emissive outfit texture for an entity.
    /// @param slotType The equipment slot of the outfit, used to get the texture.
    /// @param definition The outfit definition to get the textures from.
    /// @param slimArms Whether the entity has slim arms, used to get the texture.
    /// @return A resource location of the emissive outfit texture to be rendered, or null if `definition` is null.
    @Nullable
    public static ResourceLocation getEmissiveOutfitTexture(EquipmentSlot slotType, OutfitDefinition definition, boolean slimArms) {
        if (definition == null) return null;

        Optional<OutfitSlot> slot = byEquipmentSlot(definition, slotType);
        if (slot.isPresent() && slot.get().emissiveTexture().isPresent()) {
            return textureForSlot(slot.get().emissiveTexture().get(), hasSlimVariant(slotType) ? slimArms : null);
        }
        return null;
    }

    /// Gets the overlay outfit texture for an entity.
    /// @param slotType The equipment slot of the outfit, used to get the texture.
    /// @param definition The outfit definition to get the textures from.
    /// @param slimArms Whether the entity has slim arms, used to get the texture.
    /// @return A resource location of the overlay outfit texture to be rendered, or null if `definition` is null.
    @Nullable
    public static ResourceLocation getOverlayOutfitTexture(EquipmentSlot slotType, OutfitDefinition definition, boolean slimArms) {
        if (definition == null) return null;

        Optional<OutfitSlot> slot = byEquipmentSlot(definition, slotType);
        if (slot.isPresent() && slot.get().overlayTexture().isPresent()) {
            return textureForSlot(slot.get().overlayTexture().get(), hasSlimVariant(slotType) ? slimArms : null);
        }
        return null;
    }

    @Override
    @NotNull
    public String toString() {
        StringBuilder builder = new StringBuilder("OutfitDefinition[");

        this.headSlot().ifPresent(slot -> builder.append("head=").append(this.headSlot().get()).append(", "));
        this.chestSlot().ifPresent(slot -> builder.append("chest=").append(this.chestSlot().get()).append(", "));
        this.legsSlot().ifPresent(slot -> builder.append("legs=").append(this.legsSlot().get()).append(", "));
        this.feetSlot().ifPresent(slot -> builder.append("feet=").append(this.feetSlot().get()).append(", "));
        this.bodySlot().ifPresent(slot -> builder.append("body=").append(this.bodySlot().get()).append(", "));
        this.mainhandSlot().ifPresent(slot -> builder.append("mainhand=").append(this.mainhandSlot().get()).append(", "));
        this.offhandSlot().ifPresent(slot -> builder.append("offhand=").append(this.offhandSlot().get()));

        builder.append("]");
        return builder.toString();
    }
}
