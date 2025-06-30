package ch.mzh.components;

import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;

public class SupplyComponent implements Component {

    private int fuelSupplyCapacity;
    private int refuelRange;

    public SupplyComponent(int fuelSupplyCapacity, int refuelRange) {
        this.fuelSupplyCapacity = fuelSupplyCapacity;
        this.refuelRange = refuelRange;
    }

    public boolean canRefuel(Entity supplier, Entity target) {

        SupplyComponent supply = supplier.getComponent(SupplyComponent.class);
        if (supply == null) {
            return false;
        }

        FuelComponent fuel = supplier.getComponent(FuelComponent.class);
        if (fuel.isEmpty()) {
            return false;
        }

        // Check if target needs fuel
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);
        if (targetFuel == null || targetFuel.isFull()) {
            return false;
        }

        // Check range
        return isInRange(target.getPosition(), supplier.getPosition());
    }

    public boolean refuelTarget(Entity supplier, Entity target) {
        if (!canRefuel(supplier, target)) {
            return false;
        }

        FuelComponent supply = supplier.getComponent(FuelComponent.class);
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        int transferAmount = Math.min(supply.getCurrentFuel(), targetFuel.getMaxFuel() - targetFuel.getCurrentFuel());

        if (transferAmount > 0) {
            // What is consumed is transferred
            targetFuel.addFuel(supply.consumeFuel(transferAmount));
            return true;
        }
        return false;
    }

    public int getFuelSupplyCapacity() { return fuelSupplyCapacity; }
    public int getRefuelRange() { return refuelRange; }
    private boolean isInRange(Position2D targetPosition, Position2D sourcePosition) {
        int distance = Math.abs(targetPosition.getX() - sourcePosition.getX()) + Math.abs(targetPosition.getY() - sourcePosition.getY());
        return distance <= refuelRange;
    }
}
