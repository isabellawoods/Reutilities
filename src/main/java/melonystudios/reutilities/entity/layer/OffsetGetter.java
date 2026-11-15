package melonystudios.reutilities.entity.layer;

/// Gets the [CapeOffsets] of a given living entity.
public interface OffsetGetter {
    /// @return The [CapeOffsets] used to move the cape with the entity's movement.
    CapeOffsets getOffsets();
}
