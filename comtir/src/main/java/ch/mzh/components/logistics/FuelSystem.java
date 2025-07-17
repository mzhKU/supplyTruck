package ch.mzh.components.logistics;

import ch.mzh.infrastructure.EntityManager;
import ch.mzh.model.Entity;
import ch.mzh.model.EntityType;

import java.util.List;

import static java.util.Comparator.comparingInt;

public class FuelSystem {

    public void transferFuel(Entity supplier, Entity receiver) {
        SupplyComponent supplyComp = supplier.getComponent(SupplyComponent.class);

        FuelComponent receiverFuel = receiver.getComponent(FuelComponent.class);

        // supplyComp should be != null here...
        if (supplyComp == null || receiverFuel == null) {
            return;
        }

        // supplyComp.canRefuel was also already called above...
        if (!supplyComp.canRefuel(supplier, receiver)) return;

        boolean success = supplyComp.refuelTarget(supplier, receiver);
        if (success) {
            System.out.println(supplier.getName() + " refueled " + receiver.getName() +
                    " (Current: " + receiverFuel.getCurrentFuel() + "/" + receiverFuel.getMaxFuel() + ")");
        } else {
            System.out.println("REFUEL FAILED.");
        }
    }
}
