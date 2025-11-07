package melonystudios.behaviorapi.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.IndividualSettings;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.reutilities.api.ReAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class DamageEntityBehavior extends ItemBehavior {
    public static final MapCodec<DamageEntityBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DamageEntity.CODEC.forGetter(DamageEntityBehavior::damageEntity),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, DamageEntityBehavior::new));
    private final DamageEntity damageEntity;

    public DamageEntityBehavior(DamageEntity damageEntity, GlobalSettings settings) {
        super(damageEntity, settings);
        this.damageEntity = damageEntity;
    }

    public DamageEntityBehavior(DamageEntity damageEntity) {
        this(damageEntity, GlobalSettings.defaults());
    }

    public DamageEntityBehavior() {
        this(new DamageEntity(DamageTypes.GENERIC, 0));
    }

    public DamageEntity damageEntity() {
        return this.damageEntity;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        if (world.isClientSide()) return;
        DamageSource source = world.damageSources().source(this.damageEntity.type(), livEntity);
        if (!livEntity.isInvulnerableTo(source)) livEntity.hurt(source, this.damageEntity.amount());
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flag) {
        super.addToTooltip(context, adder, flag);
        Level world = context.level();
        if (world == null) return;

        DamageSource source = world.damageSources().source(this.damageEntity.type());
        String translationKey = this.damageEntity.type().location().toLanguageKey("damage_type");
        Component translation;

        if (I18n.exists(translationKey)) translation = Component.translatable(translationKey);
        else translation = Component.literal(source.getMsgId());

        adder.accept(Component.translatable("item_behavior.behaviorapi.damage_entity.damage", this.damageEntity.amount(), translation).withStyle(ChatFormatting.GRAY));
    }

    public record DamageEntity(ResourceKey<DamageType> type, float amount) implements IndividualSettings<DamageEntity> {
        public static final MapCodec<DamageEntity> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("type").forGetter(DamageEntity::type),
                ReAPI.floatRange(0, Float.MAX_VALUE).optionalFieldOf("amount", 0F).forGetter(DamageEntity::amount)
        ).apply(instance, DamageEntity::new));

        @Override
        public MapCodec<DamageEntity> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DamageEntity> streamCodec() {
            return StreamCodec.composite(
                    ResourceKey.streamCodec(Registries.DAMAGE_TYPE), DamageEntity::type,
                    ByteBufCodecs.FLOAT, DamageEntity::amount,
                    DamageEntity::new
            );
        }
    }
}
