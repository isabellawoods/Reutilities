package melonystudios.reutilities.entity.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.entity.custom.BoatVariant;
import melonystudios.reutilities.util.ReCommonConstants;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ReBoatRenderer extends BoatRenderer {
    private final Map<BoatType, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public ReBoatRenderer(EntityRendererProvider.Context context, boolean isChestBoat) {
        super(context, isChestBoat);
        this.boatResources = ReCommonConstants.BOATS.values().stream().collect(ImmutableMap.toImmutableMap(type -> type,
                type -> Pair.of(getBoatLocation(type, isChestBoat), this.createBoatModel(context, type, isChestBoat))));
    }

    private static ResourceLocation getBoatLocation(BoatType type, boolean isChestBoat) {
        return ResourceLocation.parse(String.format("%s:textures/entity/%s/%s.png", type.woodType().getNamespace(), isChestBoat ? "chest_boat" : "boat", type.woodType().getPath()));
    }

    private ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, BoatType type, boolean isChestBoat) {
        ModelLayerLocation layerLocation = isChestBoat ? ReBoatRenderer.createChestBoatModelName(type) : ReBoatRenderer.createBoatModelName(type);
        try {
            ModelPart part = context.bakeLayer(layerLocation);
            return this.getBoatModel(isChestBoat, type.raft(), part);
        } catch (IllegalArgumentException exception) {
            ModelPart part = isChestBoat ? ChestBoatModel.createBodyModel().bakeRoot() : BoatModel.createBodyModel().bakeRoot();
            return this.getBoatModel(isChestBoat, type.raft(), part);
        }
    }

    public ListModel<Boat> getBoatModel(boolean isChestBoat, boolean isRaft, ModelPart part) {
        if (isRaft) {
            return isChestBoat ? new ChestRaftModel(part) : new RaftModel(part);
        } else {
            return isChestBoat ? new ChestBoatModel(part) : new BoatModel(part);
        }
    }

    public static ModelLayerLocation createBoatModelName(BoatType type) {
        return createLocation(type.woodType().getNamespace() + ":boat/" + type.woodType().getPath(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(BoatType type) {
        return createLocation(type.woodType().getNamespace() + ":chest_boat/" + type.woodType().getPath(), "main");
    }

    private static ModelLayerLocation createLocation(String path, String modelName) {
        return new ModelLayerLocation(ResourceLocation.parse(path), modelName);
    }

    @Override
    @NotNull
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if (boat instanceof BoatVariant variant) {
            return this.boatResources.get(variant.getBoatType());
        } else {
            return null;
        }
    }
}
