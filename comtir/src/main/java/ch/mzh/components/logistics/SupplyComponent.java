package ch.mzh.components.logistics;

import ch.mzh.components.Component;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;
import ch.mzh.model.EntityType;

import static ch.mzh.utilities.Distance.calculateManhattanDistance;

public class SupplyComponent implements Component {

    private int refuelRange;

    public SupplyComponent(int refuelRange) {
        this.refuelRange = refuelRange;
    }

    public boolean canRefuel(Entity supplier, Entity target) {
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        if (targetFuel == null || targetFuel.isFull()) {
            return false;
        }
        if (!hasAvailableFuel(supplier)) {
            return false;
        }

        return isInRange(target.getPosition(), supplier.getPosition());
    }

    public boolean refuelTarget(Entity supplier, Entity target) {
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        if (targetFuel == null) {
            return false;
        }

        int targetFuelNeed = targetFuel.getMaxFuel() - targetFuel.getCurrentFuel();

        if (targetFuelNeed <= 0) {
            return false; // Target is already full
        }

        int transferAmount;

        if (supplier.getType() == EntityType.BASE) {
            transferAmount = targetFuelNeed;
            targetFuel.addFuel(transferAmount);
        } else {
            FuelComponent supplyFuel = supplier.getComponent(FuelComponent.class);
            if (supplyFuel == null) {
                return false;
            }

            transferAmount = Math.min(supplyFuel.getCurrentFuel(), targetFuelNeed);

            // TODO: This condition should emerge by itself from
            // 1: the FuelComponent logic where the currently available fuel
            //    is calculated, it should never drop below zero.
            // 2: The invariant that current fuel cannot be larger than maximal
            //    available fuel.
            if (transferAmount > 0) {
                targetFuel.addFuel(supplyFuel.consumeFuel(transferAmount));
            }
        }
        return transferAmount > 0;
    }

    private boolean hasAvailableFuel(Entity supplier) {
        if (supplier.getType() == EntityType.BASE) {
            return true; // Base always has fuel available
        }

        FuelComponent supply = supplier.getComponent(FuelComponent.class);
        return supply != null && !supply.isEmpty();
    }

    private boolean isInRange(Position2D targetPosition, Position2D sourcePosition) {
        int distance = calculateManhattanDistance(targetPosition, sourcePosition);
        return distance <= refuelRange;
    }
}
