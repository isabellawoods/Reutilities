package melonystudios.reutilities.entity.cape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.api.ReCodecs;
import melonystudios.reutilities.util.ReRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/// A **recape** (*Reutilities* cape) is a kind of cape that applies to all living entities, using {@linkplain melonystudios.reutilities.api.ReAPI#CAPE_CAPABILITY entity capabilities} for saving and loading.
/// Recapes can be defined using JSON files in a data pack at the path `data/<namespace>/reutilities/cape/`.
///
/// Recape tags can be defined at the path `data/<namespace>/tags/reutilities/cape/`.
/// @apiNote **This is still work in progress!!**
/// @since **1.5.0** (09-01-26)
/// @param cape A resource location for the cape's texture.
/// @param elytra An *optional* resource location for the elytra texture. If not defined, defaults to the cape texture. Use {@link #elytraTexture()} instead of this method for the texture!
/// @param emissive An *optional* emissive texture to render on top of the cape and elytra.
/// @param elytraModelOverride The model override used for elytra items. This can be used to display your current cape on the item.
@ApiStatus.Experimental
public record Recape(ResourceLocation cape, Optional<ResourceLocation> elytra, Optional<ResourceLocation> emissive, Optional<Float> elytraModelOverride) {
    public static final Codec<Recape> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("cape").forGetter(Recape::cape),
            ResourceLocation.CODEC.optionalFieldOf("elytra").forGetter(Recape::elytra),
            ResourceLocation.CODEC.optionalFieldOf("emissive").forGetter(Recape::emissive),
            ReCodecs.floatRange(1, Float.MAX_VALUE).optionalFieldOf("elytra_model_override").forGetter(Recape::elytraModelOverride)
    ).apply(instance, Recape::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Recape> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            Recape::cape,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
            Recape::elytra,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
            Recape::emissive,
            ByteBufCodecs.optional(ByteBufCodecs.FLOAT),
            Recape::elytraModelOverride,
            Recape::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Recape>> STREAM_CODEC = ByteBufCodecs.holder(ReRegistries.CAPE, DIRECT_STREAM_CODEC);

    /// @return The elytra texture for this recape, falling back to the default cape texture if not present.
    public ResourceLocation elytraTexture() {
        return this.elytra().orElse(this.cape());
    }
}
