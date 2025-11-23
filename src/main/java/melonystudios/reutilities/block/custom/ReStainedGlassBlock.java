package melonystudios.reutilities.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.api.Recolor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ReStainedGlassBlock extends TransparentBlock {
    public static final MapCodec<ReStainedGlassBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Recolor.CODEC.fieldOf("color").forGetter(ReStainedGlassBlock::getRecolor), propertiesCodec()
    ).apply(instance, ReStainedGlassBlock::new));
    private final Recolor recolor;

    public ReStainedGlassBlock(Recolor recolor, Properties properties) {
        super(properties);
        this.recolor = recolor;
    }

    public Recolor getRecolor() {
        return this.recolor;
    }

    @Override
    @Nullable
    public Integer getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
        return this.getRecolor().color();
    }
}
