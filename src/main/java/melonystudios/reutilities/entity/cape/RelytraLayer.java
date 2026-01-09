package melonystudios.reutilities.entity.cape;

import melonystudios.reutilities.api.ReAPI;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class RelytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    public RelytraLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
        super(renderer, modelSet);
    }

    @Override
    @NotNull
    public ResourceLocation getElytraTexture(ItemStack stack, T livEntity) {
        WornRecape cape = livEntity.getCapability(ReAPI.CAPE_CAPABILITY);
        if (cape != null) return ReAPI.toTexturePath(cape.cape().value().elytraTexture());
        return super.getElytraTexture(stack, livEntity);
    }
}
