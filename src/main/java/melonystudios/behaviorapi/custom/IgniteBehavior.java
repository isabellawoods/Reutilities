package melonystudios.behaviorapi.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.IndividualSettings;
import melonystudios.behaviorapi.settings.GlobalSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class IgniteBehavior extends ItemBehavior {
    public static final MapCodec<IgniteBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ignite.CODEC.forGetter(IgniteBehavior::ignite),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, IgniteBehavior::new));
    public static final int DEFAULT_TOOLTIP_COLOR = 0xE1A61E;
    public static final int SOUL_TOOLTIP_COLOR = 0x28CBC9;
    private final Ignite ignite;

    public IgniteBehavior(Ignite ignite, GlobalSettings settings) {
        super(ignite, settings);
        this.ignite = ignite;
    }

    public IgniteBehavior(Ignite ignite) {
        this(ignite, GlobalSettings.defaults());
    }

    public IgniteBehavior() {
        this(new Ignite(100));
    }

    public Ignite ignite() {
        return this.ignite;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        if (!world.isClientSide()) livEntity.igniteForTicks(this.ignite().ticksOnFire());
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flag) {
        super.addToTooltip(context, adder, flag);
        if (this.behaviorCodec() instanceof Ignite(int ticksOnFire, int tooltipColor)) {
            Component ablazeTooltip = Component.translatable("item_behavior.behaviorapi.ignite.ablaze", StringUtil.formatTickDuration(ticksOnFire, context.tickRate())).withColor(tooltipColor);
            adder.accept(Component.translatable("tooltip.reutilities.harmful_effect", ablazeTooltip).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public record Ignite(int ticksOnFire, int tooltipColor) implements IndividualSettings<Ignite> {
        public static final MapCodec<Ignite> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("ticks_on_fire", 100).forGetter(Ignite::ticksOnFire),
                ExtraCodecs.intRange(0, 16777215).optionalFieldOf("tooltip_color", DEFAULT_TOOLTIP_COLOR).forGetter(Ignite::tooltipColor)
        ).apply(instance, Ignite::new));

        public Ignite(int ticksOnFire) {
            this(ticksOnFire, DEFAULT_TOOLTIP_COLOR);
        }

        @Override
        public MapCodec<Ignite> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Ignite> streamCodec() {
            return StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, Ignite::ticksOnFire,
                    ByteBufCodecs.VAR_INT, Ignite::tooltipColor,
                    Ignite::new
            );
        }
    }
}
