package ch.mzh.movement;

import ch.mzh.components.FuelComponent;
import ch.mzh.components.SupplyComponent;
import ch.mzh.model.Entity;

// Strategy factory to determine which strategy to use
public class MovementStrategyFactory {
    public static MovementStrategy getStrategy(Entity entity) {
        if (entity.hasComponent(SupplyComponent.class)) {
            return new SupplyMovementStrategy();
        } else if (entity.hasComponent(FuelComponent.class)) {
            return new ConsumerMovementStrategy();
        } else {
            return new NoFuelMovementStrategy();
        }
    }
}