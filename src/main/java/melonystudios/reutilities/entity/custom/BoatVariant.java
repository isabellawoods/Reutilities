package melonystudios.reutilities.entity.custom;

import melonystudios.reutilities.api.BoatType;

/// Simple interface for getting and setting {@linkplain BoatType boat types} in <i>Reutilities</i>' boats.
public interface BoatVariant {
    /// Sets the boat's wood type to this new type.
    /// @param type The boat type.
    void setBoatType(BoatType type);

    /// Gets the boat's wood type.
    BoatType getBoatType();
}
