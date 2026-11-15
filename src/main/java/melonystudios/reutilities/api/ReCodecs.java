package melonystudios.reutilities.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;

import java.util.HexFormat;
import java.util.Locale;
import java.util.function.Function;

/// Utility class that houses various codecs and stream codecs.
public class ReCodecs {
    public static final Codec<Integer> RGB_CODEC = Codec.withAlternative(hexColor(6), ExtraCodecs.intRange(0, 16777215));
    public static final Codec<Integer> ARGB_CODEC = Codec.withAlternative(hexColor(8), Codec.INT);
    public static final Codec<Integer> OPTIONAL_ARGB_CODEC = Codec.withAlternative(RGB_CODEC, ARGB_CODEC);
    public static final Codec<SoundSource> SOUND_SOURCE_CODEC = Codec.stringResolver(source -> source != null ? source.getName() : null, name -> {
        try {
            return SoundSource.valueOf(name.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }).validate(ReCodecs::validadeSoundSource);
    public static final StreamCodec<ByteBuf, SoundSource> SOUND_SOURCE_STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(Enum::ordinal, SoundSource.values(), ByIdMap.OutOfBoundsStrategy.ZERO), Enum::ordinal);
    public static final StreamCodec<ByteBuf, EquipmentSlot> EQUIPMENT_SLOT_STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(Enum::ordinal, EquipmentSlot.values(), ByIdMap.OutOfBoundsStrategy.ZERO), Enum::ordinal);
    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE, Vec3::x, ByteBufCodecs.DOUBLE, Vec3::y, ByteBufCodecs.DOUBLE, Vec3::z, Vec3::new);

    /// Creates a [float codec][Codec#FLOAT] that has a specified range.
    /// @param min The minimum bound for this codec.
    /// @param max The maximum bound for this codec.
    public static Codec<Float> floatRange(float min, float max) {
        return floatRange(min, max, value -> Component.translatable("logger.reutilities.outside_bounds", min, max, value).getString());
    }

    /// Creates a [float codec][Codec#FLOAT] that has a specified range.
    /// @param min The minimum bound for this codec.
    /// @param max The maximum bound for this codec.
    /// @param errorMessage A function to get the error message for when the codec gets a value outside its bounds.
    public static Codec<Float> floatRange(float min, float max, Function<Float, String> errorMessage) {
        return Codec.FLOAT.validate(value -> value.compareTo(min) >= 0 && value.compareTo(max) <= 0 ? DataResult.success(value) : DataResult.error(() -> errorMessage.apply(value)));
    }

    /// Creates a codec for a hexadecimal color with a customizable amount of digits.
    /// @author [ExtraCodecs] in Minecraft 26.2.
    /// @param expectedDigits How many digits this codec accepts.
    public static Codec<Integer> hexColor(int expectedDigits) {
        long maxValue = (1L << expectedDigits * 4) - 1L;

        return Codec.STRING.comapFlatMap(string -> {
            if (!string.startsWith("#")) {
                return DataResult.error(() -> "Hex color must begin with #");
            } else {
                int digits = string.length() - "#".length();
                if (digits != expectedDigits) {
                    return DataResult.error(() -> "Hex color is the wrong size — expected " + expectedDigits + " digits but got " + digits);
                } else {
                    try {
                        long value = HexFormat.fromHexDigitsToLong(string, "#".length(), string.length());
                        return value >= 0L && value <= maxValue ? DataResult.success((int) value) : DataResult.error(() -> "Color value is out of range: " + string);
                    } catch (NumberFormatException ignored) {
                        return DataResult.error(() -> "Invalid color value: " + string);
                    }
                }
            }
        }, value -> "#" + HexFormat.of().toHexDigits(value, expectedDigits));
    }

    private static DataResult<SoundSource> validadeSoundSource(SoundSource source) {
        return source == null ? DataResult.error(() -> "The provided sound source does not exist") : DataResult.success(source);
    }
}
