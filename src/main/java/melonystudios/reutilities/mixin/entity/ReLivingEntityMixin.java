package melonystudios.reutilities.mixin.entity;

import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.entity.cape.CapeOffsets;
import melonystudios.reutilities.entity.cape.OffsetGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ReLivingEntityMixin extends Entity implements ILivingEntityExtension, OffsetGetter {
    @Unique
    private CapeOffsets offsets;

    public ReLivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"))
    public void runEatingBehavior(Level world, ItemStack stack, FoodProperties properties, CallbackInfoReturnable<ItemStack> callback) {
        ReAPI.runItemBehavior(stack, world, this.self(), GlobalSettings.EATING);
    }

    @Override
    public CapeOffsets getOffsets() {
        if (this.offsets == null) this.offsets = new CapeOffsets();
        return this.offsets;
    }
}
