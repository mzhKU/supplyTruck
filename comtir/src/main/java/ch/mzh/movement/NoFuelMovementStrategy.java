package ch.mzh.movement;

import ch.mzh.components.FuelSystem;
import ch.mzh.model.Entity;

// Strategy for entities without fuel components
public class NoFuelMovementStrategy implements MovementStrategy {
    @Override
    public boolean handleMovement(Entity movedEntity, FuelSystem fuelSystem) {
        return false; // No fuel-related actions
    }
}
