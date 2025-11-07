package melonystudios.behaviorapi;

import com.mojang.serialization.MapCodec;
import melonystudios.behaviorapi.settings.IndividualSettings;
import melonystudios.behaviorapi.settings.GlobalSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/// An **item behavior** is something that happens when eating an item or attacking an entity with an item.
///
/// New item behaviors can be made by extending this class and registering them with the **item behavior** and **item behavior serializer** registries.
public abstract class ItemBehavior {
    public static final Supplier<MapCodec<ItemBehavior>> CODEC = () -> BehaviorAPI.ITEM_BEHAVIOR_SERIALIZER.byNameCodec().dispatchMap("id", ItemBehavior::settingsCodec, Function.identity());
    // public static final StreamCodec<RegistryFriendlyByteBuf, List<ItemBehavior>> STREAM_CODEC = ByteBufCodecs.registry(BehaviorAPI.ITEM_BEHAVIOR_KEY).dispatch((Function<? super List<ItemBehavior>, ? extends ItemBehavior>) ItemBehavior::behaviorCodec, IndividualSettings::streamCodec);
    public static final ResourceLocation DEFAULT_BEHAVIOR_ID = BehaviorAPI.behaviorAPI("default");
    public static final int DEFAULT_BEHAVIOR_COLOR = 0xFFC55F;
    private final IndividualSettings<?> behaviorCodec;
    private final GlobalSettings settings;
    @Nullable
    private String descriptionID;

    /// Creates a new `ItemBehavior`.
    /// @param behaviorCodec The behavior's **individual settings**.
    /// @param settings The behavior's **global settings**.
    public ItemBehavior(IndividualSettings<?> behaviorCodec, GlobalSettings settings) {
        this.behaviorCodec = behaviorCodec;
        this.settings = settings;
    }

    /// Creates a new `ItemBehavior`, with the **global settings** being the {@linkplain GlobalSettings#defaults defaults}.
    /// @param behaviorCodec The behavior's **individual settings**.
    public ItemBehavior(IndividualSettings<?> behaviorCodec) {
        this(behaviorCodec, GlobalSettings.defaults());
    }

    /// Creates a new `ItemBehavior`, with both the **global** and **individual settings** being set to their defaults.
    public ItemBehavior() {
        this(new Default());
    }

    public IndividualSettings<?> behaviorCodec() {
        return this.behaviorCodec;
    }

    public GlobalSettings settings() {
        return this.settings;
    }

    protected String getOrCreateDescriptionID() {
        if (this.descriptionID == null) this.descriptionID = Util.makeDescriptionId("item_behavior", BehaviorAPI.ITEM_BEHAVIOR.getKey(this));
        return this.descriptionID;
    }

    public String getDescriptionID() {
        return this.getOrCreateDescriptionID();
    }

    /// Gets the codec for this behavior's **individual settings**.
    public abstract MapCodec<? extends ItemBehavior> settingsCodec();

    /// Runs this item behavior, based on the behavior's {@linkplain GlobalSettings **individual settings**}.
    /// @param stack The item stack with the `reutilities:behaviors` component.
    /// @param world The world.
    /// @param livEntity The entity running the effect.
    /// @see melonystudios.reutilities.api.ReAPI#runItemBehavior
    /// ReAPI.runItemBehavior()
    public abstract void runBehavior(ItemStack stack, Level world, LivingEntity livEntity);

    /// Adds entries of an individual item behavior to the attached item.
    /// @param context The tooltip context, having the world's registries, tick rate and map data.
    /// @param adder A {@linkplain Consumer consumer} to add the tooltips.
    /// @param flag The tooltip flags, like whether {@linkplain net.minecraft.client.Options#advancedItemTooltips advanced tooltips}
    /// is on, or whether shift is held down.
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flag) {}

    public Component getCommandDisplayName() {
        ResourceLocation behaviorID = BehaviorAPI.ITEM_BEHAVIOR.getKey(this);
        MutableComponent component = Component.empty();
        component.append(Component.translatable(this.getDescriptionID()).withStyle(style -> style.withColor(DEFAULT_BEHAVIOR_COLOR).withBold(true)));
        component.append("\n").append(Component.translatable(this.getDescriptionID() + ".desc").withStyle(style -> style.withColor(ChatFormatting.GRAY).withBold(false)));
        component.append("\n").append(behaviorID == null ? Component.translatable("item_behavior.unregistered_sadface") : Component.literal(behaviorID.toString())).withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY).withBold(false));

        return ComponentUtils.wrapInSquareBrackets(Component.translatable(this.getDescriptionID()).withColor(DEFAULT_BEHAVIOR_COLOR))
                .withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, component)));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (this.behaviorCodec == null) {
            return false;
        } else {
            return other instanceof ItemBehavior behavior && this.behaviorCodec.equals(behavior.behaviorCodec) && this.settings == behavior.settings;
        }
    }

    @Override
    public int hashCode() {
        return 31 * this.behaviorCodec.hashCode() + this.settings.hashCode();
    }

    @Override
    public String toString() {
        return String.format("ConsumeBehavior[settings=%s, %s]", this.behaviorCodec, this.settings.toString());
    }

    public static class Default implements IndividualSettings<Default> {
        public static final Default INSTANCE = new Default();
        public static final MapCodec<Default> CODEC = MapCodec.unit(INSTANCE);

        @Override
        public MapCodec<Default> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Default> streamCodec() {
            return StreamCodec.unit(INSTANCE);
        }
    }
}
