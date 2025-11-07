package melonystudios.behaviorapi.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonystudios.behaviorapi.ItemBehavior;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/// Represents an **{@linkplain ItemBehavior item behavior}'s global settings**. It has the following fields:
/// <li>`show_in_tooltip`: <i>(optional)</i> Whether this behavior's tooltips are displayed. Defaults to `true`;</li>
/// <li>`environment`: <i>(optional)</i> Which environment this behavior should be run on. Can be one of `eating` or `attacking`.</li>
public class GlobalSettings {
    /// The default *"eating"* environment, ran when finishing to eat the item this behavior is attached to.
    public static final String EATING = "eating";
    /// The *"attacking"* environment, ran when attacking an entity with an item with this behavior.
    public static final String ATTACKING = "attacking";
    private boolean showInTooltip;
    private String environment;

    /// Creates a new `GlobalSettings`.
    /// @param showInTooltip Whether this behavior's tooltips are displayed. Defaults to `true`.
    /// @param environment Which environment this behavior should be run on. Can be one of `eating` or `attacking`.
    private GlobalSettings(boolean showInTooltip, String environment) {
        this.showInTooltip = showInTooltip;
        this.environment = environment;
    }

    public static MapCodec<GlobalSettings> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("show_in_tooltip", true).forGetter(GlobalSettings::showInTooltip),
                Codec.STRING.optionalFieldOf("environment", EATING).forGetter(GlobalSettings::environment)
        ).apply(instance, GlobalSettings::new));
    }

    public static StreamCodec<ByteBuf, GlobalSettings> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.BOOL, GlobalSettings::showInTooltip,
                ByteBufCodecs.STRING_UTF8, GlobalSettings::environment,
                GlobalSettings::of
        );
    }

    /// Creates a new `GlobalSettings`.
    /// @param showInTooltip Whether this behavior's tooltips are displayed. Defaults to `true`.
    /// @param environment Which environment this behavior should be run on. Can be one of `eating` or `attacking`.
    public static GlobalSettings of(boolean showInTooltip, String environment) {
        return new GlobalSettings(showInTooltip, environment);
    }

    /// Creates a new `GlobalSettings` with the default settings (with tooltip, `eating` environment).
    public static GlobalSettings defaults() {
        return new GlobalSettings(true, EATING);
    }

    public boolean showInTooltip() {
        return this.showInTooltip;
    }

    public String environment() {
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
    public GlobalSettings withEnvironment(String environment) {
        this.environment = environment;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof GlobalSettings settings && this.showInTooltip == settings.showInTooltip && this.environment.equals(settings.environment);
        }
    }

    @Override
    public int hashCode() {
        return 31 * Boolean.hashCode(this.showInTooltip) + this.environment.hashCode();
    }

    @Override
    public String toString() {
        return String.format("GlobalSettings[showInTooltip=%s, environment=%s]", this.showInTooltip, this.environment);
    }
}
