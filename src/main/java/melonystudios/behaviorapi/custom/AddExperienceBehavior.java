package melonystudios.behaviorapi.custom;

import com.mojang.serialization.Codec;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class AddExperienceBehavior extends ItemBehavior {
    public static final MapCodec<AddExperienceBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AddExperience.CODEC.forGetter(AddExperienceBehavior::addExperience),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, AddExperienceBehavior::new));
    private final AddExperience addExperience;

    public AddExperienceBehavior(AddExperience addExperience, GlobalSettings settings) {
        super(addExperience, settings);
        this.addExperience = addExperience;
    }

    public AddExperienceBehavior(AddExperience addExperience) {
        this(addExperience, GlobalSettings.defaults());
    }

    public AddExperienceBehavior() {
        this(new AddExperience(0, false));
    }

    public AddExperience addExperience() {
        return this.addExperience;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        if (!world.isClientSide() && livEntity instanceof Player player) {
            if (this.addExperience().levels()) {
                player.giveExperienceLevels(this.addExperience().amount());
            } else {
                player.giveExperiencePoints(this.addExperience().amount());
            }
        }
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flag) {
        super.addToTooltip(context, adder, flag);
        adder.accept(Component.translatable("item_behavior.behaviorapi.add_experience.experience",
                Component.translatable("item_behavior.behaviorapi.add_experience." + (this.addExperience().levels() ? "levels" : "points"), this.addExperience().amount()).withColor(0x80FF20)
        ).withStyle(ChatFormatting.GRAY));
    }

    public record AddExperience(int amount, boolean levels) implements IndividualSettings<AddExperience> {
        public static final MapCodec<AddExperience> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("amount", 0).forGetter(AddExperience::amount),
                Codec.BOOL.optionalFieldOf("levels", false).forGetter(AddExperience::levels)
        ).apply(instance, AddExperience::new));

        @Override
        public MapCodec<AddExperience> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AddExperience> streamCodec() {
            return StreamCodec.composite(
                    ByteBufCodecs.INT, AddExperience::amount,
                    ByteBufCodecs.BOOL, AddExperience::levels,
                    AddExperience::new
            );
        }
    }
}
