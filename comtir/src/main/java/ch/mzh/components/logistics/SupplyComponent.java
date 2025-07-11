package ch.mzh.components.logistics;

import ch.mzh.components.Component;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;

import static ch.mzh.utilities.Distance.calculateManhattanDistance;

public class SupplyComponent implements Component {

    private int fuelSupplyCapacity;
    private int refuelRange;

    public SupplyComponent() {}

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
    protected boolean isInRange(Position2D targetPosition, Position2D sourcePosition) {
        int distance = calculateManhattanDistance(targetPosition, sourcePosition);
        return distance <= refuelRange;
    }
}
