package ch.mzh.components;

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
        return isInRange(target.getGridX(), target.getGridY(), supplier.getGridX(), supplier.getGridY());
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
    private boolean isInRange(int targetX, int targetY, int sourceX, int sourceY) {
        int distance = Math.abs(targetX - sourceX) + Math.abs(targetY - sourceY);
        return distance <= refuelRange;
    }
}
