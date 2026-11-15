package melonystudios.reutilities.api.picker;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

/// Defines when color blending should occur during the [color picker `sample()` process][ColorPicker#sample(ColorPicker, RandomSource)].
public enum ColorBlendingOrder implements StringRepresentable {
    /// Blends all picked colors before interpolating them.
    BEFORE("before"),
    /// Interpolated all picked colors, and them blends them together.
    AFTER("after"),
    /// Picks [#BEFORE] or [#AFTER] randomly.
    RANDOM("random");

    public static final Codec<ColorBlendingOrder> CODEC = StringRepresentable.fromValues(ColorBlendingOrder::values);
    private final String name;

    ColorBlendingOrder(String name) {
        this.name = name;
    }

    /// Returns whether this to blend before or after interpolation.
    /// @param random The random source.
    /// @return Whether this to blend before or after interpolation.
    public boolean blendsBefore(RandomSource random) {
        return this == BEFORE || this == RANDOM && random.nextBoolean();
    }

    /// Returns whether to blend after interpolation.
    /// @param random The random source.
    /// @return Whether to blend after interpolation.
    public boolean blendsAfter(RandomSource random) {
        return this == AFTER || this == RANDOM && random.nextBoolean();
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
