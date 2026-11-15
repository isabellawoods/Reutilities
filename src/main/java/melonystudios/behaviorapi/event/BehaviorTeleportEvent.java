package melonystudios.behaviorapi.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

/// `BehaviorTeleportEvent` is fired before a `LivingEntity` is teleported by a [Teleport Entity item behavior][melonystudios.behaviorapi.custom.TeleportEntityBehavior].
///
/// This event is [cancelable][ICancellableEvent]. When canceled, the entity will not be teleported.
///
/// This event is only fired on the **logical server side**.
public class BehaviorTeleportEvent extends EntityTeleportEvent implements ICancellableEvent {
    private final ItemStack stack;
    private final Level level;
    private final LivingEntity targetEntity;

    /// `BehaviorTeleportEvent` is fired before a `LivingEntity` is teleported by a [Teleport Entity item behavior][melonystudios.behaviorapi.custom.TeleportEntityBehavior].
    ///
    /// This event is [cancelable][ICancellableEvent]. When canceled, the entity will not be teleported.
    ///
    /// This event is only fired on the **logical server side**.
    /// @param stack The item stack with the `behaviorapi:behaviors` component.
    /// @param level The world.
    /// @param targetEntity The entity running the effect.
    /// @param targetX The x-coordinate of the teleport target.
    /// @param targetY The y-coordinate of the teleport target.
    /// @param targetZ The z-coordinate of the teleport target.
    public BehaviorTeleportEvent(ItemStack stack, Level level, LivingEntity targetEntity, double targetX, double targetY, double targetZ) {
        super(targetEntity, targetX, targetY, targetZ);
        this.stack = stack;
        this.level = level;
        this.targetEntity = targetEntity;
    }

    /// @return The item stack with the `behaviorapi:behaviors` component.
    public ItemStack getStack() {
        return this.stack;
    }

    public Level getLevel() {
        return this.level;
    }

    public LivingEntity getTargetEntity() {
        return this.targetEntity;
    }

    /// `RandomTeleport` is fired before a `LivingEntity` is randomly teleported by a
    /// [Teleport Entity item behavior][melonystudios.behaviorapi.custom.TeleportEntityBehavior].
    ///
    /// This event is [cancelable][ICancellableEvent]. When canceled, the entity will not be teleported.
    ///
    /// This event is only fired on the **logical server side**.
    public static class RandomTeleport extends BehaviorTeleportEvent {
        private final float teleportDiameter;

        /// `RandomTeleport` is fired before a `LivingEntity` is randomly teleported by a
        /// [Teleport Entity item behavior][melonystudios.behaviorapi.custom.TeleportEntityBehavior].
        ///
        /// This event is [cancelable][ICancellableEvent]. When canceled, the entity will not be teleported.
        ///
        /// This event is only fired on the **logical server side**.
        /// @param stack The item stack with the `behaviorapi:behaviors` component.
        /// @param level The world.
        /// @param targetEntity The entity running the effect.
        /// @param targetX The x-coordinate of the teleport target.
        /// @param targetY The y-coordinate of the teleport target.
        /// @param targetZ The z-coordinate of the teleport target.
        /// @param teleportDiameter The diameter that the entity can teleport within.
        public RandomTeleport(ItemStack stack, Level level, LivingEntity targetEntity, double targetX, double targetY, double targetZ, float teleportDiameter) {
            super(stack, level, targetEntity, targetX, targetY, targetZ);
            this.teleportDiameter = teleportDiameter;
        }

        /// @return The diameter that the entity can teleport within.
        public float getTeleportDiameter() {
            return this.teleportDiameter;
        }
    }
}
