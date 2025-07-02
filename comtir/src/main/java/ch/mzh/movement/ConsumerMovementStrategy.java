package ch.mzh.movement;


import ch.mzh.components.FuelSystem;
import ch.mzh.model.Entity;

// Strategy for fuel-consuming entities (non-supply)
public class ConsumerMovementStrategy implements MovementStrategy {
    @Override
    public boolean handleMovement(Entity movedEntity, FuelSystem fuelSystem) {
        return fuelSystem.requestRefuelFromNearbySuppliers(movedEntity);
    }
}
