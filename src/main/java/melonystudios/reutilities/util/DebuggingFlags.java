package melonystudios.reutilities.util;

/// Debugging flags used for *Reutilities* development.
public final class DebuggingFlags {
    /// Displays information about the emitted block light and the provided light from the item being held on the player's action bar.
    /// @see melonystudios.reutilities.api.ReAPI#getItemBrightness ReAPI.getItemBrightness
    public static final boolean DEBUG_LIGHT_EMISSION_DISPLAY = false;
    /// Enables the `/melonystudios reutilities:dump_<locations> <alphabetical_sort>` command.
    /// @see melonystudios.reutilities.command.ReCommands#register ReCommands.register
    public static final boolean DEBUG_DUMP_COMMANDS = true;
}
