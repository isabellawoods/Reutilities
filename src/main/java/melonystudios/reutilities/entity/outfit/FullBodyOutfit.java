package melonystudios.reutilities.entity.outfit;

import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.util.ReRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Optional;

/// Attachment type used for applying full-body outfits on any living entity.
/// @see melonystudios.reutilities.api.ReAPI#OUTFIT_CAPABILITY ReAPI.OUTFIT_CAPABILITY
public class FullBodyOutfit implements INBTSerializable<StringTag> {
    public static final SyncHandler SYNC_HANDLER = new SyncHandler();
    private Holder<OutfitDefinition> definition;

    public FullBodyOutfit(IAttachmentHolder holder, Holder<OutfitDefinition> definition) {
        if (!(holder instanceof LivingEntity)) {
            throw new IllegalArgumentException(ReAPI.translate("logger.reutilities.wrong_holder_type.outfit", "Holder entity must be an instance of LivingEntity"));
        }
        this.definition = definition;
    }

    /// @return A holder of an {@linkplain OutfitDefinition outfit definition} for this attachment. Used to render the full-body outfit.
    public Holder<OutfitDefinition> definition() {
        return this.definition;
    }

    /// Sets this attachment's definition holder to the one provided.
    /// @param definition The outfit definition.
    public void setDefinition(Holder<OutfitDefinition> definition) {
        this.definition = definition;
    }

    @Override
    @UnknownNullability
    public StringTag serializeNBT(HolderLookup.Provider provider) {
        if (!(this.definition() instanceof Holder.Direct<OutfitDefinition>)) {
            return this.definition().unwrapKey().map(definitionKey -> StringTag.valueOf(definitionKey.location().toString())).orElse(null);
        }
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, StringTag tag) {
        Optional.ofNullable(ResourceLocation.tryParse(tag.getAsString()))
                .map(value -> ResourceKey.create(ReRegistries.OUTFIT_DEFINITION, value))
                .flatMap(definitionKey -> provider.lookupOrThrow(ReRegistries.OUTFIT_DEFINITION).get(definitionKey))
                .ifPresent(this::setDefinition);
    }

    public static class SyncHandler implements AttachmentSyncHandler<FullBodyOutfit> {
        @Override
        public void write(RegistryFriendlyByteBuf buffer, FullBodyOutfit outfit, boolean initialSync) {
            OutfitDefinition.STREAM_CODEC.encode(buffer, outfit.definition());
        }

        @Override
        @Nullable
        public FullBodyOutfit read(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer, @Nullable FullBodyOutfit previousValue) {
            return new FullBodyOutfit(holder, OutfitDefinition.STREAM_CODEC.decode(buffer));
        }
    }
}
