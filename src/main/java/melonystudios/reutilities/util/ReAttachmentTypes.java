package melonystudios.reutilities.util;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.entity.cape.WornRecape;
import melonystudios.reutilities.entity.outfit.FullBodyOutfit;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ReAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Reutilities.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FullBodyOutfit>> FULL_BODY_OUTFIT = ATTACHMENTS.register("full_body_outfit",
            () -> AttachmentType.serializable(holder -> new FullBodyOutfit(holder, Holder.direct(null))).sync(FullBodyOutfit.SYNC_HANDLER).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<WornRecape>> CAPE = ATTACHMENTS.register("cape",
            () -> AttachmentType.serializable(holder -> new WornRecape(holder, Holder.direct(null))).sync(WornRecape.SYNC_HANDLER).build());
}
