package melonystudios.behaviorapi;

import melonystudios.behaviorapi.configuration.ItemBehaviorConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BehaviorDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BehaviorAPI.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<ItemBehaviorConfig>>> BEHAVIORS = COMPONENTS.registerComponentType("behaviors",
            builder -> builder.persistent(ItemBehaviorConfig.CODEC).cacheEncoding());
}
