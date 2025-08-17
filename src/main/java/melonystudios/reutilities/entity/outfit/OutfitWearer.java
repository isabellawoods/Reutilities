package melonystudios.reutilities.entity.outfit;

/// Interface containing methods related to the "<code>outfit</code>" string tag on entities.
public interface OutfitWearer {
    /// Gets a string for an {@link OutfitDefinition outfit definition} for this entity. Used to render the full-body outfit.
    String getOutfitDefinition();

    /// Sets the entity's outfit to the specified outfit definition.
    /// @param definition A string for the outfit.
    void setOutfitDefinition(String definition);

    /// Whether this entity has a full-body outfit.
    boolean isWearingOutfit();
}
