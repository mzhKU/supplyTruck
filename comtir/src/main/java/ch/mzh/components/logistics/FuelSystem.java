package ch.mzh.components.logistics;

import ch.mzh.infrastructure.EntityManager;
import ch.mzh.model.Entity;

import java.util.List;

import static java.util.Comparator.comparingInt;

public class FuelSystem {
    private EntityManager entityManager;

    public FuelSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean handleEntityMovement(Entity movedEntity) {
        if (movedEntity.hasComponent(SupplyComponent.class)) {
            return refuelNearbyEntities(movedEntity);
        } else if (movedEntity.hasComponent(FuelComponent.class)) {
            return requestRefuelFromNearbySuppliers(movedEntity);
        }
        return false;
    }

    // Supply truck refuels nearby entities (prioritizes lowest fuel)
    public boolean refuelNearbyEntities(Entity supplier) {
        SupplyComponent supplyComp = supplier.getComponent(SupplyComponent.class);
        if (supplyComp == null) return false;

        List<Entity> targets = entityManager.getEntitiesInRange(supplier, supplyComp.getRefuelRange()).stream()
                .filter(e -> e != supplier && e.hasComponent(FuelComponent.class))
                .filter(e -> supplyComp.canRefuel(supplier, e))
                .sorted(comparingInt(e -> e.getComponent(FuelComponent.class).getCurrentFuel()))
                .toList();

        for (Entity target : targets) {
            if (transferFuel(supplier, target)) {
                return true; // Refuel one entity per move
            }
        }
        return false;
    }

    // Entity requests refuel from nearby supply trucks
    public boolean requestRefuelFromNearbySuppliers(Entity receiver) {
        if (receiver.hasComponent(SupplyComponent.class)) return false; // TODO: Supply trucks refuel at the base

        List<Entity> suppliers = entityManager.getEntitiesInRange(receiver, 1).stream() // Use standard range or make configurable
                .filter(e -> e.hasComponent(SupplyComponent.class))
                .filter(e -> e.getComponent(SupplyComponent.class).canRefuel(e, receiver))
                .sorted(comparingInt((Entity e) ->
                        e.getComponent(FuelComponent.class).getCurrentFuel()).reversed())
                .toList();

        for (Entity supplier : suppliers) {
            if (transferFuel(supplier, receiver)) {
                return true;
            }
        }
        return false;
    }

    public boolean transferFuel(Entity supplier, Entity receiver) {
        SupplyComponent supplyComp = supplier.getComponent(SupplyComponent.class);
        FuelComponent supplierFuel = supplier.getComponent(FuelComponent.class);
        FuelComponent receiverFuel = receiver.getComponent(FuelComponent.class);

        // supplyComp should be != null here...
        if (supplyComp == null || supplierFuel == null || receiverFuel == null) {
            return false;
        }

        // supplyComp.canRefuel was also already called above...
        if (!supplyComp.canRefuel(supplier, receiver)) return false;

        boolean success = supplyComp.refuelTarget(supplier, receiver);
        if (success) {
            System.out.println(supplier.getName() + " refueled " + receiver.getName() +
                    " (Current: " + receiverFuel.getCurrentFuel() + "/" + receiverFuel.getMaxFuel() + ")");
        }
        return success;
    }
}
