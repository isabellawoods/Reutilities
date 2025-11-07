package melonystudios.behaviorapi;

import melonystudios.behaviorapi.custom.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemBehaviors {
    public static final DeferredRegister<ItemBehavior> BEHAVIORS = DeferredRegister.create(BehaviorAPI.ITEM_BEHAVIOR_KEY, BehaviorAPI.MOD_ID);

    public static final DeferredHolder<ItemBehavior, ItemBehavior> DEFAULT = BEHAVIORS.register("default", () -> new DefaultItemBehavior());
    public static final DeferredHolder<ItemBehavior, AddExperienceBehavior> ADD_EXPERIENCE = BEHAVIORS.register("add_experience", () -> new AddExperienceBehavior());
    public static final DeferredHolder<ItemBehavior, ApplyEffectsBehavior> APPLY_EFFECTS = BEHAVIORS.register("apply_effects", () -> new ApplyEffectsBehavior());
    public static final DeferredHolder<ItemBehavior, ClearEffectsBehavior> CLEAR_EFFECTS = BEHAVIORS.register("clear_effects", () -> new ClearEffectsBehavior());
    public static final DeferredHolder<ItemBehavior, IgniteBehavior> IGNITE = BEHAVIORS.register("ignite", () -> new IgniteBehavior());
    public static final DeferredHolder<ItemBehavior, DamageEntityBehavior> DAMAGE_ENTITY = BEHAVIORS.register("damage_entity", () -> new DamageEntityBehavior());
    public static final DeferredHolder<ItemBehavior, EatItemBehavior> EAT_ITEM = BEHAVIORS.register("eat_item", () -> new EatItemBehavior());
    public static final DeferredHolder<ItemBehavior, ExplodeBehavior> EXPLODE = BEHAVIORS.register("explode", () -> new ExplodeBehavior());
    public static final DeferredHolder<ItemBehavior, PlaySoundBehavior> PLAY_SOUND = BEHAVIORS.register("play_sound", () -> new PlaySoundBehavior());
    public static final DeferredHolder<ItemBehavior, RemoveEffectsBehavior> REMOVE_EFFECTS = BEHAVIORS.register("remove_effects", () -> new RemoveEffectsBehavior());
    public static final DeferredHolder<ItemBehavior, TeleportEntityBehavior> TELEPORT_ENTITY = BEHAVIORS.register("teleport_entity", () -> new TeleportEntityBehavior());
}
