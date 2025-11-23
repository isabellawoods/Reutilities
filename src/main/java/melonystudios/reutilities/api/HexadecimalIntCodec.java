package melonystudios.reutilities.api;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

public class HexadecimalIntCodec implements PrimitiveCodec<Integer> {
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
