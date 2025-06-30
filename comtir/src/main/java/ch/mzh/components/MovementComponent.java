package ch.mzh.components;

import ch.mzh.infrastructure.GameGrid;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;

public class MovementComponent implements Component {

    public MovementComponent() {
    }

    public boolean cannotMoveTo(Entity entity, Position2D targetPosition, GameGrid grid) {
        int distance = calculateDistance(entity.getPosition(), targetPosition);
        FuelComponent fuel = entity.getComponent(FuelComponent.class);
        if (!fuel.canAffordMove(distance) || grid.isInvalidPosition(targetPosition)) return true;
        return false;
    }

    public boolean move(Entity entity, Position2D targetPosition, GameGrid grid) {

        if (cannotMoveTo(entity, targetPosition, grid)) return false;

        FuelComponent fuel = entity.getComponent(FuelComponent.class);
        fuel.consumeFuel(fuel.calculateMovementCost(calculateDistance(entity.getPosition(), targetPosition)));

        entity.setGridPosition(targetPosition);
        return true;
    }

    private int calculateDistance(Position2D from, Position2D to) {
        return Math.abs(to.getX() - from.getX()) + Math.abs(to.getY() - from.getY());
    }

}
