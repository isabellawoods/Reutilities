package melonystudios.mellotests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import melonystudios.reutilities.api.ReCodecs;
import melonystudios.reutilities.api.picker.ColorPicker;
import melonystudios.reutilities.api.picker.ColorBlendingOrder;
import melonystudios.reutilities.api.test.CodecParseException;
import melonystudios.reutilities.api.test.ResultsWriter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ColorPickerTests implements ResultsWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger("ColorPickerTests");
    private static final ColorPicker EXAMPLE_PICKER = new ColorPicker(
            List.of(0xFFFFFF, 0xA0A0A0),
            UniformInt.of(1, 4),
            ColorBlendingOrder.BEFORE,
            List.of(0xFFC55F, 0xFFA134, 0xCF8EDA, 0xD11D49, 0xEBF1F4, 0xD0A775, 0x0F59AE, 0xE92FD5, 0xB4CF6C),
            Optional.of(ResourceLocation.fromNamespaceAndPath("melony-studios-tests", "color_picker/example_picker"))
    );
    // based on Stancements' recorded discs from 0.4.3 and up
    private static final ColorPicker RECORDED_DISC_043 = new ColorPicker(
            List.of(),
            ConstantInt.of(1),
            ColorBlendingOrder.BEFORE,
            ColorPicker.DYE_COLORS_BY_TEXTURE_DIFFUSE,
            Optional.of(ResourceLocation.fromNamespaceAndPath("melony-studios-tests", "color_picker/recorded_disc_043"))
    );

    @Override
    public String testIdentifier() {
        return "color_picker";
    }

    @Test
    public void trySavingJSON() {
        ColorPicker.DIRECT_CODEC
                .encodeStart(JsonOps.INSTANCE, EXAMPLE_PICKER)
                .resultOrPartial(error -> {
                    this.save(new JsonPrimitive(error), "example_picker_json", this::error);
                    throw new CodecParseException("Failed to encode ColorPicker.DIRECT_CODEC for 'melony-studios-tests:example_picker': " + error);
                })
                .ifPresent(element -> this.save(element, "example_picker_json", this::results));
    }

    @Test
    public void sample25Times() {
        int samples = 25;
        int randomSourceSeed = "notthesameasbefore".hashCode();
        JsonObject object = new JsonObject();
        JsonArray colors = new JsonArray();
        JsonArray errors = new JsonArray();

        RandomSource source = RandomSource.create(randomSourceSeed);
        for (int i = 0; i < samples; ++i) {
            int color = ColorPicker.sample(EXAMPLE_PICKER, source).asDecimal();
            ReCodecs.RGB_CODEC.encodeStart(JsonOps.INSTANCE, color)
                    .resultOrPartial(error -> {
                        String message = String.format("Failed to encode ReCodecs.ARGB_CODEC for value %s: %s", color, error);
                        LOGGER.error(message);
                        errors.add(message);
                    })
                    .ifPresent(colors::add);
        }

        object.add("colors", colors);
        object.addProperty("samples", samples);
        object.addProperty("random_source_seed", randomSourceSeed);

        this.save(object, "example_picker_" + samples + "_samples", results -> {
            JsonObject results1 = this.results(results);
            if (!errors.isEmpty()) results1.add(ERROR_FIELD, errors);
            return results1;
        });

        if (!errors.isEmpty()) throw new CodecParseException("Failed to encode ReCodecs.ARGB_CODEC for value(s): " + errors);
    }
}
