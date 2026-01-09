package melonystudios.reutilities.component.custom;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.entity.outfit.FullBodyOutfit;
import melonystudios.reutilities.util.tag.ReItemTags;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public enum CapePositioning implements StringRepresentable {
    /// Hides the cape from the entity's model.
    HIDDEN(0, "hidden"),
    /// Renders the cape attached to the entity's body/chest.
    ON_BODY(1, "on_body"),
    /// Renders the cape attached to the entity's chestplate.
    ON_ARMOR(2, "on_armor");

    public static final IntFunction<CapePositioning> BY_ID = ByIdMap.continuous(CapePositioning::getID, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<CapePositioning> CODEC = StringRepresentable.fromValues(CapePositioning::values);
    public static final StreamCodec<ByteBuf, CapePositioning> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CapePositioning::getID);
    private final int id;
    private final String name;

    CapePositioning(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    @Override
    @NotNull
    public String toString() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    /// @return Whether this positioning should hide the cape.
    public boolean isCapeHidden() {
        return this == HIDDEN;
    }

    /// @return Whether this positioning should offset the cape to account for a chestplate.
    public boolean shouldOffset() {
        return this == ON_BODY;
    }

    /// Gets the positioning for a cape based on the item's chest item and worn outfits.
    /// @param stack The item stack. Checks if it's elytra, or if it has the `reutilities:cape_positioning` component,
    /// @param outfit The {@linkplain FullBodyOutfit full-body outfit} attachment from the entity, if available.
    public static CapePositioning positionCapeBasedOnOutfit(ItemStack stack, @Nullable FullBodyOutfit outfit) {
        // if the item has the positioning component, always use that
        if (stack.has(ReDataComponents.CAPE_POSITIONING)) return stack.get(ReDataComponents.CAPE_POSITIONING);

        // if wearing an outfit (either component or full-body), and not wearing a non-outfit armor item
        if (stack.has(ReDataComponents.OUTFIT) || (!(stack.getItem() instanceof ArmorItem) && outfit != null)) {
            return ON_BODY;
        }

        // hide if wearing elytra (any item that extends the base class)
        if (stack.getItem() instanceof ElytraItem || stack.is(ReItemTags.HIDES_CAPE_WHEN_WORN)) return HIDDEN;

        // default to offset since we're wearing a chestplate
        return ON_ARMOR;
    }

    /// Whether the player's default cape should render based on their chest item.
    /// @param stack The chest item stack.
    public static boolean shouldCapeRender(ItemStack stack) {
        // if the item has the positioning component, always use that
        if (stack.has(ReDataComponents.CAPE_POSITIONING)) return !stack.get(ReDataComponents.CAPE_POSITIONING).isCapeHidden();

        // hide if wearing elytra (any item that extends the base class)
        return !(stack.getItem() instanceof ElytraItem) && !stack.is(ReItemTags.HIDES_CAPE_WHEN_WORN);
    }
}
