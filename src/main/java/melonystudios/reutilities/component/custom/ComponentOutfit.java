package melonystudios.reutilities.component.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.entity.outfit.OutfitDefinition;
import melonystudios.reutilities.util.ReRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

/// Represents an outfit definition as a data component. This class' {@link #CODEC} has the following fields:
/// <li>`definition`: A resource key of an outfit definition;</li>
/// <li>`tooltip`: <i>(optional)</i> The {@linkplain TooltipStyle tooltip style} to use for this component outfit.</li>
/// @param definition An {@linkplain EitherHolder either holder} for the definition.
/// @param style The tooltip style to use for the item's tooltip. Defaults to {@link TooltipStyle#OUTFIT OUTFIT}.
public record ComponentOutfit(EitherHolder<OutfitDefinition> definition, TooltipStyle style) implements TooltipProvider {
    public static final Codec<ComponentOutfit> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EitherHolder.codec(ReRegistries.OUTFIT_DEFINITION, OutfitDefinition.CODEC).fieldOf("definition").forGetter(ComponentOutfit::definition),
            TooltipStyle.CODEC.optionalFieldOf("tooltip", TooltipStyle.OUTFIT).forGetter(ComponentOutfit::style)
    ).apply(instance, ComponentOutfit::new));
    public static final Codec<ComponentOutfit> CODEC = Codec.withAlternative(FULL_CODEC, EitherHolder.codec(ReRegistries.OUTFIT_DEFINITION, OutfitDefinition.CODEC), holder -> new ComponentOutfit(holder, TooltipStyle.OUTFIT));
    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentOutfit> STREAM_CODEC = StreamCodec.composite(
            EitherHolder.streamCodec(ReRegistries.OUTFIT_DEFINITION, OutfitDefinition.STREAM_CODEC),
            ComponentOutfit::definition,
            TooltipStyle.STREAM_CODEC,
            ComponentOutfit::style,
            ComponentOutfit::new
    );

    /// Creates a new `ComponentOutfit`.
    /// @param definition A resource key of an outfit definition.
    /// @param style The tooltip style to use for the item's tooltip.
    public static ComponentOutfit of(ResourceKey<OutfitDefinition> definition, TooltipStyle style) {
        return new ComponentOutfit(new EitherHolder<>(definition), style);
    }

    /// Creates a new `ComponentOutfit`.
    /// @param definition A resource key of an outfit definition.
    public static ComponentOutfit of(ResourceKey<OutfitDefinition> definition) {
        return of(definition, TooltipStyle.OUTFIT);
    }

    /// Recreates a `ComponentOutfit` with a specified tooltip style.
    /// @param style The tooltip style to use.
    public ComponentOutfit withTooltip(TooltipStyle style) {
        return new ComponentOutfit(this.definition(), style);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flag) {
        HolderLookup.Provider registries = context.registries();
        if (registries == null) return;

        this.definition().unwrap(registries).ifPresent(definition -> {
            Component translation = Component.translatable(definition.getKey().location().toLanguageKey("outfit_definition").replace('/', '.')).withStyle(ChatFormatting.GRAY);
            this.style().getTooltip().accept(translation, adder);
        });
    }
}
