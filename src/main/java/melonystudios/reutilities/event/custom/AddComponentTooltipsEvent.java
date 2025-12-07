package melonystudios.reutilities.event.custom;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.bus.api.Event;

import java.util.*;
import java.util.stream.Collectors;

/// This event adds the tooltips of data components during {@linkplain net.minecraft.world.item.ItemStack#getTooltipLines ItemStack.getTooltipLines()},
/// when other global tooltips are added.
public class AddComponentTooltipsEvent extends Event {
    private final Map<Double, DataComponentType<? extends TooltipProvider>> components = new HashMap<>();

    /// Adds a data component to the tooltip of the item stack.
    /// @param priority The priority of this component. Lower priorities appear first in the item's tooltip.
    /// @param component The data component to add.
    public void addComponent(double priority, DataComponentType<? extends TooltipProvider> component) {
        this.components.put(priority, component);
    }

    /// @return An *immutable* list of all components to add, sorted according to their priorities.
    public List<DataComponentType<? extends TooltipProvider>> getComponentToAdd() {
        return ImmutableList.copyOf(this.components.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList())
        );
    }
}
