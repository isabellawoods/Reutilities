package melonystudios.reutilities.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.neoforged.neoforge.common.Tags;

public class ReCraftingMenu extends CraftingMenu {
    private final ContainerLevelAccess access;

    public ReCraftingMenu(int containerID, Inventory playerInventory, ContainerLevelAccess access) {
        super(containerID, playerInventory, access);
        this.access = access;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.access.evaluate((world, pos) -> world.getBlockState(pos).is(Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES) && player.canInteractWithBlock(pos, 4), true);
    }
}
