package melonystudios.behaviorapi.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

/// `BehaviorTeleportEvent` is fired before a LivingEntity is teleported from a {@linkplain melonystudios.behaviorapi.custom.TeleportEntityBehavior Teleport Entity item behavior}.
///
/// This event is {@linkplain ICancellableEvent cancellable}. When cancelled, the entity will not be teleported.
///
/// This event is only fired on the {@link net.neoforged.fml.LogicalSide#SERVER LogicalSide#SERVER} side.
public class BehaviorTeleportEvent extends EntityTeleportEvent implements ICancellableEvent {
    private final ItemStack stack;
    private final Level world;
    private final LivingEntity livEntity;

    /// `BehaviorTeleportEvent` is fired before a LivingEntity is teleported from a {@linkplain melonystudios.behaviorapi.custom.TeleportEntityBehavior Teleport Entity item behavior}.
    ///
    /// This event is {@linkplain ICancellableEvent cancellable}. When cancelled, the entity will not be teleported.
    ///
    /// This event is only fired on the {@link net.neoforged.fml.LogicalSide#SERVER LogicalSide#SERVER} side.
    /// @param stack The item stack with the `reutilities:behaviors` component.
    /// @param world The world.
    /// @param livEntity The entity running the effect.
    /// @param targetX The x-position of the target.
    /// @param targetY The y-position of the target.
    /// @param targetZ The z-position of the target.
    public BehaviorTeleportEvent(ItemStack stack, Level world, LivingEntity livEntity, double targetX, double targetY, double targetZ) {
        super(livEntity, targetX, targetY, targetZ);
        this.stack = stack;
        this.world = world;
        this.livEntity = livEntity;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public Level getWorld() {
        return this.world;
    }

    public LivingEntity getTargetEntity() {
        return this.livEntity;
    }

    /// `BehaviorTeleportEvent.RandomTeleport` is fired before a LivingEntity randomly teleported from a
    /// {@linkplain melonystudios.behaviorapi.custom.TeleportEntityBehavior Teleport Entity item behavior}.
    ///
    /// This event is {@linkplain ICancellableEvent cancellable}. When cancelled, the entity will not be teleported.
    ///
    /// This event is only fired on the {@link net.neoforged.fml.LogicalSide#SERVER LogicalSide#SERVER} side.
    public static class RandomTeleport extends BehaviorTeleportEvent {
        private final float teleportDiameter;

        /// `BehaviorTeleportEvent.RandomTeleport` is fired before a LivingEntity randomly teleported from a
        /// {@linkplain melonystudios.behaviorapi.custom.TeleportEntityBehavior Teleport Entity item behavior}.
        ///
        /// This event is {@linkplain ICancellableEvent cancellable}. When cancelled, the entity will not be teleported.
        ///
        /// This event is only fired on the {@link net.neoforged.fml.LogicalSide#SERVER LogicalSide#SERVER} side.
        /// @param stack The item stack with the `reutilities:behaviors` component.
        /// @param world The world.
        /// @param livEntity The entity running the effect.
        /// @param targetX The x-position of the target.
        /// @param targetY The y-position of the target.
        /// @param targetZ The z-position of the target.
        /// @param teleportDiameter The diameter that the entity can teleport within.
        public RandomTeleport(ItemStack stack, Level world, LivingEntity livEntity, double targetX, double targetY, double targetZ, float teleportDiameter) {
            super(stack, world, livEntity, targetX, targetY, targetZ);
            this.teleportDiameter = teleportDiameter;
        }

        public float getTeleportDiameter() {
            return this.teleportDiameter;
        }
    }
}
