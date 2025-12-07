package melonystudios.reutilities.entity.outfit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonystudios.reutilities.api.ReCodecs;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/// An **outfit slot** defines the textures and properties to be in a single equipment slot.
/// @param texture A {@linkplain ResourceLocation resource location} for the texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
/// @param emissiveTexture *(optional)* A resource location for the emissive texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
/// @param overlayTexture *(optional)* A resource location for the overlay texture. Overlay textures will not be colored. Omits the `textures/` prefix and `.png` suffix.
/// @param hidesSkinLayers *(optional)* Whether this slot hides the {@linkplain net.minecraft.world.entity.player.PlayerModelPart skin layers} associated with this slot.
/// @param color *(optional)* An integer defining a set color for this outfit slot. When defined, this field overrides the item's default color and the "{@link net.minecraft.core.component.DataComponents#DYED_COLOR minecraft:dyed_color}" component.
/// @see OutfitDefinition
/// @author isabellawoods. Copied from [*Back Math*'s documentation on **IMF**](https://github.com/isabellawoods/Informational-Mod-Features/blob/main/Back%20Math/Docs/Outfit%20Definition.md).
public record OutfitSlot(ResourceLocation texture, Optional<ResourceLocation> emissiveTexture, Optional<ResourceLocation> overlayTexture, boolean hidesSkinLayers, Optional<Integer> color) {
    public static final Codec<OutfitSlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(OutfitSlot::texture),
            ResourceLocation.CODEC.optionalFieldOf("emissive_texture").forGetter(OutfitSlot::emissiveTexture),
            ResourceLocation.CODEC.optionalFieldOf("overlay_texture").forGetter(OutfitSlot::overlayTexture),
            Codec.BOOL.optionalFieldOf("hides_skin_layers", true).forGetter(OutfitSlot::hidesSkinLayers),
            ReCodecs.HEX_INT_CODEC.optionalFieldOf("color").forGetter(OutfitSlot::color)
    ).apply(instance, OutfitSlot::new));
    public static final StreamCodec<ByteBuf, OutfitSlot> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, OutfitSlot::texture,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs::optional), OutfitSlot::emissiveTexture,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs::optional), OutfitSlot::overlayTexture,
            ByteBufCodecs.BOOL, OutfitSlot::hidesSkinLayers,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs::optional), OutfitSlot::color,
            OutfitSlot::new
    );

    /// Creates an instance of the **outfit slot builder**.
    /// @param texture A {@linkplain ResourceLocation resource location} for the texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
    public static Builder slot(ResourceLocation texture) {
        return new Builder(texture);
    }

    @Override
    public int hashCode() {
        return 31 * this.texture().hashCode() + this.emissiveTexture().hashCode() + this.overlayTexture().hashCode() + Boolean.hashCode(this.hidesSkinLayers()) + this.color().hashCode();
    }

    @Override
    @NotNull
    public String toString() {
        return "OutfitSlot[texture=" + this.texture + ", emissive_texture=" + this.emissiveTexture + ", overlay_texture=" + this.overlayTexture + ", hides_skin_layers=" + this.hidesSkinLayers + ", color=" + this.color + "]";
    }

    public static class Builder {
        private final ResourceLocation texture;
        private ResourceLocation emissiveTexture;
        private ResourceLocation overlayTexture;
        private boolean hidesSkinLayers;
        private Integer color;

        /// Creates an instance of the **outfit slot builder**.
        /// @param texture A {@linkplain ResourceLocation resource location} for the texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
        public Builder(ResourceLocation texture) {
            this.texture = texture;
        }

        /// Sets the emissive texture used by this outfit slot. Omits the `textures/` prefix and `.png` suffix.
        /// @param emissiveTexture A resource location for the emissive texture.
        public Builder withEmissive(ResourceLocation emissiveTexture) {
            this.emissiveTexture = emissiveTexture;
            return this;
        }

        /// Sets the overlay texture used by this outfit slot. Overlay textures will not be colored. Omits the `textures/` prefix and `.png` suffix.
        /// @param overlayTexture A resource location for the overlay texture.
        public Builder withOverlay(ResourceLocation overlayTexture) {
            this.overlayTexture = overlayTexture;
            return this;
        }

        /// Makes this outfit slot show the {@linkplain net.minecraft.world.entity.player.PlayerModelPart skin layers} associated with this slot.
        public Builder showSkinLayers() {
            this.hidesSkinLayers = true;
            return this;
        }

        /// Sets a color to apply to this outfit slot. When defined, this field overrides the item's default color and the "{@link net.minecraft.core.component.DataComponents#DYED_COLOR minecraft:dyed_color}" component.
        /// @param color An integer defining a set color for this outfit slot.
        public Builder withColor(int color) {
            this.color = color;
            return this;
        }

        /// Builds this builder into an outfit slot.
        public OutfitSlot build() {
            return new OutfitSlot(this.texture, Optional.ofNullable(this.emissiveTexture), Optional.ofNullable(this.overlayTexture), this.hidesSkinLayers, Optional.ofNullable(this.color));
        }
    }
}
