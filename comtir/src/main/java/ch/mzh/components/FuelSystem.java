package ch.mzh.components;

import ch.mzh.infrastructure.EntityManager;
import ch.mzh.model.Entity;

import java.util.List;

public class FuelSystem {
    private EntityManager entityManager;

    public FuelSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void transferIfPossibleFrom(Entity selectedEntity) {

        // List<Entity> supplyEntities = entityManager.getSupplyEntitiesAroundSelectedEntity(selectedEntity.getGridX(), selectedEntity.getGridY(), selectedEntity);

        SupplyComponent supply = selectedEntity.getComponent(SupplyComponent.class);
        if (supply == null) return;

        List<Entity> nearbyEntities = entityManager.getEntitiesInRange(selectedEntity.getPosition(), supply.getRefuelRange(), selectedEntity);

        for (Entity target : nearbyEntities) {
            if (target == selectedEntity) continue;

            if (supply.canRefuel(selectedEntity, target)) {
                boolean refuelSuccess = supply.refuelTarget(selectedEntity, target);
                if (refuelSuccess) {
                    FuelComponent targetFuel = target.getComponent(FuelComponent.class);
                    System.out.println(selectedEntity.getName() + " refueled " + target.getName() +
                            " (Target current fuel: " + targetFuel.getCurrentFuel() + "/" +
                            " target max fuel: " + targetFuel.getMaxFuel() + ")");
                }
            }
        }
    }
}
