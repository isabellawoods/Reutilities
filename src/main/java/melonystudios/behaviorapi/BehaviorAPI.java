package melonystudios.behaviorapi;

import com.mojang.serialization.MapCodec;
import melonystudios.behaviorapi.event.BehaviorTeleportEvent;
import melonystudios.behaviorapi.serializer.ConsumeBehaviorSerializers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.ApiStatus;

@Mod(BehaviorAPI.MOD_ID)
public class BehaviorAPI {
    public static final ResourceKey<Registry<ItemBehavior>> ITEM_BEHAVIOR_KEY = ResourceKey.createRegistryKey(behaviorAPI("item_behavior"));
    public static final ResourceKey<Registry<MapCodec<? extends ItemBehavior>>> ITEM_BEHAVIOR_SERIALIZER_KEY = ResourceKey.createRegistryKey(behaviorAPI("item_behavior_serializer"));
    public static final Registry<ItemBehavior> ITEM_BEHAVIOR = new RegistryBuilder<>(ITEM_BEHAVIOR_KEY).defaultKey(ItemBehavior.DEFAULT_BEHAVIOR_ID).sync(true).create();
    public static final Registry<MapCodec<? extends ItemBehavior>> ITEM_BEHAVIOR_SERIALIZER = new RegistryBuilder<>(ITEM_BEHAVIOR_SERIALIZER_KEY).sync(true).create();
    public static final ResourceLocation PLAY_SOUND_DEFAULT = behaviorAPI("behavior.play_sound.default");
    public static final String MOD_ID = "behaviorapi";

    public BehaviorAPI(IEventBus eventBus, ModContainer container) {
        BehaviorSounds.SOUNDS.register(eventBus);
        ItemBehaviors.BEHAVIORS.register(eventBus);
        ConsumeBehaviorSerializers.SERIALIZERS.register(eventBus);
    }

    /// Creates a new resource location under ***Behavior API's*** namespace.
    /// @param name The path of this resource location.
    public static ResourceLocation behaviorAPI(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    @ApiStatus.Internal
    public static BehaviorTeleportEvent exactTeleportThroughBehavior(ItemStack stack, Level world, LivingEntity livEntity, Vec3 teleportPos) {
        BehaviorTeleportEvent event = new BehaviorTeleportEvent(stack, world, livEntity, teleportPos.x, teleportPos.y, teleportPos.z);
        NeoForge.EVENT_BUS.post(event);
        return event;
    }

    @ApiStatus.Internal
    public static BehaviorTeleportEvent randomTeleportThroughBehavior(ItemStack stack, Level world, LivingEntity livEntity, double teleportX, double teleportY, double teleportZ, float teleportDiameter) {
        BehaviorTeleportEvent.RandomTeleport event = new BehaviorTeleportEvent.RandomTeleport(stack, world, livEntity, teleportX, teleportY, teleportZ, teleportDiameter);
        NeoForge.EVENT_BUS.post(event);
        return event;
    }
}
