package melonystudios.mellotests;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import melonystudios.reutilities.api.ReCodecs;
import melonystudios.reutilities.api.test.CodecParseException;
import melonystudios.reutilities.api.test.ResultsWriter;
import net.minecraft.sounds.SoundSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReAPITests implements ResultsWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReAPITests");

    @Override
    public String testIdentifier() {
        return "reutilities_api";
    }

    @Test
    public void testRGBCodecs() {
        ReCodecs.RGB_CODEC
                .encodeStart(JsonOps.INSTANCE, 0x955AE0)
                .resultOrPartial(error -> LOGGER.error("Failed to encode ReCodecs.RGB_CODEC for full color: {}", error))
                .ifPresent(element -> this.save(element, "rgb_codec_full_color", result -> {
                    JsonObject object = this.results(result);
                    if (!element.getAsString().equals("#955ae0")) {
                        String message = "[ReCodecs.RGB_CODEC] Input: 0x955AE0; Expected: #955ae0; Result: " + element.getAsString();
                        object.addProperty(ERROR_FIELD, message);
                        throw new CodecParseException(message);
                    }
                    return object;
                }));

        ReCodecs.RGB_CODEC
                .encodeStart(JsonOps.INSTANCE, 0)
                .resultOrPartial(error -> LOGGER.error("Failed to encode ReCodecs.RGB_CODEC for pure black: {}", error))
                .ifPresent(element -> this.save(element, "rgb_codec_pure_black", result -> {
                    JsonObject object = this.results(result);
                    if (!element.getAsString().equals("#000000")) {
                        String message = "[ReCodecs.RGB_CODEC] Input: 0; Expected: #000000; Result: " + element.getAsString();
                        object.addProperty(ERROR_FIELD, message);
                        throw new CodecParseException(message);
                    }
                    return object;
                }));

        ReCodecs.ARGB_CODEC
                .encodeStart(JsonOps.INSTANCE, 0x7FB00B69)
                .resultOrPartial(error -> LOGGER.error("Failed to encode ReCodecs.ARGB_CODEC: {}", error))
                .ifPresent(element -> this.save(element, "argb_codec", result -> {
                    JsonObject object = this.results(result);
                    if (!element.getAsString().equals("#7fb00b69")) {
                        String message = "[ReCodecs.ARGB_CODEC] Input: 0x7FB00B69; Expected: #7fb00b69; Result: " + element.getAsString();
                        object.addProperty(ERROR_FIELD, message);
                        throw new CodecParseException(message);
                    }
                    return object;
                }));
    }

    @Test
    public void testSoundSourceCodec() {
        ReCodecs.SOUND_SOURCE_CODEC
                .encodeStart(JsonOps.INSTANCE, SoundSource.HOSTILE)
                .resultOrPartial(error -> LOGGER.error("Failed to encode ReCodecs.SOUND_SOURCE_CODEC: {}", error))
                .ifPresent(element -> this.save(element, "sound_source_codec", result -> {
                    JsonObject object = this.results(element);
                    if (!element.getAsString().equals("hostile")) {
                        String message = "[ReCodecs.SOUND_SOURCE_CODEC] Input: SoundSource.HOSTILE; Expected: hostile; Result: " + element.getAsString();
                        object.addProperty(ERROR_FIELD, message);
                        throw new CodecParseException(message);
                    }
                    return object;
                }));

        ReCodecs.SOUND_SOURCE_CODEC
                .encodeStart(JsonOps.INSTANCE, null)
                .resultOrPartial(error -> this.save(new JsonPrimitive(error), "null_sound_source_codec"))
                .ifPresent(element -> {
                    throw new CodecParseException("[ReCodecs.SOUND_SOURCE_CODEC] Input: null; Expected: null; Output: " + element);
                });
    }
}
