package melonystudios.reutilities.api.picker;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import melonystudios.reutilities.api.ReCodecs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.slf4j.Logger;

/// Represents a sample from a color picker in three different formats:
/// - [Decimal][#asDecimal()]: `-2147483648` to `2147483647`;
/// - [Hexadecimal][#asHexadecimal()]: `#aarrggbb`;
/// - [Float array][#asFloatArray()]: `[0.0, 0.0, 0.0, 0.0]` (from left to right: alpha, red, green, blue, all from `0` to `1`).
public class PickerSample {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final int decimal;
    private Component hexadecimal;

    /// Represents a sample from a color picker in three different formats: decimal, hexadecimal and as an array of floats.
    /// @param value The color sampled from the color picker.
    public PickerSample(int value) {
        this.decimal = value;
    }

    /// @return The sampled color in decimal format, as an `Integer`.
    public int asDecimal() {
        return this.decimal;
    }

    /// Returns the sampled color in hexadecimal format (`#rrggbb` or `#aarrggbb`) as a [text component][Component].
    ///
    /// If any errors occur when converting to hexadecimal, the resulting text component reads the following:
    /// <pre>{@code An error occurred while encoding the decimal value into hexadecimal, check the logs for more information.}</pre>
    /// @return The sampled color in **plain text**, or an error message as a **translatable component**.
    public Component asHexadecimal() {
        if (this.hexadecimal == null) {
            ReCodecs.OPTIONAL_ARGB_CODEC.encodeStart(JsonOps.INSTANCE, this.decimal)
                    .resultOrPartial(error -> LOGGER.error("Failed to encode a decimal value into hexadecimal: {}", error))
                    .ifPresent(element -> {
                        if (element.getAsJsonPrimitive().isString()) this.hexadecimal = Component.literal(element.getAsString());
                        else this.hexadecimal = Component.translatable("commands.mstudios.sample_color_picker.encoding").withStyle(ChatFormatting.RED);
                    });
        }
        return this.hexadecimal;
    }

    /// @return An array of **4** (with transparency) / **3** (no transparency) floats representing the alpha, red, green and blue channels of the color.
    public float[] asFloatArray() {
        float alpha = FastColor.ARGB32.alpha(this.decimal) / 255F;
        float red = FastColor.ARGB32.red(this.decimal) / 255F;
        float green = FastColor.ARGB32.green(this.decimal) / 255F;
        float blue = FastColor.ARGB32.blue(this.decimal) / 255F;

        if (alpha > 0) {
            return new float[] {alpha, red, green, blue};
        } else {
            return new float[] {red, green, blue};
        }
    }
}
