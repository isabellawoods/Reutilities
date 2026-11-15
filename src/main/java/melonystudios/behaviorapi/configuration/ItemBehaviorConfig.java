package melonystudios.behaviorapi.configuration;

import com.mojang.serialization.Codec;
import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.ItemBehavior;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;

import java.util.List;

/// An **item behavior configuration** is a way to store [item behaviors][ItemBehavior] as a separate file outside the item's data components.
///
/// Item behavior configurations can be defined using JSON files in a data pack at the path `data/<namespace>/behaviorapi/item_behavior_config`,
/// and can have tags defined at the path `data/<namespace>/tags/behaviorapi/item_behavior_config`.
/// @since *Behavior API* `1.6.0-beta`
/// @author isabellawoods on [**Informational Mod Features**](https://github.com/isabellawoods/Informational-Mod-Features/blob/main/Reutilities/Docs/Item%20Behavior%20Configuration.md)
/// @param behaviors A list of item behaviors.
public record ItemBehaviorConfig(List<ItemBehavior> behaviors) {
    public static final Codec<ItemBehaviorConfig> DIRECT_CODEC = ItemBehavior.CODEC.get().codec().listOf().xmap(
            ItemBehaviorConfig::new,
            ItemBehaviorConfig::behaviors
    );
    public static final Codec<Holder<ItemBehaviorConfig>> CODEC = RegistryFileCodec.create(BehaviorAPI.ITEM_BEHAVIOR_CONFIG_KEY, DIRECT_CODEC);
}
