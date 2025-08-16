package melonystudios.reutilities.entity.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.entity.custom.ReBoatEntity;
import melonystudios.reutilities.entity.custom.ReChestBoatEntity;
import melonystudios.reutilities.util.Reconstants;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
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
        this.boatResources = Reconstants.BOATS.values().stream().collect(ImmutableMap.toImmutableMap(type -> type,
                type -> Pair.of(getBoatLocation(type, isChestBoat), this.createBoatModel(context, type, isChestBoat))));
    }

    private static ResourceLocation getBoatLocation(BoatType type, boolean isChestBoat) {
        return ResourceLocation.parse(String.format("%s:textures/entity/%s/%s.png", type.woodType().getNamespace(), isChestBoat ? "chest_boat" : "boat", type.woodType().getPath()));
    }

    private ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, BoatType type, boolean isChestBoat) {
        ModelLayerLocation layerLocation = isChestBoat ? ReBoatRenderer.createChestBoatModelName(type) : ReBoatRenderer.createBoatModelName(type);
        ModelPart part = context.bakeLayer(layerLocation);
        return isChestBoat ? new ChestBoatModel(part) : new BoatModel(part);
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
        if (boat instanceof ReBoatEntity reBoat) {
            return this.boatResources.get(reBoat.getBoatType());
        } else if (boat instanceof ReChestBoatEntity reChestBoat) {
            return this.boatResources.get(reChestBoat.getBoatType());
        } else {
            return null;
        }
    }
}
