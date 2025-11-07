package melonystudios.reutilities.component.custom;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public enum TooltipStyle implements StringRepresentable {
    NONE(0, "none", (translation, adder) -> {}),
    OUTFIT(1, "outfit", (translation, adder) -> adder.accept(Component.translatable("tooltip.reutilities.character", translation).withStyle(ChatFormatting.DARK_GRAY))),
    CHARACTER(2, "character", (translation, adder) -> adder.accept(Component.translatable("tooltip.reutilities.outfit", translation).withStyle(ChatFormatting.DARK_GRAY))),
    PLAYER(3, "player", (translation, adder) -> adder.accept(Component.translatable("tooltip.reutilities.player", translation).withStyle(ChatFormatting.DARK_GRAY))),
    DESIGN(4, "design", (translation, adder) -> {
        adder.accept(Component.translatable("tooltip.reutilities.design").withStyle(ChatFormatting.GRAY));
        adder.accept(CommonComponents.space().append(translation).withStyle(ChatFormatting.BLUE));
    });

    public static final IntFunction<TooltipStyle> BY_ID = ByIdMap.continuous(TooltipStyle::getID, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<TooltipStyle> CODEC = StringRepresentable.fromValues(TooltipStyle::values);
    public static final StreamCodec<ByteBuf, TooltipStyle> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, TooltipStyle::getID);
    private final int id;
    private final String name;
    private final BiConsumer<Component, Consumer<Component>> tooltip;

    TooltipStyle(int id, String name, BiConsumer<Component, Consumer<Component>> tooltip) {
        this.id = id;
        this.name = name;
        this.tooltip = tooltip;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public BiConsumer<Component, Consumer<Component>> getTooltip() {
        return this.tooltip;
    }
}
