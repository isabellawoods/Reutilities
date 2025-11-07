package melonystudios.behaviorapi.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.behaviorapi.settings.IndividualSettings;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EatItemBehavior extends ItemBehavior {
    public static final MapCodec<EatItemBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EatItem.CODEC.forGetter(EatItemBehavior::eatItem),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, EatItemBehavior::new));
    private final EatItem eatItem;

    public EatItemBehavior(EatItem eatItem, GlobalSettings settings) {
        super(eatItem, settings);
        this.eatItem = eatItem;
    }

    public EatItemBehavior(EatItem eatItem) {
        this(eatItem, GlobalSettings.defaults());
    }

    public EatItemBehavior() {
        this(new EatItem(ItemStack.EMPTY));
    }

    public EatItem eatItem() {
        return this.eatItem;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        livEntity.eat(world, this.eatItem.item());
        this.eatItem.item().finishUsingItem(world, livEntity);
    }

    public record EatItem(ItemStack item) implements IndividualSettings<EatItem> {
        public static final MapCodec<EatItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("item").forGetter(EatItem::item)
        ).apply(instance, EatItem::new));

        @Override
        public MapCodec<EatItem> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EatItem> streamCodec() {
            return StreamCodec.composite(ItemStack.STREAM_CODEC, EatItem::item, EatItem::new);
        }
    }
}
