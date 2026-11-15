package melonystudios.reutilities.api.picker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.api.ReCodecs;
import melonystudios.reutilities.util.ReRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.util.FastColor.ARGB32.*;

/// A **color picker** is a way to randomly pick colors from a list and blend them together. This is a replacement for the vanilla [`DyedArmorForEmeralds.getOffer()`][net.minecraft.world.entity.npc.VillagerTrades.DyedArmorForEmeralds#getOffer]
/// method that one of my other mods, [*Stancements*](https://github.com/isabellawoods/Stancements), uses.
///
/// Color pickers can be defined using JSON files in a data pack at the path `data/<namespace>/reutilities/color_picker`, and can have tags defined at the path `data/<namespace>/tags/reutilities/color_picker`.
/// @author isabellawoods on [**IMF**](https://github.com/isabellawoods/Informational-Mod-Features/blob/main/Reutilities/Docs/Color%20Picker.md)
/// @param interpolationTargets A list of integers — the colors picked from [`initial_values`][#initialValues()] will be [interpolated][#lerpColor] to a color picked from this list.
/// @param pickAndMixAmount *(optional, defaults to `1`)* An [int provider][IntProvider] — how many colors should be picked to be blended and/or interpolated.
/// @param colorBlendingOrder *(optional, defaults to `before`)* Whether the color blending should happen *before* or *after* the [color interpolation][#lerpColor] process. Can be one of `before`, `after` or `random`.
/// @param initialValues A list of 1 to 128 base colors to blend and/or interpolate. Can be written in decimal or in hexadecimal (`#aarrggbb` or `#rrggbb`).
/// @param randomSequence *(optional)* An [identifier][ResourceLocation] of the random sequence used to generate the colors in this color picker. This is used to make color picker generate the same colors when using the same world seed.
public record ColorPicker(
        List<Integer> interpolationTargets,
        IntProvider pickAndMixAmount,
        ColorBlendingOrder colorBlendingOrder,
        List<Integer> initialValues,
        Optional<ResourceLocation> randomSequence
) {
    public static final Codec<ColorPicker> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ReCodecs.OPTIONAL_ARGB_CODEC.listOf(0, 128).fieldOf("interpolation_targets").forGetter(ColorPicker::interpolationTargets),
            IntProvider.codec(1, 128).optionalFieldOf("pick_and_mix_amount", ConstantInt.of(1)).forGetter(ColorPicker::pickAndMixAmount),
            ColorBlendingOrder.CODEC.optionalFieldOf("color_blending_order", ColorBlendingOrder.BEFORE).forGetter(ColorPicker::colorBlendingOrder),
            ReCodecs.OPTIONAL_ARGB_CODEC.listOf(1, 128).fieldOf("initial_values").forGetter(ColorPicker::initialValues),
            ResourceLocation.CODEC.optionalFieldOf("random_sequence").forGetter(ColorPicker::randomSequence)
    ).apply(instance, ColorPicker::new));
    public static final Codec<Holder<ColorPicker>> CODEC = RegistryFileCodec.create(ReRegistries.COLOR_PICKER, DIRECT_CODEC);

    public static final DyeColor[] DYE_COLORS = DyeColor.values();
    /// A list of all dye colors based on their appearance on maps.
    public static final Function<MapColor.Brightness, List<Integer>> DYE_COLORS_BY_MAP = brightness -> Arrays.stream(DYE_COLORS).map(dye -> dye.getMapColor().calculateRGBColor(brightness)).toList();
    /// A list of all dye colors based on their texture diffuse color. **This is the commonly used values for dyes**.
    public static final List<Integer> DYE_COLORS_BY_TEXTURE_DIFFUSE = Arrays.stream(DYE_COLORS).map(DyeColor::getTextureDiffuseColor).toList();
    /// A list of all dye colors based on their firework rocket colors.
    public static final List<Integer> DYE_COLORS_BY_FIREWORKS = Arrays.stream(DYE_COLORS).map(DyeColor::getFireworkColor).toList();
    /// A list of all dye colors based on their color on signs and hanging signs.
    public static final List<Integer> DYE_COLORS_BY_TEXT = Arrays.stream(DYE_COLORS).map(DyeColor::getTextColor).toList();

    /// Creates an instance of the **color picker builder**.
    /// @param randomSequence An [identifier][ResourceLocation] of the random sequence used to generate the colors in this color picker.
    public static Builder picker(ResourceLocation randomSequence) {
        return new Builder(randomSequence);
    }

    /// Creates an instance of the **color picker builder** with no backing random sequence.
    public static Builder picker() {
        return new Builder(null);
    }

    /// Samples the given color picker based on the random source provided by its `random_sequence` parameter, if possible.
    /// @param pickerKey The [resource key][ResourceKey] of the color picker.
    /// @param level The server world. The [random source][RandomSource] given by this method is based on the color picker's [`random_sequence`][#randomSequence()], and will be same when using the same world seed.
    /// @return A [sample][PickerSample] picked by blending and interpolating the colors provided by the picker.
    public static PickerSample sample(ResourceKey<ColorPicker> pickerKey, ServerLevel level) {
        Registry<ColorPicker> colorPickers = level.registryAccess().registryOrThrow(ReRegistries.COLOR_PICKER);
        return sample(colorPickers.get(pickerKey), level);
    }

    /// Samples the given color picker based on the random source provided by its `random_sequence` parameter, if possible.
    /// @param picker The color picker to sample.
    /// @param level The server world. The [random source][RandomSource] given by this method is based on the color picker's [`random_sequence`][#randomSequence()], and will be same when using the same world seed.
    /// @return A [sample][PickerSample] picked by blending and interpolating the colors provided by the picker.
    public static PickerSample sample(ColorPicker picker, ServerLevel level) {
        return sample(picker, picker.randomSequence().map(level::getRandomSequence).orElseGet(level::getRandom));
    }

    /// Samples the given color picker based on the provided random source.
    /// @param picker The color picker to sample.
    /// @param random The [random source][RandomSource] to use.
    /// @return A [sample][PickerSample] picked by blending and interpolating the colors provided by the picker.
    public static PickerSample sample(ColorPicker picker, RandomSource random) {
        // note: these steps are from when i first started writing this method ~isa 04-11-26
        // 'mix_after_interpolation' is the former name of 'color_blending_order'

        // STEPS
        // 1. pick 'pick_and_mix_limit' colors from 'initial_values' -> int[]::pickedInitialValues
        // 2. pick 1 color from 'interpolation_targets', if available -> int::interpolationTarget
        // 3. pick a number from 0 to 1, used by Mth.lerp -> float::delta

        // == IF 'mix_after_interpolation' IS 'BEFORE' ==
        // 4. blend all pickedInitialValues together (see DyedItemColor#applyDyes) -> int::blendedInitialColors
        // 5. Mth.lerp(delta, blendedInitialColors, interpolationTarget); (return value)

        // == IF 'mix_after_interpolation' IS 'AFTER' ==
        // 4. for (int initialValue : pickedInitialValues) Mth.lerp(delta, initialValue, interpolationTarget); -> mixedColors
        // 5. blend all mixedColors together (see DyedItemColor#applyDyes) -> int::blendedMixedColors (return value)


        // 1. pick from one to 'pick_and_mix_limit' color(s) from 'initial_values'
        List<Integer> pickedInitialValues = new ArrayList<>();
        for (int i = 0; i < picker.pickAndMixAmount().sample(random); ++i) {
            int pickedValue = picker.initialValues().get(Mth.randomBetweenInclusive(random, 0, picker.initialValues().size() - 1));
            if (!pickedInitialValues.contains(pickedValue)) pickedInitialValues.add(pickedValue);
        }

        // 2. pick one color from 'interpolation_targets', if available
        Integer interpolationTarget = null;
        if (!picker.interpolationTargets().isEmpty()) {
            interpolationTarget = picker.interpolationTargets().get(Mth.randomBetweenInclusive(random, 0, picker.interpolationTargets().size() - 1));
        }

        // 3. pick a number from 0 to 1, used by Mth.lerp
        float delta = random.nextFloat();
        boolean shouldBlendBefore = picker.colorBlendingOrder().blendsBefore(random);

        if (shouldBlendBefore) {
            // 4. and 5. blend all pickedInitialValues together and interpolate the blendedInitialColors
            int blendedInitialColors = blendColors(pickedInitialValues);
            return new PickerSample(interpolationTarget != null ? lerpColor(delta, blendedInitialColors, interpolationTarget) : blendedInitialColors);
        } else {
            // 4. interpolate all pickedInitialValues first
            List<Integer> mixedColors = new ArrayList<>();
            for (Integer initialValue : pickedInitialValues) {
                mixedColors.add(interpolationTarget != null ? lerpColor(delta, initialValue, interpolationTarget) : initialValue);
            }

            // 5. blend all mixedColors together
            return new PickerSample(blendColors(mixedColors));
        }
    }

    /// Blends a list of several colors into one. The blending formula was copied from [`DyedItemColor.applyDyes()`][net.minecraft.world.item.component.DyedItemColor#applyDyes],
    /// and is [described in the Minecraft Wiki](https://minecraft.wiki/w/Dye#Dye_mixing_formula).
    /// @param colors A list of integer ARGB colors to blend.
    /// @return A blend of all input colors, or the base color if the list only contains one color.
    public static int blendColors(List<Integer> colors) {
        if (colors.size() == 1) return colors.getFirst();

        int alphaTotal = 0;
        int redTotal = 0;
        int greenTotal = 0;
        int blueTotal = 0;
        int intensityTotal = 0;
        int colorCount = 0;

        for (int color : colors) {
            int alpha = alpha(color);
            int red = red(color);
            int green = green(color);
            int blue = blue(color);
            intensityTotal = Math.max(alpha, Math.max(red, Math.max(green, blue)));
            alphaTotal += alpha(color);
            redTotal += red;
            greenTotal += green;
            blueTotal += blue;
            colorCount++;
        }

        int alpha = alphaTotal / colorCount;
        int red = redTotal / colorCount;
        int green = greenTotal / colorCount;
        int blue = blueTotal / colorCount;
        float averageIntensity = (float) intensityTotal / colorCount;
        float resultIntensity = Math.max(alpha, Math.max(red, Math.max(green, blue)));
        alpha = (int) (alpha * averageIntensity / resultIntensity);
        red = (int) (red * averageIntensity / resultIntensity);
        green = (int) (green * averageIntensity / resultIntensity);
        blue = (int) (blue * averageIntensity / resultIntensity);
        return color(alpha, red, green, blue);
    }

    /// A variant of [Mth#lerpInt] used to lerp (**l**inearly int**erp**olate) between two colors in the ARGB format.
    /// @param delta A value usually between `0` and `1` that indicates the percentage of the lerp. `0` will give the starting value and `1` will give the end value.
    /// @param baseColor The base color. Acts as the starting value for the lerp.
    /// @param targetColor The target color. Acts as the end value for the lerp.
    public static int lerpColor(float delta, int baseColor, int targetColor) {
        int alpha = Mth.lerpInt(delta, alpha(baseColor), alpha(targetColor));
        int red = Mth.lerpInt(delta, red(baseColor), red(targetColor));
        int green = Mth.lerpInt(delta, green(baseColor), green(targetColor));
        int blue = Mth.lerpInt(delta, blue(baseColor), blue(targetColor));
        return color(alpha, red, green, blue);
    }

    public static class Builder {
        private final List<Integer> interpolationTargets = new ArrayList<>();
        private IntProvider pickAndMixAmount = ConstantInt.of(1);
        private ColorBlendingOrder colorBlendingOrder = ColorBlendingOrder.BEFORE;
        private final List<Integer> initialValues = new ArrayList<>();
        @Nullable
        private final ResourceLocation randomSequence;

        /// Creates a new instance of the **color picker builder**.
        /// @param randomSequence *(optional)* An [identifier][ResourceLocation] of the random sequence used to generate the colors in this color picker.
        public Builder(@Nullable ResourceLocation randomSequence) {
            this.randomSequence = randomSequence;
        }

        /// Adds an interpolation target to this color picker.
        /// @param targetColor An integer — the new color to target.
        public Builder withTarget(int targetColor) {
            this.interpolationTargets.add(targetColor);
            return this;
        }

        /// Adds a list interpolation targets to this color picker.
        /// @param targetColors A list of integers — the new colors to target.
        public Builder withTargets(List<Integer> targetColors) {
            this.interpolationTargets.addAll(targetColors);
            return this;
        }

        /// Sets the pick-and-mix amount of this color picker. The default is `1`.
        /// @param amount An [int provider][IntProvider] defining the new limit. The final result must be between `1` and `128`.
        public Builder pickAndMix(IntProvider amount) {
            this.pickAndMixAmount = amount;
            return this;
        }

        /// Sets the color blending order of this color picker. The default is `before`.
        /// @param order The new [color blending order][ColorBlendingOrder].
        public Builder blendingOrder(ColorBlendingOrder order) {
            this.colorBlendingOrder = order;
            return this;
        }

        /// Adds a base color to this color picker.
        /// @param baseColor An integer — the color to add.
        public Builder withInitial(int baseColor) {
            this.initialValues.add(baseColor);
            return this;
        }

        /// Adds a list of base colors to this color picker.
        /// @param baseColors A list of integers — the colors to add.
        public Builder withInitials(List<Integer> baseColors) {
            this.initialValues.addAll(baseColors);
            return this;
        }

        /// Builds this builder into a color picker definition.
        /// @return A new [color picker][ColorPicker].
        public ColorPicker build() {
            return new ColorPicker(List.copyOf(this.interpolationTargets), this.pickAndMixAmount, this.colorBlendingOrder, List.copyOf(this.initialValues), Optional.ofNullable(this.randomSequence));
        }
    }
}
