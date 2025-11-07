package melonystudios.behaviorapi;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BehaviorSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, BehaviorAPI.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PLAY_SOUND_DEFAULT = SOUNDS.register("behavior.play_sound.default", () -> SoundEvent.createVariableRangeEvent(BehaviorAPI.PLAY_SOUND_DEFAULT));
}
