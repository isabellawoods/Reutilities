package melonystudios.reutilities.registry.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.registry.loot.ReLootFunctions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetRandomDyesFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetRandomDyesFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).and(
            NumberProviders.CODEC.fieldOf("number_of_dyes").forGetter(function -> function.numberOfDyes)
    ).apply(instance, SetRandomDyesFunction::new));
    private final NumberProvider numberOfDyes;

    public SetRandomDyesFunction(List<LootItemCondition> conditions, NumberProvider numberOfDyes) {
        super(conditions);
        this.numberOfDyes = numberOfDyes;
    }

    @Override
    @NotNull
    public ItemStack run(ItemStack stack, LootContext context) {
        RandomSource random = context.getRandom();
        int rolls = this.numberOfDyes.getInt(context);

        if (rolls <= 0) {
            return stack;
        } else {
            List<DyeItem> dyes = new ArrayList<>(rolls);
            for (int i = 0; i < rolls; i++) dyes.add(getRandomDye(random));

            ItemStack dyedStack = DyedItemColor.applyDyes(stack, dyes);
            return dyedStack.isEmpty() ? stack : dyedStack;
        }
    }

    /// Picks a random [DyeColor] from the existing **16** colors.
    /// @param random The random source of randomize the colors.
    private static DyeItem getRandomDye(RandomSource random) {
        return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
    }

    public static Builder<?> withCount(NumberProvider dyeAmount) {
        return simpleBuilder(conditions -> new SetRandomDyesFunction(conditions, dyeAmount));
    }

    @Override
    @NotNull
    public LootItemFunctionType<SetRandomDyesFunction> getType() {
        return ReLootFunctions.SET_RANDOM_DYES.get();
    }
}
