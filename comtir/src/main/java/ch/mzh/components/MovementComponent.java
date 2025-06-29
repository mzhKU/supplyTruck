package ch.mzh.components;

import ch.mzh.infrastructure.GameGrid;
import ch.mzh.model.Entity;

public class MovementComponent implements Component {

    public MovementComponent() {
    }

    public boolean cannotMoveTo(Entity entity, int targetX, int targetY, GameGrid grid) {
        int distance = calculateDistance(entity.getGridX(), entity.getGridY(), targetX, targetY);
        FuelComponent fuel = entity.getComponent(FuelComponent.class);
        if (!fuel.canAffordMove(distance) || !grid.isValidPosition(targetX, targetY)) return true;
        return false;
    }

    public boolean move(Entity entity, int targetX, int targetY, GameGrid grid) {

        if (cannotMoveTo(entity, targetX, targetY, grid)) return false;

        int oldX = entity.getGridX();
        int oldY = entity.getGridY();

        FuelComponent fuel = entity.getComponent(FuelComponent.class);
        fuel.consumeFuel(fuel.calculateMovementCost(calculateDistance(oldX, oldY, targetX, targetY)));

        entity.setGridPosition(targetX, targetY);
        return true;
    }

    private int calculateDistance(int fromX, int fromY, int toX, int toY) {
        return Math.abs(toX - fromX) + Math.abs(toY - fromY);
    }

}
