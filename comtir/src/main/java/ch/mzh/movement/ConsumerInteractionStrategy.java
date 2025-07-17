package ch.mzh.movement;


import ch.mzh.components.logistics.FuelSystem;
import ch.mzh.model.Entity;

// Strategy for fuel-consuming entities (non-supply)
public class ConsumerInteractionStrategy implements InteractionStrategy {
    @Override
    public boolean handleInteraction(Entity movedEntity, FuelSystem fuelSystem) {
        return fuelSystem.requestRefuelFromNearbySuppliers(movedEntity);
    }
}
