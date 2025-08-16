package melonystudios.reutilities.entity;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.entity.custom.ReBoatEntity;
import melonystudios.reutilities.entity.custom.ReChestBoatEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Reutilities.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ReBoatEntity>> BOAT = ENTITIES.register("boat", () ->
            EntityType.Builder.<ReBoatEntity>of(ReBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10).build(Reutilities.reutilities("boat").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ReChestBoatEntity>> CHEST_BOAT = ENTITIES.register("chest_boat", () ->
            EntityType.Builder.<ReChestBoatEntity>of(ReChestBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10).build(Reutilities.reutilities("chest_boat").toString()));
}
