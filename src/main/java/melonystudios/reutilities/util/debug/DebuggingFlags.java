package melonystudios.reutilities.util.debug;

import melonystudios.reutilities.Reutilities;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/// Debugging flag methods used for my mods.
public interface DebuggingFlags {
    Marker MARKER = MarkerFactory.getMarker("DebuggingFlags");

    /// @return The prefix used for this mod's debugging flags.
    String getPrefix();

    /// Finds the {@link Boolean} debug flag under the name `<prefix>debug.<name>`.
    /// @param name The name of the flag.
    /// @param defaultValue The default value of this flag, used when the flag isn't found and for logging.
    default boolean locateBooleanFlag(String name, boolean defaultValue) {
        String prefixedName = this.getPrefix() + "debug." + name;
        boolean value = this.booleanProperty(prefixedName, defaultValue);

        if (value != defaultValue) Reutilities.LOGGER.debug(MARKER, "Found debug flag: {} = {} (boolean)", prefixedName, value);
        return value;
    }

    /// Finds the {@link Integer} debug flag under the name `<prefix>debug.<name>`.
    /// @param name The name of the flag.
    /// @param defaultValue The default value of this flag, used when the flag isn't found and for logging.
    default int locateIntFlag(String name, int defaultValue) {
        String prefixedName = this.getPrefix() + name;
        int value = this.intProperty(prefixedName, defaultValue);

        if (value != defaultValue) Reutilities.LOGGER.debug(MARKER, "Found debug flag: {} = {} (integer)", prefixedName, value);
        return value;
    }

    /// Finds the {@link Float} debug flag under the name `<prefix>debug.<name>`.
    /// @param name The name of the flag.
    /// @param defaultValue The default value of this flag, used when the flag isn't found and for logging.
    default float locateFloatFlag(String name, float defaultValue) {
        String prefixedName = this.getPrefix() + name;
        float value = this.floatProperty(prefixedName, defaultValue);

        if (value != defaultValue) Reutilities.LOGGER.debug(MARKER, "Found debug flag: {} = {} (float)", prefixedName, value);
        return value;
    }

    default boolean booleanProperty(String name, boolean defaultValue) {
        String value = System.getProperty(name, Boolean.toString(defaultValue));
        return value != null && (value.isEmpty() || Boolean.parseBoolean(value));
    }

    default int intProperty(String name, int defaultValue) {
        String value = System.getProperty(name, Integer.toString(defaultValue));
        return Integer.parseInt(value);
    }

    default float floatProperty(String name, float defaultValue) {
        String value = System.getProperty(name, Float.toString(defaultValue));
        return Float.parseFloat(value);
    }
}
