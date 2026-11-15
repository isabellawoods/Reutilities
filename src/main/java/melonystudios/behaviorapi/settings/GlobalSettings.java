package melonystudios.behaviorapi.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.ItemBehavior;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/// Represents an **[item behavior][ItemBehavior]'s global settings**. It has the following fields:
/// - `show_in_tooltip`: *(optional)* Whether this behavior's tooltips are displayed. Defaults to `true`;
/// - `environment`: *(optional)* Which environment this behavior should be run on. Can be one of `behaviorapi:eating` or `behaviorapi:attacking`.
public class GlobalSettings {
    /// The default *"eating"* environment, ran when finishing to eat the item this behavior is attached to.
    public static final ResourceLocation EATING = BehaviorAPI.behaviorAPI("eating");
    /// The *"attacking"* environment, ran when attacking an entity with an item with this behavior.
    public static final ResourceLocation ATTACKING = BehaviorAPI.behaviorAPI("attacking");
    private boolean showInTooltip;
    private ResourceLocation environment;

    /// Creates a new `GlobalSettings`.
    /// @param showInTooltip Whether this behavior's tooltips are displayed. Defaults to `true`.
    /// @param environment Which environment this behavior should be run on. Can be one of `behaviorapi:eating` or `behaviorapi:attacking`.
    private GlobalSettings(boolean showInTooltip, ResourceLocation environment) {
        this.showInTooltip = showInTooltip;
        this.environment = environment;
    }

    public static MapCodec<GlobalSettings> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("show_in_tooltip", true).forGetter(GlobalSettings::showInTooltip),
                ResourceLocation.CODEC.optionalFieldOf("environment", EATING).forGetter(GlobalSettings::environment)
        ).apply(instance, GlobalSettings::of));
    }

    public static StreamCodec<ByteBuf, GlobalSettings> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.BOOL, GlobalSettings::showInTooltip,
                ResourceLocation.STREAM_CODEC, GlobalSettings::environment,
                GlobalSettings::of
        );
    }

    /// Creates a new `GlobalSettings`.
    /// @param showInTooltip Whether this behavior's tooltips are displayed. Defaults to `true`.
    /// @param environment Which environment this behavior should be run on. Can be one of `behaviorapi:eating` or `behaviorapi:attacking`.
    public static GlobalSettings of(boolean showInTooltip, ResourceLocation environment) {
        return new GlobalSettings(showInTooltip, environment);
    }

    /// Creates a new `GlobalSettings` with the default settings (with tooltip, `behaviorapi:eating` environment).
    public static GlobalSettings defaults() {
        return new GlobalSettings(true, EATING);
    }

    public boolean showInTooltip() {
        return this.showInTooltip;
    }

    public ResourceLocation environment() {
        return this.environment;
    }

    /// Sets the tooltip display of the global settings.
    /// @param tooltip What to set the display to.
    public GlobalSettings withTooltip(boolean tooltip) {
        this.showInTooltip = tooltip;
        return this;
    }

    /// Sets the environment of the global settings.
    /// @param environment The environment.
    public GlobalSettings withEnvironment(ResourceLocation environment) {
        this.environment = environment;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof GlobalSettings settings && this.showInTooltip() == settings.showInTooltip() && this.environment().equals(settings.environment());
        }
    }

    @Override
    public int hashCode() {
        return 31 * Boolean.hashCode(this.showInTooltip()) + this.environment().hashCode();
    }

    @Override
    public String toString() {
        return String.format("GlobalSettings[showInTooltip=%s, environment=%s]", this.showInTooltip(), this.environment());
    }
}
