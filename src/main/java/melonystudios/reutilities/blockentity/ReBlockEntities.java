package melonystudios.reutilities.blockentity;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.blockentity.custom.ReHangingSignBlockEntity;
import melonystudios.reutilities.blockentity.custom.ReSignBlockEntity;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reutilities.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ReSignBlockEntity>> SIGN = BLOCK_ENTITIES.register("sign", () ->
            BlockEntityType.Builder.of(ReSignBlockEntity::new, Reconstants.SIGNS.toArray(new Block[0])).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ReHangingSignBlockEntity>> HANGING_SIGN = BLOCK_ENTITIES.register("hanging_sign", () ->
            BlockEntityType.Builder.of(ReHangingSignBlockEntity::new, Reconstants.HANGING_SIGNS.toArray(new Block[0])).build(null));
}
