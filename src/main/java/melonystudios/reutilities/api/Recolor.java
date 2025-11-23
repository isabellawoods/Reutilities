package melonystudios.reutilities.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import melonystudios.reutilities.util.ReCommonConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

/// Represents a single recolor (*Reutilities* color), used as an accompanying system to {@link DyeColor}.
/// @param color The color of this recolor.
/// @param colorLocation A resource location of the color, like `minecraft:magenta`.
/// @param toVanilla A vanilla {@link DyeColor} roughly equivalent to this color. Used when converting to vanilla for compatibility.
public record Recolor(int color, ResourceLocation colorLocation, DyeColor toVanilla) {
    public static final Codec<Recolor> CODEC = Codec.stringResolver(color -> color.colorLocation().toString(), location -> ReCommonConstants.COLORS.get(ResourceLocation.parse(location)));
    public static final Codec<ResourceLocation> RECOLOR_CODEC = ResourceLocation.CODEC.validate(location -> ReCommonConstants.COLORS.containsKey(location)
            ? DataResult.success(location) : DataResult.error(() -> Component.translatable("logger.reutilities.recolor.non_existent", location.toString()).getString()));
}
