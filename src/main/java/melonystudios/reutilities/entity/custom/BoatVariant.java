package melonystudios.reutilities.entity.custom;

import melonystudios.reutilities.api.boat.BoatType;

/// Simple interface for getting and setting [boat types][BoatType] in *Reutilities'* boats.
public interface BoatVariant {
    /// Sets the boat's wood type to this new type.
    /// @param type The boat type.
    void setBoatType(BoatType type);

    /// Gets the boat's wood type.
    BoatType getBoatType();
}
