package melonystudios.reutilities.util;

import melonystudios.reutilities.api.BoatType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class ReBoats {
    /// Represents the default `OAK` boat type, for when there are no registered boat types.
    public static final BoatType OAK = new BoatType(() -> Items.OAK_BOAT, () -> Items.OAK_CHEST_BOAT, ResourceLocation.withDefaultNamespace("oak"));
    public static final BoatType SPRUCE = new BoatType(() -> Items.SPRUCE_BOAT, () -> Items.SPRUCE_CHEST_BOAT, ResourceLocation.withDefaultNamespace("spruce"));
    public static final BoatType BIRCH = new BoatType(() -> Items.BIRCH_BOAT, () -> Items.BIRCH_CHEST_BOAT, ResourceLocation.withDefaultNamespace("birch"));
    public static final BoatType JUNGLE = new BoatType(() -> Items.JUNGLE_BOAT, () -> Items.JUNGLE_CHEST_BOAT, ResourceLocation.withDefaultNamespace("jungle"));
    public static final BoatType ACACIA = new BoatType(() -> Items.ACACIA_BOAT, () -> Items.ACACIA_CHEST_BOAT, ResourceLocation.withDefaultNamespace("acacia"));
    public static final BoatType DARK_OAK = new BoatType(() -> Items.DARK_OAK_BOAT, () -> Items.DARK_OAK_CHEST_BOAT, ResourceLocation.withDefaultNamespace("dark_oak"));
    public static final BoatType MANGROVE = new BoatType(() -> Items.MANGROVE_BOAT, () -> Items.MANGROVE_CHEST_BOAT, ResourceLocation.withDefaultNamespace("mangrove"));
    public static final BoatType BAMBOO = new BoatType(() -> Items.BAMBOO_RAFT, () -> Items.BAMBOO_CHEST_RAFT, true, ResourceLocation.withDefaultNamespace("bamboo"));
    public static final BoatType CHERRY = new BoatType(() -> Items.CHERRY_BOAT, () -> Items.CHERRY_CHEST_BOAT, ResourceLocation.withDefaultNamespace("cherry"));
}
