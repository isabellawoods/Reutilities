package melonystudios.reutilities.event.custom;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/// This event adds the tooltips of data components during {@linkplain net.minecraft.world.item.ItemStack#getTooltipLines ItemStack.getTooltipLines()},
/// when other global tooltips are added.
public class AddComponentTooltipsEvent extends Event {
    private final List<DataComponentType<? extends TooltipProvider>> components = new ArrayList<>();

    /// Adds a data component to the tooltip of the item stack.
    /// @param component The data component to add.
    public void addComponent(DataComponentType<? extends TooltipProvider> component) {
        this.components.add(component);
    }

    /// @return An *immutable* list of all components to add.
    public List<DataComponentType<? extends TooltipProvider>> getComponentToAdd() {
        return ImmutableList.copyOf(this.components);
    }
}
