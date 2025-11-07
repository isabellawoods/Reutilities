package melonystudios.behaviorapi.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.settings.IndividualSettings;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.EffectCures;

import java.util.List;

public class ClearEffectsBehavior extends ItemBehavior {
    public static final MapCodec<ClearEffectsBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ClearEffects.CODEC.forGetter(ClearEffectsBehavior::clearEffects),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, ClearEffectsBehavior::new));
    private final ClearEffects clearEffects;

    public ClearEffectsBehavior(ClearEffects clearEffects, GlobalSettings settings) {
        super(clearEffects, settings);
        this.clearEffects = clearEffects;
    }

    public ClearEffectsBehavior(ClearEffects clearEffects) {
        this(clearEffects, GlobalSettings.defaults());
    }

    public ClearEffectsBehavior() {
        this(new ClearEffects(List.of(EffectCures.MILK)));
    }

    public ClearEffects clearEffects() {
        return this.clearEffects;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        if (!world.isClientSide()) {
            for (EffectCure cure : this.clearEffects.cures()) livEntity.removeEffectsCuredBy(cure);
        }
    }

    public record ClearEffects(List<EffectCure> cures) implements IndividualSettings<ClearEffects> {
        public static final MapCodec<ClearEffects> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                EffectCure.CODEC.listOf().optionalFieldOf("cures", List.of(EffectCures.MILK)).forGetter(ClearEffects::cures)
        ).apply(instance, ClearEffects::new));

        @Override
        public MapCodec<ClearEffects> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ClearEffects> streamCodec() {
            return StreamCodec.composite(EffectCure.STREAM_CODEC.apply(ByteBufCodecs.list()), ClearEffects::cures, ClearEffects::new);
        }
    }
}
