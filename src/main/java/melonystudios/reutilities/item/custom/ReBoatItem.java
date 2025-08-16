package melonystudios.reutilities.item.custom;

import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.custom.BoatTypeGetter;
import melonystudios.reutilities.entity.custom.ReBoatEntity;
import melonystudios.reutilities.entity.custom.ReChestBoatEntity;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class ReBoatItem extends BoatItem {
    public static final Predicate<Entity> SPECTATORS_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    private final BoatType type;
    private final boolean hasChest;

    public ReBoatItem(boolean hasChest, BoatType type, Properties properties) {
        super(hasChest, null, properties.component(ReDataComponents.WOOD_TYPE, type.woodType()));
        this.type = type;
        this.hasChest = hasChest;
    }

    public boolean hasChest() {
        return this.hasChest;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack handStack = player.getItemInHand(hand);
        HitResult result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY);

        if (result.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(handStack);
        } else {
            Vec3 viewVector = player.getViewVector(1);
            List<Entity> entitiesList = world.getEntities(player, player.getBoundingBox().expandTowards(viewVector.scale(5)).inflate(1), SPECTATORS_PREDICATE);
            if (!entitiesList.isEmpty()) {
                Vec3 eyePosition = player.getEyePosition();

                for (Entity entity : entitiesList) {
                    AABB axisAlignedBB = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (axisAlignedBB.contains(eyePosition)) return InteractionResultHolder.pass(handStack);
                }
            }

            if (result.getType() == HitResult.Type.BLOCK) {
                Boat boat = this.getBoat(world, result, handStack, player);
                if (boat instanceof BoatTypeGetter getter) getter.setBoatType(this.getBoatType(handStack, this.type));
                boat.setYRot(player.getYRot());
                if (!world.noCollision(boat, boat.getBoundingBox())) {
                    return InteractionResultHolder.fail(handStack);
                } else {
                    if (!world.isClientSide) {
                        world.addFreshEntity(boat);
                        world.gameEvent(player, GameEvent.ENTITY_PLACE, result.getLocation());
                        handStack.consume(1, player);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(handStack, world.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass(handStack);
            }
        }
    }

    /// Gets the {@linkplain BoatType boat type} for placing down the boat.
    /// @param boatStack The boat item stack, used for getting the "{@link ReDataComponents#WOOD_TYPE reutilities:wood_type}" component.
    /// @param type The default wood type, for when the boat doesn't have component.
    public BoatType getBoatType(ItemStack boatStack, BoatType type) {
        return boatStack.has(ReDataComponents.WOOD_TYPE) ? Reconstants.byWoodType(boatStack.get(ReDataComponents.WOOD_TYPE).toString(), type) : type;
    }

    /// Gets the boat entity for placing down the boat.
    /// @param world The world.
    /// @param result Where the boat is going to be placed.
    /// @param stack The boat item stack, used to get the "{@link net.minecraft.core.component.DataComponents#ENTITY_DATA minecraft:entity_data}" component.
    /// @param player The player placing the boat.
    public Boat getBoat(Level world, HitResult result, ItemStack stack, Player player) {
        Vec3 location = result.getLocation();
        Boat boat = this.hasChest ? new ReChestBoatEntity(world, location.x, location.y, location.z) : new ReBoatEntity(world, location.x, location.y, location.z);
        if (world instanceof ServerLevel serverWorld) EntityType.<Boat>createDefaultStackConfig(serverWorld, stack, player).accept(boat);
        return boat;
    }
}
