package melonystudios.reutilities.mixin.item;

import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.custom.BoatVariant;
import melonystudios.reutilities.entity.custom.ReBoatEntity;
import melonystudios.reutilities.entity.custom.ReChestBoatEntity;
import melonystudios.reutilities.item.custom.ReBoatItem;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatItem.class)
public class ReBoatItemMixin extends Item {
    @Shadow
    @Final
    private boolean hasChest;

    public ReBoatItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getBoat", at = @At("HEAD"), cancellable = true)
    private void getBoat(Level world, HitResult result, ItemStack stack, Player player, CallbackInfoReturnable<Boat> callback) {
        if (stack.has(ReDataComponents.WOOD_TYPE)) {
            callback.cancel();
            Vec3 location = result.getLocation();
            Boat boat = this.hasChest ? new ReChestBoatEntity(world, location.x, location.y, location.z) : new ReBoatEntity(world, location.x, location.y, location.z);
            ((BoatVariant) boat).setBoatType(ReBoatItem.getBoatType(stack, Reconstants.OAK));
            if (world instanceof ServerLevel serverWorld) EntityType.<Boat>createDefaultStackConfig(serverWorld, stack, player).accept(boat);
            callback.setReturnValue(boat);
        }
    }
}
