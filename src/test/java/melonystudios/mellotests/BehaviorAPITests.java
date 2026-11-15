package melonystudios.mellotests;

import com.mojang.serialization.JsonOps;
import melonystudios.behaviorapi.BehaviorAPI;
import melonystudios.behaviorapi.ChanceEffectInstance;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.configuration.ItemBehaviorConfig;
import melonystudios.behaviorapi.custom.*;
import melonystudios.behaviorapi.settings.GlobalSettings;
import melonystudios.reutilities.api.test.CodecParseException;
import melonystudios.reutilities.api.test.ResultsWriter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

@ExtendWith(EphemeralTestServerProvider.class)
public class BehaviorAPITests implements ResultsWriter {
    @Override
    public String testIdentifier() {
        return "behavior_api";
    }

    // todo: for whatever reason this test passes but nothing is actually logged or saved... ~isa 17-08-26
    //  (the log manager crashes when closing)
    @Test
    public void testBehaviorConfig(MinecraftServer server) {
        server.execute(() -> {
            List<ItemBehavior> behaviors = List.of(
                    new AddExperienceBehavior(new AddExperienceBehavior.AddExperience(30, true)),
                    new ApplyEffectsBehavior(new ApplyEffectsBehavior.ApplyEffects(Optional.of(new ChanceEffectInstance(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1), 0.3F)))),
                    new ClearEffectsBehavior(new ClearEffectsBehavior.ClearEffects(List.of(EffectCures.HONEY))),
                    new DefaultItemBehavior(),
                    new EatItemBehavior(new EatItemBehavior.EatItem(new ItemStack(Items.GOLDEN_APPLE))),
                    new ExplodeBehavior(new ExplodeBehavior.Explode(
                            true,
                            Optional.empty(),
                            Optional.of(5F),
                            Optional.of(HolderSet.direct(Blocks.ACACIA_BUTTON.builtInRegistryHolder())),
                            Vec3.ZERO,
                            15F,
                            true,
                            Level.ExplosionInteraction.BLOCK,
                            ParticleTypes.ELDER_GUARDIAN,
                            ParticleTypes.DRAGON_BREATH,
                            SoundEvents.MUSIC_DISC_PIGSTEP
                    )),
                    new IgniteBehavior(new IgniteBehavior.Ignite(300, IgniteBehavior.SOUL_TOOLTIP_COLOR)),
                    new PlaySoundBehavior(new PlaySoundBehavior.PlaySound(BehaviorAPI.behaviorAPI("aaaa"), SoundSource.MUSIC, null)),
                    new RunFunctionBehavior(new RunFunctionBehavior.RunFunction(ResourceLocation.fromNamespaceAndPath("isatweaks", "amys_sun_hat"))),
                    new TeleportEntityBehavior(new TeleportEntityBehavior.TeleportEntity(24.5F), GlobalSettings.of(true, GlobalSettings.ATTACKING))
            );

            ItemBehaviorConfig.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, new ItemBehaviorConfig(behaviors))
                    .resultOrPartial(error -> {
                        throw new CodecParseException(error);
                    })
                    .ifPresent(element -> this.save(element, "item_behavior_config"));
        });
    }
}
