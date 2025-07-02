package ch.mzh.components;

import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;

public class MovementComponent implements Component {

    public MovementComponent() {
    }

    public boolean move(Entity selectedEntity, Position2D targetPosition) {
        FuelComponent fuel = selectedEntity.getComponent(FuelComponent.class);
        int distance = calculateDistance(selectedEntity.getPosition(), targetPosition);

        if (fuel == null || !fuel.canAffordMove(distance)) return false; // TODO: Troops have infinite fuel

        fuel.consumeFuel(fuel.calculateMovementCost(distance));
        selectedEntity.setGridPosition(targetPosition);

        return true;
    }

    private int calculateDistance(Position2D from, Position2D to) {
        return Math.abs(to.getX() - from.getX()) + Math.abs(to.getY() - from.getY());
    }


}
