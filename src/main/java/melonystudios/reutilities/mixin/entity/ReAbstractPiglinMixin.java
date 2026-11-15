package melonystudios.reutilities.mixin.entity;

import melonystudios.reutilities.entity.layer.CapeOffsets;
import melonystudios.reutilities.entity.layer.OffsetGetter;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractPiglin.class)
public abstract class ReAbstractPiglinMixin implements OffsetGetter {
    @Unique
    private CapeOffsets offsets;

    @Override
    public CapeOffsets getOffsets() {
        if (this.offsets == null) this.offsets = new CapeOffsets();
        return this.offsets;
    }
}
