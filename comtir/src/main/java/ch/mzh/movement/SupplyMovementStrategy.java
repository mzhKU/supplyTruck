package ch.mzh.movement;

import ch.mzh.components.FuelSystem;
import ch.mzh.model.Entity;

public class SupplyMovementStrategy implements MovementStrategy {
    @Override
    public boolean handleMovement(Entity movedEntity, FuelSystem fuelSystem) {
        return fuelSystem.refuelNearbyEntities(movedEntity);
    }
}
