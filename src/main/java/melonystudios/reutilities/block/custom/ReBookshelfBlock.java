package melonystudios.reutilities.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ReBookshelfBlock extends Block {
    public static final MapCodec<ReBookshelfBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("enchanting_power", 1F).forGetter(block -> block.enchantingPower), propertiesCodec()
    ).apply(instance, ReBookshelfBlock::new));
    protected final float enchantingPower;

    public ReBookshelfBlock(Properties properties) {
        this(1, properties);
    }

    public ReBookshelfBlock(float enchantingPower, Properties properties) {
        super(properties);
        this.enchantingPower = enchantingPower;
    }

    @Override
    @NotNull
    protected MapCodec<? extends ReBookshelfBlock> codec() {
        return CODEC;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
        return state.is(BlockTags.ENCHANTMENT_POWER_PROVIDER) ? this.enchantingPower : 0;
    }
}
