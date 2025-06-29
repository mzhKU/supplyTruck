package ch.mzh.components;

import ch.mzh.infrastructure.EntityManager;
import ch.mzh.model.Entity;

import java.util.List;

public class FuelSystem {
    private EntityManager entityManager;

    public FuelSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void transferIfPossibleFrom(Entity supplier) {
        SupplyComponent supply = supplier.getComponent(SupplyComponent.class);
        if (supply == null) return;

        List<Entity> nearbyEntities = entityManager.getEntitiesInRange(supplier.getGridX(), supplier.getGridY(), supply.getRefuelRange(), supplier);

        for (Entity target : nearbyEntities) {
            if (target == supplier) continue;

            if (supply.canRefuel(supplier, target)) {
                boolean refuelSuccess = supply.refuelTarget(supplier, target);
                if (refuelSuccess) {
                    FuelComponent targetFuel = target.getComponent(FuelComponent.class);
                    System.out.println(supplier.getName() + " refueled " + target.getName() +
                            " (Target current fuel: " + targetFuel.getCurrentFuel() + "/" +
                            " target max fuel: " + targetFuel.getMaxFuel() + ")");
                }
            }
        }
    }
}
