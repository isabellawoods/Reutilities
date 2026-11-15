package melonystudios.reutilities.util.debug;

/// Debugging flags used for *Reutilities*' development.
public final class ReDebuggingFlags implements DebuggingFlags {
    private static final ReDebuggingFlags FLAGS = new ReDebuggingFlags();

    /// Displays information about the emitted block light and the provided light from the item being held on the player's action bar.
    /// @apiNote You must be holding an item for the display to work properly.
    /// @since 1.5.0
    /// @see melonystudios.reutilities.client.api.ReClientAPI#getItemBrightness ReClientAPI.getItemBrightness
    public static final boolean DEBUG_LIGHT_EMISSION_DISPLAY = FLAGS.locateBooleanFlag("lightEmissionDisplay", false);
    /// Enables the `/mstudios develop/dump_<identifiers> [<alphabetical_sort>]` command.
    /// @since 1.5.0
    /// @see melonystudios.reutilities.command.ReCommands#register ReCommands.register
    public static final boolean DEBUG_DUMP_COMMANDS = FLAGS.locateBooleanFlag("dumpCommands", true);

    @Override
    public String getPrefix() {
        return "re";
    }
}
