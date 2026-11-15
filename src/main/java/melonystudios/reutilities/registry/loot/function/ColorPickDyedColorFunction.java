package melonystudios.reutilities.registry.loot.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.api.picker.ColorPicker;
import melonystudios.reutilities.registry.loot.ReLootFunctions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class ColorPickDyedColorFunction extends LootItemConditionalFunction {
    public static final MapCodec<ColorPickDyedColorFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).and(instance.group(
            ColorPicker.CODEC.fieldOf("color_picker").forGetter(function -> function.colorPicker),
            Codec.BOOL.optionalFieldOf("use_picker_random_sequence", true).forGetter(function -> function.usePickerRandomSequence),
            Codec.BOOL.optionalFieldOf("show_dyed_tooltip", true).forGetter(function -> function.showDyedTooltip)
    )).apply(instance, ColorPickDyedColorFunction::new));
    private final Holder<ColorPicker> colorPicker;
    private final boolean usePickerRandomSequence;
    private final boolean showDyedTooltip;

    public ColorPickDyedColorFunction(List<LootItemCondition> conditions, Holder<ColorPicker> colorPicker, boolean usePickerRandomSequence, boolean showDyedTooltip) {
        super(conditions);
        this.colorPicker = colorPicker;
        this.usePickerRandomSequence = usePickerRandomSequence;
        this.showDyedTooltip = showDyedTooltip;
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        int sampledColor;
        if (this.usePickerRandomSequence) {
            sampledColor = ColorPicker.sample(this.colorPicker.value(), context.getLevel()).asDecimal();
        } else {
            sampledColor = ColorPicker.sample(this.colorPicker.value(), context.getRandom()).asDecimal();
        }

        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(sampledColor, this.showDyedTooltip));
        return stack;
    }

    public static LootItemConditionalFunction.Builder<?> pickColor(Holder<ColorPicker> colorPicker, boolean usePickerRandomSequence, boolean showDyedTooltip) {
        return simpleBuilder(conditions -> new ColorPickDyedColorFunction(conditions, colorPicker, usePickerRandomSequence, showDyedTooltip));
    }

    @Override
    public LootItemFunctionType<ColorPickDyedColorFunction> getType() {
        return ReLootFunctions.COLOR_PICK_DYED_COLOR.get();
    }
}
