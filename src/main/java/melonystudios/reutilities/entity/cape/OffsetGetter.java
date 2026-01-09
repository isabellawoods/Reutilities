package melonystudios.reutilities.entity.cape;

/// Gets the {@link CapeOffsets} of a given living entity.
public interface OffsetGetter {
    /// @return The {@link CapeOffsets} used to move the cape with the entity's movement.
    CapeOffsets getOffsets();
}
