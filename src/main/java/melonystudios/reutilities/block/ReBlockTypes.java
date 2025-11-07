package melonystudios.reutilities.block;

import com.mojang.serialization.MapCodec;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.block.custom.ReBookshelfBlock;
import melonystudios.reutilities.block.custom.ReCraftingTableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReBlockTypes {
    public static final DeferredRegister<MapCodec<? extends Block>> TYPES = DeferredRegister.create(Registries.BLOCK_TYPE, Reutilities.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<ReBookshelfBlock>> BOOKSHELF = TYPES.register("bookshelf", () -> ReBookshelfBlock.CODEC);
    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<? extends ReCraftingTableBlock>> CRAFTING_TABLE = TYPES.register("crafting_table", () -> ReCraftingTableBlock.CODEC);
}
