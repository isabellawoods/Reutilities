package melonystudios.reutilities.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.util.ReCommonConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/// Represents a single boat type, used by *Reutilities*' boats to save and render the correct wood type.
/// @param boat The boat item.
/// @param chestBoat The boat with chest item.
/// @param raft Whether this boat type represents a raft.
/// @param woodType A resource location of the boat's wood type, like `minecraft:oak`.
public record BoatType(Supplier<Item> boat, Supplier<Item> chestBoat, boolean raft, ResourceLocation woodType) {
    public static final Codec<BoatType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("boat").forGetter(type -> type.boat().get()),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("chest_boat").forGetter(type -> type.chestBoat().get()),
            Codec.BOOL.optionalFieldOf("raft", false).forGetter(BoatType::raft),
            ResourceLocation.CODEC.fieldOf("wood_type").forGetter(BoatType::woodType)
    ).apply(instance, (boat, chestBoat, raft, woodType) -> new BoatType(() -> boat, () -> chestBoat, raft, woodType)));
    public static final Codec<ResourceLocation> WOOD_TYPE_CODEC = ResourceLocation.CODEC.validate(location -> ReCommonConstants.BOATS.containsKey(location)
            ? DataResult.success(location) : DataResult.error(() -> Component.translatable("logger.reutilities.wood_type.non_existent", location.toString()).getString()));

    /// Represents a single boat type, used by *Reutilities*' boats to save and render the correct wood type.
    /// @param boat The boat item.
    /// @param chestBoat The boat with chest item.
    /// @param woodType A resource location of the boat's wood type, like `minecraft:oak`.
    public BoatType(Supplier<Item> boat, Supplier<Item> chestBoat, ResourceLocation woodType) {
        this(boat, chestBoat, false, woodType);
    }

    @Override
    @NotNull
    public String toString() {
        return this.woodType().toString();
    }
}
