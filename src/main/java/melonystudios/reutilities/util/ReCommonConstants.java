package melonystudios.reutilities.util;

import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.api.Recolor;
import melonystudios.reutilities.compat.femalegender.BreastArmorData;
import melonystudios.reutilities.event.custom.AddComponentTooltipsEvent;
import melonystudios.reutilities.option.ReCommonOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

/// Contains various constants, maps and methods used across *Reutilities*.
public class ReCommonConstants {
    public static final List<Block> SIGNS = new ArrayList<>();
    public static final List<Block> HANGING_SIGNS = new ArrayList<>();
    public static final Map<Item, BreastArmorData> BREAST_ARMOR_CAPABILITIES = new HashMap<>();
    public static final Map<ResourceLocation, BoatType> BOATS = new HashMap<>();
    public static final Map<ResourceLocation, Recolor> COLORS = new HashMap<>();
    public static final int REUTILITIES_ACCENT_COLOR = 0xFFA134;
    public static final int REVARIED_ACCENT_COLOR = 0xFFC55F;

    /// Gets a {@linkplain BoatType boat type} from a string.
    /// @param woodType A string containing the wood type to get, usually a resource location.
    public static BoatType byWoodType(String woodType) {
        return byWoodType(woodType, ReBoats.OAK);
    }

    /// Gets a {@linkplain BoatType boat type} from a string.
    /// @param woodType A string containing the wood type to get, usually a resource location.
    /// @param defaultType A default boat type if the requested type doesn't exist in the map.
    public static BoatType byWoodType(String woodType, BoatType defaultType) {
        return BOATS.getOrDefault(ResourceLocation.parse(woodType), defaultType);
    }

    @ApiStatus.Internal
    public static AddComponentTooltipsEvent addComponentTooltips() {
        AddComponentTooltipsEvent event = new AddComponentTooltipsEvent();
        NeoForge.EVENT_BUS.post(event);
        return event;
    }

    /// Adds all tag entries from an item stack's `components` field to a list.
    /// @param holder A data component holder, such as an item stack.
    /// @param tooltip A list of {@linkplain Component components} to add the tooltip into. This is usually empty, so when there is something it should display the tag.
    public static <S extends DataComponentHolder> List<Component> addItemTagsTooltip(S holder, HolderLookup.Provider registries, List<Component> tooltip) {
        if (!(holder instanceof ItemStack stack) || stack.isEmpty()) return tooltip;
        var stackTag = stack.copyWithCount(1).save(registries, new CompoundTag()); // set count to 1 to fix crash with Classic Pipes ~isa 08-01-26
        if (stackTag instanceof CompoundTag tag && tag.contains("components", Tag.TAG_COMPOUND)) {
            String indentation = ReCommonOptions.LINE_BREAKS_ON_COMPONENTS.get() ? " " : "";
            tooltip.add(Component.translatable("tooltip.reutilities.components", new TextComponentTagVisitor(indentation).visit(tag.getCompound("components"))).withStyle(ChatFormatting.GRAY));
        }
        return tooltip;
    }

    public static ResourceLocation pulling() {
        return ResourceLocation.withDefaultNamespace("pulling");
    }

    public static ResourceLocation pullProgress() {
        return ResourceLocation.withDefaultNamespace("pull");
    }

    public static ResourceLocation charged() {
        return ResourceLocation.withDefaultNamespace("charged");
    }

    public static ResourceLocation fireworkRocketLoaded() {
        return ResourceLocation.withDefaultNamespace("firework");
    }

    public static ResourceLocation blocking() {
        return ResourceLocation.withDefaultNamespace("blocking");
    }

    public static ResourceLocation trimType() {
        return ResourceLocation.withDefaultNamespace("trim_type");
    }

    public static ResourceLocation broken() {
        return ResourceLocation.withDefaultNamespace("broken");
    }

    public static ResourceLocation textureID() {
        return Reutilities.reutilities("texture_id");
    }

    public static ResourceLocation monthCheck(Month month) {
        return Reutilities.reutilities("month_check/" + month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH).toLowerCase(Locale.ENGLISH));
    }
}
