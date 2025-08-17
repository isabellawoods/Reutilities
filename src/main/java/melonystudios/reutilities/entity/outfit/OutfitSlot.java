package melonystudios.reutilities.entity.outfit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/// An **outfit slot** defines the textures and properties to be in a single equipment slot.
/// @param texture A {@linkplain ResourceLocation resource location} for the texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
/// @param emissiveTexture *(optional)* A resource location for the emissive texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
/// @param hidesSkinLayers *(optional)* Whether this slot hides the {@linkplain net.minecraft.world.entity.player.PlayerModelPart skin layers} associated with this slot.
/// @param color *(optional)* An integer defining a set color for this outfit slot. When defined, this field overrides the item's default color and the "{@link net.minecraft.core.component.DataComponents#DYED_COLOR minecraft:dyed_color}" component.
/// @see OutfitDefinition Outfit Definitions
/// @author isabellawoods. Copied from [Back Math's documentation on **IMF**](https://github.com/isabellawoods/Informational-Mod-Features/blob/main/Back%20Math/Docs/Outfit%20Definition.md).
public record OutfitSlot(ResourceLocation texture, Optional<ResourceLocation> emissiveTexture, boolean hidesSkinLayers, Optional<Integer> color) {
    public static final Codec<OutfitSlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(OutfitSlot::texture),
            ResourceLocation.CODEC.optionalFieldOf("emissive_texture").forGetter(OutfitSlot::emissiveTexture),
            Codec.BOOL.optionalFieldOf("hides_skin_layers", true).forGetter(OutfitSlot::hidesSkinLayers),
            Codec.INT.optionalFieldOf("color").forGetter(OutfitSlot::color)
    ).apply(instance, OutfitSlot::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, OutfitSlot> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, OutfitSlot::texture,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs::optional), OutfitSlot::emissiveTexture,
            ByteBufCodecs.BOOL, OutfitSlot::hidesSkinLayers,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs::optional), OutfitSlot::color,
            OutfitSlot::new
    );

    /// @param texture A {@linkplain ResourceLocation resource location} for the texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
    /// @param emissiveTexture *(optional)* A resource location for the emissive texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
    public OutfitSlot(ResourceLocation texture, ResourceLocation emissiveTexture) {
        this(texture, Optional.ofNullable(emissiveTexture), true, Optional.empty());
    }

    /// @param texture A {@linkplain ResourceLocation resource location} for the texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
    /// @param hidesSkinLayers *(optional)* Whether this slot hides the {@linkplain net.minecraft.world.entity.player.PlayerModelPart skin layers} associated with this slot.
    public OutfitSlot(ResourceLocation texture, boolean hidesSkinLayers) {
        this(texture, Optional.empty(), hidesSkinLayers, Optional.empty());
    }

    /// @param texture A {@linkplain ResourceLocation resource location} for the texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
    public OutfitSlot(ResourceLocation texture) {
        this(texture, Optional.empty(), true, Optional.empty());
    }

    @Override
    @NotNull
    public String toString() {
        return "OutfitSlot[texture=" + this.texture + ", emissive_texture=" + this.emissiveTexture + ", hides_skin_layers=" + this.hidesSkinLayers + ", color=" + this.color + "]";
    }
}
