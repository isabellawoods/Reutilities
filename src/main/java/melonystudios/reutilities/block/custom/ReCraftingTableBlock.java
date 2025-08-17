package melonystudios.reutilities.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.container.custom.ReCraftingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ReCraftingTableBlock extends CraftingTableBlock {
    public static final MapCodec<? extends ReCraftingTableBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("container_title").forGetter(block -> block.containerTitle), propertiesCodec()).apply(instance, ReCraftingTableBlock::new));
    private final Component containerTitle;

    public ReCraftingTableBlock(Properties properties) {
        this(Component.translatable("container.crafting"), properties);
    }

    public ReCraftingTableBlock(Component containerTitle, Properties properties) {
        super(properties);
        this.containerTitle = containerTitle;
    }

    @Override
    @NotNull
    public MapCodec<? extends ReCraftingTableBlock> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    protected MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        return new SimpleMenuProvider((containerID, playerInventory, player) -> new ReCraftingMenu(containerID, playerInventory, ContainerLevelAccess.create(world, pos)), this.containerTitle);
    }
}
