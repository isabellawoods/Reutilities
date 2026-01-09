package melonystudios.reutilities.entity.custom;

/// Helpers class that retrieves the arm size from an {@link net.minecraft.client.renderer.entity.EntityRenderer EntityRenderer}.
public interface ArmSize {
    /// @return Whether this renderer has slim arms.
    default boolean reutilities$slimArms() {
        return false;
    }
}
