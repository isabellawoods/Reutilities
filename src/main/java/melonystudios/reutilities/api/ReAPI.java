package melonystudios.reutilities.api;

import melonystudios.reutilities.util.Reconstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.List;

/// <i>Reutilities</i>' API class, used for some of my mods to register mainly boats and signs.
public class ReAPI {
    /// Adds a boat and chest boat to <i>Reutilities</i>' boat map.
    /// @param type The boat type to add.
    public static void addBoat(BoatType type) {
        Reconstants.BOATS.put(type.woodType().toString(), type);
    }

    /// Adds a boat and chest boat to <i>Reutilities</i>' boat map.
    /// @param boat The boat item.
    /// @param chestBoat The boat with chest item.
    /// @param woodType A resource location of the boat's wood type, like <code>minecraft:oak</code>.
    public static void addBoat(ItemLike boat, ItemLike chestBoat, ResourceLocation woodType) {
        Reconstants.BOATS.put(woodType.toString(), new BoatType(boat, chestBoat, woodType));
    }

    /// Adds signs to the valid list of blocks of the {@link melonystudios.reutilities.blockentity.ReBlockEntities#SIGN SIGN} block entity.
    /// @param signs An array of signs to add.
    public static void addSigns(Block... signs) {
        Reconstants.SIGNS.addAll(List.of(signs));
    }

    /// Adds hanging signs to the valid list of blocks of the {@link melonystudios.reutilities.blockentity.ReBlockEntities#HANGING_SIGN HANGING_SIGN} block entity.
    /// @param hangingSigns An array of hanging signs to add.
    public static void addHangingsSigns(Block... hangingSigns) {
        Reconstants.HANGING_SIGNS.addAll(List.of(hangingSigns));
    }
}
