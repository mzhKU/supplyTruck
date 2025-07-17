package ch.mzh.movement;

import ch.mzh.components.logistics.FuelComponent;
import ch.mzh.components.logistics.SupplyComponent;
import ch.mzh.model.Entity;

// Strategy factory to determine which strategy to use
public class InteractionStrategyFactory {
    public static InteractionStrategy getStrategy(Entity entity) {

        if (entity.hasComponent(SupplyComponent.class)) {
            return new SupplyInteractionStrategy();
        } else if (entity.hasComponent(FuelComponent.class)) {
            return new ConsumerInteractionStrategy();
        } else {
            return new NoFuelInteractionStrategy();
        }
    }
}