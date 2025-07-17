package ch.mzh.components.logistics;

import ch.mzh.components.Component;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;

import static ch.mzh.utilities.Distance.calculateManhattanDistance;

public class SupplyComponent implements Component {

    private int fuelSupplyCapacity;
    private int refuelRange;
    private BaseSupplyComponent baseSupplyComponent;

    public SupplyComponent() {

    }

    public SupplyComponent(BaseSupplyComponent baseSupplyComponent) {
        this.baseSupplyComponent = baseSupplyComponent;
    }

    public SupplyComponent(int fuelSupplyCapacity, int refuelRange) {
        this.fuelSupplyCapacity = fuelSupplyCapacity;
        this.refuelRange = refuelRange;
    }

    public boolean canRefuel(Entity supplier, Entity target) {
        FuelComponent supply = supplier.getComponent(FuelComponent.class);
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        if (supply.isEmpty() || targetFuel.isFull()) return false;

        return isInRange(target.getPosition(), supplier.getPosition());
    }

    public boolean refuelTarget(Entity supplier, Entity target) {
        if (baseSupplyComponent != null) {
            baseSupplyComponent.refuelTarget(supplier, target);
            return true;
        }
        FuelComponent supplyFuel = supplier.getComponent(FuelComponent.class);
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        int targetFuelNeed = targetFuel.getMaxFuel() - targetFuel.getCurrentFuel();
        int transferAmount = Math.min(supplyFuel.getCurrentFuel(), targetFuelNeed);

        // TODO: This condition should emerge by itself from
        // 1: the FuelComponent logic where the currently available fuel
        //    is calculated, it should never drop below zero.
        // 2: The invariant that current fuel cannot be larger than maximal
        //    available fuel.
        if (transferAmount > 0) {
            targetFuel.addFuel(supplyFuel.consumeFuel(transferAmount));
            return true;
        }
        return false;
    }

    public int getFuelSupplyCapacity() {
        return fuelSupplyCapacity;
    }

    public int getRefuelRange() {
        return refuelRange;
    }

    protected boolean isInRange(Position2D targetPosition, Position2D sourcePosition) {
        int distance = calculateManhattanDistance(targetPosition, sourcePosition);
        return distance <= refuelRange;
    }
}
