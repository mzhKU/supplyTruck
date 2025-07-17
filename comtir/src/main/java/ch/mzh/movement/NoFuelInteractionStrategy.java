package ch.mzh.movement;

import ch.mzh.components.logistics.FuelSystem;
import ch.mzh.model.Entity;

// Strategy for entities without fuel components
public class NoFuelInteractionStrategy implements InteractionStrategy {
    @Override
    public boolean handleInteraction(Entity movedEntity, FuelSystem fuelSystem) {
        return false; // No fuel-related actions
    }
}
