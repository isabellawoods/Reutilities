package melonystudios.reutilities.entity.cape;

import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.util.ReRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Optional;

/// Attachment type used for rendering {@linkplain Recape capes} on any* living entity.
/// @see ReAPI#CAPE_CAPABILITY ReAPI.CAPE_CAPABILITY
public class WornRecape implements INBTSerializable<StringTag> {
    public static final SyncHandler SYNC_HANDLER = new SyncHandler();
    private Holder<Recape> cape;

    public WornRecape(IAttachmentHolder holder, Holder<Recape> cape) {
        if (!(holder instanceof LivingEntity && holder instanceof OffsetGetter)) {
            throw new IllegalArgumentException(ReAPI.translate("logger.reutilities.wrong_holder_type.cape", "Holder must be an instance of LivingEntity and OffsetGetter"));
        }
        this.cape = cape;
    }

    /// @return A holder of a {@linkplain Recape cape} for this attachment. Used to render the cape on the wearer's body.
    public Holder<Recape> cape() {
        return this.cape;
    }

    /// Sets this attachment's cape holder to the one provided.
    /// @param cape The *Reutilities* cape.
    public void setCape(Holder<Recape> cape) {
        this.cape = cape;
    }

    @Override
    @UnknownNullability
    public StringTag serializeNBT(HolderLookup.Provider provider) {
        if (!(this.cape() instanceof Holder.Direct<Recape>)) {
            return this.cape().unwrapKey().map(capeKey -> StringTag.valueOf(capeKey.location().toString())).orElse(null);
        }
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, StringTag tag) {
        Optional.ofNullable(ResourceLocation.tryParse(tag.getAsString()))
                .map(value -> ResourceKey.create(ReRegistries.CAPE, value))
                .flatMap(capeKey -> provider.lookupOrThrow(ReRegistries.CAPE).get(capeKey))
                .ifPresent(this::setCape);
    }

    /// Moves the cape when the entity moves.
    /// @param entity The entity. For this use case, it must extend {@link LivingEntity}.
    /// @param offsets The offsets sed to move the cape with the entity's movement.
    public void moveCape(Entity entity, CapeOffsets offsets) {
        offsets.capeXOld = offsets.capeX;
        offsets.capeYOld = offsets.capeY;
        offsets.capeZOld = offsets.capeZ;

        double xDiff = entity.getX() - offsets.capeX;
        double yDiff = entity.getY() - offsets.capeY;
        double zDiff = entity.getZ() - offsets.capeZ;
        if (xDiff > 10) {
            offsets.capeX = entity.getX();
            offsets.capeXOld = offsets.capeX;
        }

        if (zDiff > 10) {
            offsets.capeZ = entity.getZ();
            offsets.capeZOld = offsets.capeZ;
        }

        if (yDiff > 10) {
            offsets.capeY = entity.getY();
            offsets.capeYOld = offsets.capeY;
        }

        if (xDiff < -10) {
            offsets.capeX = entity.getX();
            offsets.capeXOld = offsets.capeX;
        }

        if (zDiff < -10) {
            offsets.capeZ = entity.getZ();
            offsets.capeZOld = offsets.capeZ;
        }

        if (yDiff < -10) {
            offsets.capeY = entity.getY();
            offsets.capeYOld = offsets.capeY;
        }

        offsets.capeX += xDiff * 0.25;
        offsets.capeZ += zDiff * 0.25;
        offsets.capeY += yDiff * 0.25;
    }

    public static class SyncHandler implements AttachmentSyncHandler<WornRecape> {
        @Override
        public void write(RegistryFriendlyByteBuf buffer, WornRecape cape, boolean initialSync) {
            Recape.STREAM_CODEC.encode(buffer, cape.cape());
        }

        @Override
        @Nullable
        public WornRecape read(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer, @Nullable WornRecape previousValue) {
            return new WornRecape(holder, Recape.STREAM_CODEC.decode(buffer));
        }
    }
}
