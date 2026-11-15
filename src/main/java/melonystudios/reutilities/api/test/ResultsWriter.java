package melonystudios.reutilities.api.test;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/// Helper interface for writing test outputs to a file.
/// <pre>{@code
/// Test results format:
/// {
///   "results": "Stores the output of the tested element, usually the encoded output of a codec.",
///   "error": "Optional. An error message for when the result is not properly encoded."
/// }
/// }</pre>
/// @since *Reutilities* `1.6.0`
public interface ResultsWriter {
    UnaryOperator<String> SAVE_DIRECTORY = testName -> "melony-studios-tests/" + testName;
    Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    String RESULTS_FIELD = "results";
    String ERROR_FIELD = "error";

    /// @return A unique identifier for a test class. This is used as the folder location when saving tests contained within it.
    String testIdentifier();

    /// Saves the outputs of a test to a `.json` file in the `/minecraft-junit/melony-studios-tests/<test name>` directory.
    ///
    /// Tests saved with this file will always have their input [wrapped in a `results` field][#results].
    /// @param input The JSON output of the codec's encoder.
    /// @param fileName The name for the test file within its test's folder.
    default void save(JsonElement input, String fileName) {
        this.save(input, fileName, this::results);
    }

    /// Saves the outputs of a test to a `.json` file in the `/minecraft-junit/melony-studios-tests/<test name>` directory.
    /// @param input The JSON output of the codec's encoder.
    /// @param fileName The name for the test file within its test's folder.
    /// @param validator A [Function] to validate the JSON input, or to add extra fields (such as the `error` message field) to the file being written.
    default void save(JsonElement input, String fileName, Function<JsonElement, JsonObject> validator) {
        String json = GSON.toJson(validator.apply(input));
        File fileOutput = new File(FMLPaths.GAMEDIR.get().toFile(), SAVE_DIRECTORY.apply(this.testIdentifier()));

        if (fileOutput.exists() || fileOutput.mkdirs()) {
            try {
                OutputStream stream = Files.newOutputStream(new File(fileOutput, fileName + ".json").toPath());
                Writer writer = new OutputStreamWriter(stream, Charsets.UTF_8.newEncoder());
                writer.write(json);
                writer.flush();
                writer.close();
                LoggerFactory.getLogger(this.testIdentifier()).info(
                        MarkerFactory.getMarker(fileName),
                        "Successfully saved test results to \"minecraft-junit/melony-studios-tests/\""
                );
            } catch (IOException exception) {
                throw new RuntimeException("Failed to save test results for test [" + this.testIdentifier() + "/" + fileName + "] to \"/minecraft-junit/melony-studios-tests/\"");
            }
        } else {
            throw new RuntimeException("Failed to create test results folder for test [" + this.testIdentifier() + "/" + fileName + "] at \"/minecraft-junit/melony-studios-tests/\"");
        }
    }

    /// Wraps a [JSON element][JsonElement] in a [`results`][#RESULTS_FIELD] field.
    /// @param results The JSON output of a codec.
    /// @return A [JSON object][JsonObject] with the input wrapped in a [`results`][#RESULTS_FIELD] field.
    default JsonObject results(JsonElement results) {
        return Util.make(new JsonObject(), object -> object.add(RESULTS_FIELD, results));
    }

    /// Wraps a [JSON element][JsonElement] in a [`error`][#ERROR_FIELD] field.
    /// @param error The JSON output of a codec.
    /// @return A [JSON object][JsonObject] with the input wrapped in a [`error`][#ERROR_FIELD] field.
    default JsonObject error(JsonElement error) {
        return Util.make(new JsonObject(), object -> object.add(ERROR_FIELD, error));
    }
}
