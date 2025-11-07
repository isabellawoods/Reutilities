package melonystudios.behaviorapi.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.behaviorapi.ItemBehavior;
import melonystudios.behaviorapi.settings.GlobalSettings;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DefaultItemBehavior extends ItemBehavior {
    public static final MapCodec<DefaultItemBehavior> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(GlobalSettings.codec().forGetter(ItemBehavior::settings)).apply(instance, DefaultItemBehavior::new));

    public DefaultItemBehavior(GlobalSettings settings) {
        super(new Default(), settings);
    }

    public DefaultItemBehavior() {
        this(GlobalSettings.defaults());
    }

    @Override
    public MapCodec<? extends ItemBehavior> settingsCodec() {
        return CODEC;
    }

    @Override
    public void runBehavior(ItemStack stack, Level world, LivingEntity livEntity) {}
}
