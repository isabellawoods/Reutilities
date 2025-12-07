package melonystudios.reutilities.entity.outfit;

import java.util.Optional;

public class DefinitionBuilder {
    private OutfitSlot head;
    private OutfitSlot chest;
    private OutfitSlot legs;
    private OutfitSlot feet;
    private OutfitSlot body;
    private OutfitSlot mainhand;
    private OutfitSlot offhand;

    /// The head outfit slot. Used when getting a texture for the entity's head.
    /// @param head The head outfit slot.
    public DefinitionBuilder head(OutfitSlot head) {
        this.head = head;
        return this;
    }

    /// The chest outfit slot. Used when getting a texture for the entity's chest or hands (if they don't have their own).
    /// @param chest The chest outfit slot.
    public DefinitionBuilder chest(OutfitSlot chest) {
        this.chest = chest;
        return this;
    }

    /// The legs outfit slot. Used when getting a texture for the entity's legs.
    /// @param legs The legs outfit slot.
    public DefinitionBuilder legs(OutfitSlot legs) {
        this.legs = legs;
        return this;
    }

    /// The feet outfit slot. Used when getting a texture for the entity's feet.
    /// @param feet The feet outfit slot.
    public DefinitionBuilder feet(OutfitSlot feet) {
        this.feet = feet;
        return this;
    }

    /// The body outfit slot. Used when getting a texture for the entity's body.
    ///
    /// This is provided by *Reutilities* only to have all equipment slots covered, as the mod doesn't provide any use cases for these.
    /// @param body The body outfit slot.
    public DefinitionBuilder body(OutfitSlot body) {
        this.body = body;
        return this;
    }

    /// The main hand outfit slot. Used when getting a texture for the entity's main hand.
    ///
    /// Uses the chest texture if none is defined.
    /// @param mainhand The main hand outfit slot.
    public DefinitionBuilder mainhand(OutfitSlot mainhand) {
        this.mainhand = mainhand;
        return this;
    }

    /// The offhand outfit slot. Used when getting a texture for the entity's offhand.
    ///
    /// Uses the chest texture if none is defined.
    /// @param offhand The offhand outfit slot.
    public DefinitionBuilder offhand(OutfitSlot offhand) {
        this.offhand = offhand;
        return this;
    }

    /// Builds this builder into an outfit definition.
    public OutfitDefinition build() {
        return new OutfitDefinition(Optional.ofNullable(this.head), Optional.ofNullable(this.chest), Optional.ofNullable(this.legs), Optional.ofNullable(this.feet),
                Optional.ofNullable(this.body), Optional.ofNullable(this.mainhand), Optional.ofNullable(this.offhand));
    }
}
