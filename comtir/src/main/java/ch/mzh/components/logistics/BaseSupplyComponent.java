package ch.mzh.components.logistics;

import ch.mzh.model.Entity;

public class BaseSupplyComponent extends SupplyComponent {

    public BaseSupplyComponent() {
    }

    @Override
    public boolean canRefuel(Entity supplier, Entity target) {
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);
        if (targetFuel.isFull()) return false;
        return isInRange(target.getPosition(), supplier.getPosition());
    }

    @Override
    public boolean refuelTarget(Entity supplier, Entity target) {
        FuelComponent targetFuel = target.getComponent(FuelComponent.class);
        int targetFuelNeeded = targetFuel.getMaxFuel() - targetFuel.getCurrentFuel();
        targetFuel.addFuel(targetFuelNeeded);
        return true;
    }

}
