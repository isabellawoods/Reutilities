package melonystudios.reutilities.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

/// Represents a single boat type, used by <i>Reutilities</i>' boats to save and render the correct wood type.
/// @param boat The boat item.
/// @param chestBoat The boat with chest item.
/// @param woodType A resource location of the boat's wood type, like <code>minecraft:oak</code>.
public record BoatType(ItemLike boat, ItemLike chestBoat, ResourceLocation woodType) {
    public static final Codec<BoatType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("boat", Items.OAK_BOAT).forGetter(type -> type.boat().asItem()),
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("chest_boat", Items.OAK_CHEST_BOAT).forGetter(type -> type.chestBoat().asItem()),
            ResourceLocation.CODEC.optionalFieldOf("wood_type", ResourceLocation.withDefaultNamespace("oak")).forGetter(BoatType::woodType)
    ).apply(instance, BoatType::new));

    @Override
    @NotNull
    public  String toString() {
        return this.woodType.toString();
    }
}
