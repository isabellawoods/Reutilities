package melonystudios.behaviorapi;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum SoundSourceCodec implements StringRepresentable {
    MASTER(0, "master"),
    MUSIC(1, "music"),
    RECORDS(2, "record"),
    WEATHER(3, "weather"),
    BLOCKS(4, "block"),
    HOSTILE(5, "hostile"),
    NEUTRAL(6, "neutral"),
    PLAYERS(7, "player"),
    AMBIENT(8, "ambient"),
    VOICE(9, "voice");

    public static final Codec<SoundSourceCodec> CODEC = StringRepresentable.fromValues(SoundSourceCodec::values);
    public static final IntFunction<SoundSourceCodec> BY_ID = ByIdMap.continuous(source -> source.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, SoundSourceCodec> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, source -> source.id);
    private final int id;
    private final String name;

    SoundSourceCodec(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SoundSource decodec() {
        return switch (this) {
            case MASTER -> SoundSource.MASTER;
            case MUSIC -> SoundSource.MUSIC;
            case RECORDS -> SoundSource.RECORDS;
            case WEATHER -> SoundSource.WEATHER;
            case BLOCKS -> SoundSource.BLOCKS;
            case HOSTILE -> SoundSource.HOSTILE;
            case NEUTRAL -> SoundSource.NEUTRAL;
            case PLAYERS -> SoundSource.PLAYERS;
            case AMBIENT -> SoundSource.AMBIENT;
            case VOICE -> SoundSource.VOICE;
        };
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }
}
