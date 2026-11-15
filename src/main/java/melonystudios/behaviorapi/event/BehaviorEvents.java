package melonystudios.behaviorapi.event;

import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.command.ItemBehaviorCommand;
import melonystudios.behaviorapi.configuration.ItemBehaviorConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = BehaviorAPI.MOD_ID)
public class BehaviorEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ItemBehaviorCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void addBuiltInRegistries(NewRegistryEvent event) {
        event.register(BehaviorAPI.ITEM_BEHAVIOR);
        event.register(BehaviorAPI.ITEM_BEHAVIOR_SERIALIZER);
    }

    @SubscribeEvent
    public static void addDataDrivenRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(BehaviorAPI.ITEM_BEHAVIOR_CONFIG_KEY, ItemBehaviorConfig.DIRECT_CODEC, ItemBehaviorConfig.DIRECT_CODEC);
    }
}
