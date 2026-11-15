package melonystudios.behaviorapi.settings;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/// Abstract class representing a codec for an **[item behavior][melonystudios.behaviorapi.ItemBehavior]'s individual settings**.
public interface IndividualSettings<T extends IndividualSettings<T>> {
    /// A [map codec][MapCodec] for the behavior's custom settings.
    MapCodec<T> codec();

    /// A [stream codec][StreamCodec] for the behavior's custom settings.
    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}
