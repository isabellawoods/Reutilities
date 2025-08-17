package melonystudios.reutilities.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/// Represents a single boat type, used by *Reutilities*' boats to save and render the correct wood type.
/// @param boat The boat item.
/// @param chestBoat The boat with chest item.
/// @param woodType A resource location of the boat's wood type, like `minecraft:oak`.
public record BoatType(Supplier<Item> boat, Supplier<Item> chestBoat, ResourceLocation woodType) {
    public static final Codec<BoatType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("boat", Items.OAK_BOAT).forGetter(type -> type.boat().get()),
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("chest_boat", Items.OAK_CHEST_BOAT).forGetter(type -> type.chestBoat().get()),
            ResourceLocation.CODEC.optionalFieldOf("wood_type", ResourceLocation.withDefaultNamespace("oak")).forGetter(BoatType::woodType)
    ).apply(instance, (boat, chestBoat, woodType) -> new BoatType(() -> boat, () -> chestBoat, woodType)));

    @Override
    @NotNull
    public  String toString() {
        return this.woodType.toString();
    }
}
