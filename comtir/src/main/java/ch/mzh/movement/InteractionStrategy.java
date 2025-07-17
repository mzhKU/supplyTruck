package ch.mzh.movement;

import ch.mzh.components.logistics.FuelSystem;
import ch.mzh.model.Entity;

public interface InteractionStrategy {
    boolean handleInteraction(Entity movedEntity, FuelSystem fuelSystem);
}
