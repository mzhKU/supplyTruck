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
        FuelComponent fuel = supplier.getComponent(FuelComponent.class);
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        if (fuel.isEmpty() || targetFuel.isFull()) return false;

        return isInRange(target.getPosition(), supplier.getPosition());
    }

    public boolean refuelTarget(Entity supplier, Entity target) {

        FuelComponent supply = supplier.getComponent(FuelComponent.class);
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        int targetFuelNeed = targetFuel.getMaxFuel() - targetFuel.getCurrentFuel();
        int transferAmount = Math.min(supply.getCurrentFuel(), targetFuelNeed);

        if (transferAmount > 0) {
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
