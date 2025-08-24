package melonystudios.reutilities.component.custom;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum TooltipStyle implements StringRepresentable {
    NONE(0, "none"),
    OUTFIT(1, "outfit"),
    CHARACTER(2, "character"),
    DESIGN(3, "design", Style.EMPTY.withColor(ChatFormatting.BLUE));

    public static final IntFunction<TooltipStyle> BY_ID = ByIdMap.continuous(style -> style.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<TooltipStyle> CODEC = StringRepresentable.fromValues(TooltipStyle::values);
    public static final StreamCodec<ByteBuf, TooltipStyle> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, style -> style.id);
    private final int id;
    private final String name;
    private final Style style;

    TooltipStyle(int id, String name) {
        this(id, name, Style.EMPTY.withColor(ChatFormatting.GRAY));
    }

    TooltipStyle(int id, String name, Style style) {
        this.id = id;
        this.name = name;
        this.style = style;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    public Style getStyle() {
        return this.style;
    }
}
