package melonystudios.reutilities.util;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.BoatType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// Contains various constants, maps and methods used across <i>Reutilities</i>.
public class Reconstants {
    public static final List<Block> SIGNS = new ArrayList<>();
    public static final List<Block> HANGING_SIGNS = new ArrayList<>();
    public static final Map<String, BoatType> BOATS = new HashMap<>();
    /// Represents the default "<code>OAK</code>" boat type, for when there are no registered boat types.
    public static final BoatType OAK = new BoatType(() -> Items.OAK_BOAT, () -> Items.OAK_CHEST_BOAT, ResourceLocation.withDefaultNamespace("oak"));

    /// Gets a {@linkplain BoatType boat type} from a string.
    /// @param woodType A string containing the wood type to get, usually a resource location.
    public static BoatType byWoodType(String woodType) {
        return byWoodType(woodType, OAK);
    }

    /// Gets a {@linkplain BoatType boat type} from a string.
    /// @param woodType A string containing the wood type to get, usually a resource location.
    /// @param defaultType A default boat type if the requested type doesn't exist in the map.
    public static BoatType byWoodType(String woodType, BoatType defaultType) {
        return BOATS.getOrDefault(woodType, defaultType);
    }

    public static ResourceLocation pulling() {
        return ResourceLocation.withDefaultNamespace("pulling");
    }

    public static ResourceLocation pullProgress() {
        return ResourceLocation.withDefaultNamespace("pull");
    }

    public static ResourceLocation charged() {
        return ResourceLocation.withDefaultNamespace("charged");
    }

    public static ResourceLocation fireworkRocketLoaded() {
        return ResourceLocation.withDefaultNamespace("firework");
    }

    public static ResourceLocation blocking() {
        return ResourceLocation.withDefaultNamespace("blocking");
    }

    public static ResourceLocation trimType() {
        return ResourceLocation.withDefaultNamespace("trim_type");
    }

    public static ResourceLocation broken() {
        return ResourceLocation.withDefaultNamespace("broken");
    }

    public static ResourceLocation textureID() {
        return Reutilities.reutilities("texture_id");
    }
}
