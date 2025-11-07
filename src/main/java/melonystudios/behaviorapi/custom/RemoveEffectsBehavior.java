package melonystudios.behaviorapi.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.behaviorapi.settings.IndividualSettings;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class RemoveEffectsBehavior extends ItemBehavior {
    public static final MapCodec<RemoveEffectsBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RemoveEffects.CODEC.forGetter(RemoveEffectsBehavior::removeEffects),
            GlobalSettings.codec().forGetter(ItemBehavior::settings)
    ).apply(instance, RemoveEffectsBehavior::new));
    private final RemoveEffects removeEffects;

    public RemoveEffectsBehavior(RemoveEffects removeEffects, GlobalSettings settings) {
        super(removeEffects, settings);
        this.removeEffects = removeEffects;
    }

    public RemoveEffectsBehavior(RemoveEffects removeEffects) {
        this(removeEffects, GlobalSettings.defaults());
    }

    public RemoveEffectsBehavior() {
        this(new RemoveEffects(List.of()));
    }

    public RemoveEffects removeEffects() {
        return this.removeEffects;
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {
        if (world.isClientSide()) return;
        world.registryAccess().registry(Registries.MOB_EFFECT).ifPresent(registry -> {
            for (ResourceKey<MobEffect> effect : this.removeEffects.effects()) {
                registry.getHolder(effect).ifPresent(livEntity::removeEffect);
            }
        });
    }

    public record RemoveEffects(List<ResourceKey<MobEffect>> effects) implements IndividualSettings<RemoveEffects> {
        public static final MapCodec<RemoveEffects> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceKey.codec(Registries.MOB_EFFECT).listOf().fieldOf("effects").forGetter(RemoveEffects::effects)
        ).apply(instance, RemoveEffects::new));

        @Override
        public MapCodec<RemoveEffects> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RemoveEffects> streamCodec() {
            return StreamCodec.composite(ResourceKey.streamCodec(Registries.MOB_EFFECT).apply(ByteBufCodecs.list()), RemoveEffects::effects, RemoveEffects::new);
        }
    }
}
