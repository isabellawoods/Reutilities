package melonystudios.behaviorapi.serializer;

import com.mojang.serialization.MapCodec;
import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.custom.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ConsumeBehaviorSerializers {
    public static final DeferredRegister<MapCodec<? extends ItemBehavior>> SERIALIZERS = DeferredRegister.create(BehaviorAPI.ITEM_BEHAVIOR_SERIALIZER, BehaviorAPI.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<DefaultItemBehavior>> DEFAULT = SERIALIZERS.register("default", () -> DefaultItemBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<AddExperienceBehavior>> ADD_EXPERIENCE = SERIALIZERS.register("add_experience", () -> AddExperienceBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<ApplyEffectsBehavior>> APPLY_EFFECTS = SERIALIZERS.register("apply_effects", () -> ApplyEffectsBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<ClearEffectsBehavior>> CLEAR_EFFECTS = SERIALIZERS.register("clear_effects", () -> ClearEffectsBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<IgniteBehavior>> IGNITE = SERIALIZERS.register("ignite", () -> IgniteBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<DamageEntityBehavior>> DAMAGE_ENTITY = SERIALIZERS.register("damage_entity", () -> DamageEntityBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<EatItemBehavior>> EAT_ITEM = SERIALIZERS.register("eat_item", () -> EatItemBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<ExplodeBehavior>> EXPLODE = SERIALIZERS.register("explode", () -> ExplodeBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<PlaySoundBehavior>> PLAY_SOUND = SERIALIZERS.register("play_sound", () -> PlaySoundBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<RemoveEffectsBehavior>> REMOVE_EFFECTS = SERIALIZERS.register("remove_effects", () -> RemoveEffectsBehavior.CODEC);
    public static final DeferredHolder<MapCodec<? extends ItemBehavior>, MapCodec<TeleportEntityBehavior>> TELEPORT_ENTITY = SERIALIZERS.register("teleport_entity", () -> TeleportEntityBehavior.CODEC);
}
