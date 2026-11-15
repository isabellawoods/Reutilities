package melonystudios.reutilities.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class TwoByTwoSaplingBlock extends SaplingBlock {
    public static final MapCodec<TwoByTwoSaplingBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TreeGrower.CODEC.fieldOf("tree").forGetter(block -> block.treeGrower), propertiesCodec()
    ).apply(instance, TwoByTwoSaplingBlock::new));

    @Override
    public MapCodec<? extends TwoByTwoSaplingBlock> codec() {
        return CODEC;
    }

    public TwoByTwoSaplingBlock(TreeGrower grower, Properties properties) {
        super(grower, properties);
    }

    // "fixes" https://bugs.mojang.com/browse/MC-168446 for 2x2 saplings
    // this isn't the actual fix for this bug, but i'll do
    // ~isa 15-11-26 (copied from The Mato)
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        boolean isTwoByTwo = false;
        for (int x = 0; x >= -1; x--) {
            for (int y = 0; y >= -1; y--) {
                isTwoByTwo |= TreeGrower.isTwoByTwoSapling(state, level, pos, x, y);
            }
        }
        return isTwoByTwo;
    }
}
