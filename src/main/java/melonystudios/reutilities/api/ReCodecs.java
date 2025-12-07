package melonystudios.reutilities.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;
import java.util.function.Function;

/// Utility class that houses various codecs and stream codecs.
public class ReCodecs {
    public static final Codec<Integer> HEX_INT_CODEC = new HexadecimalIntCodec();
    public static final Codec<SoundSource> SOUND_SOURCE_CODEC = Codec.stringResolver(SoundSource::getName, name -> SoundSource.valueOf(name.toUpperCase(Locale.ENGLISH)));
    public static final StreamCodec<ByteBuf, SoundSource> SOUND_SOURCE_STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(Enum::ordinal, SoundSource.values(), ByIdMap.OutOfBoundsStrategy.ZERO), Enum::ordinal);
    public static final StreamCodec<ByteBuf, EquipmentSlot> EQUIPMENT_SLOT_STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(Enum::ordinal, EquipmentSlot.values(), ByIdMap.OutOfBoundsStrategy.ZERO), Enum::ordinal);
    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE, Vec3::x, ByteBufCodecs.DOUBLE, Vec3::y, ByteBufCodecs.DOUBLE, Vec3::z, Vec3::new);

    /// Creates a {@linkplain Codec#FLOAT float codec} that has a specified range.
    /// @param min The minimum bound for this codec.
    /// @param max The maximum bound for this codec.
    public static Codec<Float> floatRange(float min, float max) {
        return floatRange(min, max, value -> Component.translatable("logger.reutilities.outside_bounds", min, max, value).getString());
    }

    /// Creates a {@linkplain Codec#FLOAT float codec} that has a specified range.
    /// @param min The minimum bound for this codec.
    /// @param max The maximum bound for this codec.
    /// @param errorMessage A function to get the error message for when the codec gets a value outside its bounds.
    public static Codec<Float> floatRange(float min, float max, Function<Float, String> errorMessage) {
        return Codec.FLOAT.validate(value -> value.compareTo(min) >= 0 && value.compareTo(max) <= 0 ? DataResult.success(value) : DataResult.error(() -> errorMessage.apply(value)));
    }

    /// Creates a {@linkplain #HEX_INT_CODEC hexadecimal integer codec} that has a specified range.
    /// @param min The minimum bound for this codec.
    /// @param max The maximum bound for this codec.
    public static Codec<Integer> hexadecimalRange(int min, int max) {
        return hexadecimalRange(min, max, value -> Component.translatable("logger.reutilities.outside_bounds", min, max, value).getString());
    }

    /// Creates a {@linkplain #HEX_INT_CODEC hexadecimal integer codec} that has a specified range.
    /// @param min The minimum bound for this codec.
    /// @param max The maximum bound for this codec.
    /// @param errorMessage A function to get the error message for when the codec gets a value outside its bounds.
    public static Codec<Integer> hexadecimalRange(int min, int max, Function<Integer, String> errorMessage) {
        return HEX_INT_CODEC.validate(value -> value.compareTo(min) >= 0 && value.compareTo(max) <= 0 ? DataResult.success(value) : DataResult.error(() -> errorMessage.apply(value)));
    }

    public static class HexadecimalIntCodec implements PrimitiveCodec<Integer> {
        @Override
        public <T> DataResult<Integer> read(DynamicOps<T> ops, T input) {
            DataResult<Integer> data = ops.getNumberValue(input).map(Number::intValue);
            if (data.isError()) return ops.getStringValue(input).map(Integer::decode);
            return data;
        }

        @Override
        public <T> T write(DynamicOps<T> ops, Integer color) {
            return ops.createString("#" + Integer.toHexString(color));
        }

        @Override
        public String toString() {
            return "HexadecimalInt";
        }
    }
}
