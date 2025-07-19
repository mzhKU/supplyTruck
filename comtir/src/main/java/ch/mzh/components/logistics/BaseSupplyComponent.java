package ch.mzh.components.logistics;

import ch.mzh.model.Entity;

public class BaseSupplyComponent extends SupplyComponent {

    public BaseSupplyComponent(int refuelRange) {
        super(refuelRange);
    }

    @Override
    public boolean canRefuel(Entity supplier, Entity target) {
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        if (targetFuel.isFull()) {
            return false;
        }
        return isInRange(target.getPosition(), supplier.getPosition());
    }

    @Override
    public boolean refuelTarget(Entity supplier, Entity target) {
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);

        int targetFuelNeed = targetFuel.getMaxFuel() - targetFuel.getCurrentFuel();

        if (targetFuelNeed <= 0) {
            return false; // Target is already full
        }
        targetFuel.addFuel(targetFuelNeed);
        return true;
    }
}
