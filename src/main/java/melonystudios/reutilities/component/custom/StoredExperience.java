package melonystudios.reutilities.component.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/// Stores a non-negative amount of experience points or levels.
/// @param amount The amount of experience, stored as a non-negative integer.
/// @param levels Whether this is storing experience levels instead of points.
public record StoredExperience(int amount, boolean levels) implements TooltipProvider {
    public static final Codec<StoredExperience> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("amount", 0).forGetter(StoredExperience::amount),
            Codec.BOOL.optionalFieldOf("levels", false).forGetter(StoredExperience::levels)
    ).apply(instance, StoredExperience::new));
    public static final Codec<StoredExperience> CODEC = Codec.withAlternative(FULL_CODEC, Codec.INT, StoredExperience::new);
    public static final StreamCodec<ByteBuf, StoredExperience> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, StoredExperience::amount,
            ByteBufCodecs.BOOL, StoredExperience::levels,
            StoredExperience::new
    );

    /// Stores a non-negative amount of experience points.
    /// @param amount The amount of experience, stored as a non-negative integer.
    public StoredExperience(int amount) {
        this(amount, false);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flag) {
        adder.accept(Component.translatable("tooltip.reutilities.stored_experience",
                Component.translatable("tooltip.reutilities.stored_experience." + (this.levels() ? "levels" : "points"), this.amount()).withColor(0x80FF20)
        ).withStyle(ChatFormatting.GRAY));
    }

    @Override
    @NotNull
    public String toString() {
        return String.format("StoredExperience[amount=%s, levels=%s]", this.amount(), this.levels());
    }
}
