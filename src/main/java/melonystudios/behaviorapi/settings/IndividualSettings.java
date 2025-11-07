package melonystudios.behaviorapi.settings;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/// Abstract class representing a codec for an **{@linkplain melonystudios.behaviorapi.ItemBehavior item behavior}'s individual settings**.
public interface IndividualSettings<T extends IndividualSettings<T>> {
    /// A {@linkplain MapCodec map codec} for the behavior's custom settings.
    MapCodec<T> codec();

    /// A {@linkplain StreamCodec stream codec} for the behavior's custom settings.
    StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
}
