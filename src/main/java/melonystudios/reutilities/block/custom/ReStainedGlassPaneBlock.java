package melonystudios.reutilities.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonystudios.reutilities.api.Recolor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ReStainedGlassPaneBlock extends IronBarsBlock {
    public static final MapCodec<ReStainedGlassPaneBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Recolor.CODEC.fieldOf("color").forGetter(ReStainedGlassPaneBlock::getRecolor), propertiesCodec()
    ).apply(instance, ReStainedGlassPaneBlock::new));
    private final Recolor recolor;

    public ReStainedGlassPaneBlock(Recolor recolor, Properties properties) {
        super(properties);
        this.recolor = recolor;
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
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
