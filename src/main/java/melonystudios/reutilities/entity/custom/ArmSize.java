package melonystudios.reutilities.entity.custom;

/// Helpers class that retrieves the arm size from an [`EntityRenderer`][net.minecraft.client.renderer.entity.EntityRenderer].
public interface ArmSize {
    /// @return Whether this renderer has slim arms.
    default boolean reutilities$slimArms() {
        return false;
    }
}
