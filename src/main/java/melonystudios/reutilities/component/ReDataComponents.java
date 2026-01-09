package melonystudios.reutilities.component;

import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.api.ReCodecs;
import melonystudios.reutilities.component.custom.CapePositioning;
import melonystudios.reutilities.component.custom.ComponentOutfit;
import melonystudios.reutilities.component.custom.StoredExperience;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ReDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Reutilities.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> WOOD_TYPE = COMPONENTS.registerComponentType("wood_type",
            builder -> builder.persistent(BoatType.WOOD_TYPE_CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BAR_COLOR = COMPONENTS.registerComponentType("bar_color",
            builder -> builder.persistent(ReCodecs.hexadecimalRange(0, 16777215)).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<StoredExperience>> STORED_EXPERIENCE = COMPONENTS.registerComponentType("stored_experience",
            builder -> builder.persistent(StoredExperience.CODEC).networkSynchronized(StoredExperience.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ResourceLocation>>> HIDE_COMPONENTS = COMPONENTS.registerComponentType("hide_components",
            builder -> builder.persistent(ResourceLocation.CODEC.listOf()).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ComponentOutfit>> OUTFIT = COMPONENTS.registerComponentType("outfit",
            builder -> builder.persistent(ComponentOutfit.CODEC).networkSynchronized(ComponentOutfit.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ItemBehavior>>> BEHAVIORS = COMPONENTS.registerComponentType("behaviors",
            builder -> builder.persistent(ItemBehavior.CODEC.get().codec().listOf()).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LIGHT_EMISSION = COMPONENTS.registerComponentType("light_emission",
            builder -> builder.persistent(ExtraCodecs.intRange(0, 15)).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CapePositioning>> CAPE_POSITIONING = COMPONENTS.registerComponentType("cape_positioning",
            builder -> builder.persistent(CapePositioning.CODEC).networkSynchronized(CapePositioning.STREAM_CODEC).cacheEncoding());
}
