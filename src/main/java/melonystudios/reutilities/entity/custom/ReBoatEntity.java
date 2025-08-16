package melonystudios.reutilities.entity.custom;

import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.ReEntities;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReBoatEntity extends Boat implements BoatVariant {
    private static final EntityDataAccessor<String> WOOD_TYPE = SynchedEntityData.defineId(ReBoatEntity.class, EntityDataSerializers.STRING);

    public ReBoatEntity(EntityType<? extends Boat> boat, Level world) {
        super(boat, world);
    }

    public ReBoatEntity(Level world, double x, double y, double z) {
        this(ReEntities.BOAT.get(), world);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WOOD_TYPE, "oak");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Type", Tag.TAG_STRING)) {
            this.setBoatType(Reconstants.byWoodType(tag.getString("Type")));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("Type", this.getBoatType().woodType().toString());
    }

    @Override
    public void setBoatType(BoatType type) {
        this.entityData.set(WOOD_TYPE, type.woodType().toString());
    }

    @Override
    public BoatType getBoatType() {
        return Reconstants.byWoodType(this.entityData.get(WOOD_TYPE));
    }

    @Override
    @NotNull
    public Item getDropItem() {
        return this.getBoatType().boat().get();
    }

    @Override
    @Nullable
    public ItemStack getPickedResult(HitResult target) {
        BoatType type = this.getBoatType();
        ItemStack boatStack = new ItemStack(type.boat().get());
        boatStack.set(ReDataComponents.WOOD_TYPE, type.woodType());
        return boatStack;
    }
}
