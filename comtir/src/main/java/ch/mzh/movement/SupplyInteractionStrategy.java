package ch.mzh.movement;

import ch.mzh.components.logistics.FuelSystem;
import ch.mzh.model.Entity;

public class SupplyInteractionStrategy implements InteractionStrategy {
    @Override
    public boolean handleInteraction(Entity movedEntity, FuelSystem fuelSystem) {
        return fuelSystem.refuelNearbyEntities(movedEntity);
    }
}
