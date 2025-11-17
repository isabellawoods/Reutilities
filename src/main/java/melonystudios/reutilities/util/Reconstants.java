package melonystudios.reutilities.util;

import melonystudios.reutilities.ReConfigs;
import melonystudios.reutilities.Reutilities;
import melonystudios.reutilities.api.BoatType;
import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.event.AddComponentTooltipsEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.jetbrains.annotations.ApiStatus;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

/// Contains various constants, maps and methods used across <i>Reutilities</i>.
public class Reconstants {
    public static final List<Block> SIGNS = new ArrayList<>();
    public static final List<Block> HANGING_SIGNS = new ArrayList<>();
    public static final Map<ResourceLocation, BoatType> BOATS = new HashMap<>();
    /// Represents the default `OAK` boat type, for when there are no registered boat types.
    public static final BoatType OAK = new BoatType(() -> Items.OAK_BOAT, () -> Items.OAK_CHEST_BOAT, ResourceLocation.withDefaultNamespace("oak"));
    public static final int EMISSIVE_LIGHT_VALUE = LightTexture.pack(15, 15);
    public static String CURRENT_PANORAMA_OUTPUT_FOLDER = "unset_please_report_to_reutilities";

    /// Gets a {@linkplain BoatType boat type} from a string.
    /// @param woodType A string containing the wood type to get, usually a resource location.
    public static BoatType byWoodType(String woodType) {
        return byWoodType(woodType, OAK);
    }

    /// Gets a {@linkplain BoatType boat type} from a string.
    /// @param woodType A string containing the wood type to get, usually a resource location.
    /// @param defaultType A default boat type if the requested type doesn't exist in the map.
    public static BoatType byWoodType(String woodType, BoatType defaultType) {
        return BOATS.getOrDefault(ResourceLocation.parse(woodType), defaultType);
    }

    /// Custom {@link net.minecraft.client.renderer.entity.LivingEntityRenderer#getOverlayCoords(LivingEntity, float) getOverlayCoords()} method to remove the red tint from taking damage or dying.
    /// @param u The textures U value, usually set to `0`.
    public static int getOverlayCoordinates(float u) {
        return OverlayTexture.pack(OverlayTexture.u(u), OverlayTexture.v(false));
    }

    @ApiStatus.Internal
    public static AddComponentTooltipsEvent addComponentTooltips() {
        AddComponentTooltipsEvent event = new AddComponentTooltipsEvent();
        NeoForge.EVENT_BUS.post(event);
        return event;
    }

    /// Adds all tag entries from an item stack's `components` field to a list.
    /// @param extension The item stack.
    /// @param tooltip A list of {@linkplain Component components} to add the tooltip into. This is usually empty, so when there is something it should display the tag.
    public static <S extends IItemStackExtension> List<Component> addItemTagsTooltip(S extension, HolderLookup.Provider registries, List<Component> tooltip) {
        if (!(extension instanceof ItemStack stack)) return tooltip;
        var stackTag = stack.save(registries, new CompoundTag());
        if (stackTag instanceof CompoundTag tag && tag.contains("components", Tag.TAG_COMPOUND)) {
            String indentation = ReConfigs.LINE_BREAKS_ON_COMPONENTS.get() ? " " : "";
            tooltip.add(Component.translatable("tooltip.reutilities.components", new TextComponentTagVisitor(indentation).visit(tag.getCompound("components"))).withStyle(ChatFormatting.GRAY));
        }
        return tooltip;
    }

    /// Whether a tooltip can be displayed on an item, or is hidden by the {@link melonystudios.reutilities.component.ReDataComponents#HIDE_COMPONENTS reutilities:hide_components} component.
    /// @param extension The item stack.
    /// @param name A resource location of the tooltip name, like `reutilities:item_components`.
    public static <S extends IItemStackExtension> boolean shouldDisplay(S extension, ResourceLocation name) {
        return extension instanceof ItemStack stack && ReAPI.shouldDisplay(stack, name);
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
