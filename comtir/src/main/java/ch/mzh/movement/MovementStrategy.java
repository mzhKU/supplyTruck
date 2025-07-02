package ch.mzh.movement;

import ch.mzh.components.FuelSystem;
import ch.mzh.model.Entity;

public interface MovementStrategy {
    boolean handleMovement(Entity movedEntity, FuelSystem fuelSystem);
}
