package ch.mzh.components.logistics;

import ch.mzh.game.SupplyAction;
import ch.mzh.game.SupplyRule;
import ch.mzh.game.rules.CannonMovesNextToSupplyTruck;
import ch.mzh.game.rules.SupplyTruckMovesNextToCannon;
import ch.mzh.game.rules.VehicleMovesNextToBase;
import ch.mzh.infrastructure.EntityManager;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SupplyRuleEngine {
    private final List<SupplyRule> rules;
    private final EntityManager entityManager;
    private final FuelSystem fuelSystem;

    public SupplyRuleEngine(EntityManager entityManager, FuelSystem fuelSystem) {
        this.rules = Arrays.asList(
                new VehicleMovesNextToBase(),
                new SupplyTruckMovesNextToCannon(),
                new CannonMovesNextToSupplyTruck()
        );
        this.entityManager = entityManager;
        this.fuelSystem = fuelSystem;
    }

    public void processMovement(Entity movedEntity, Position2D endPosition) {
        rules.stream()
                .map(rule -> rule.apply(entityManager, movedEntity, endPosition))
                .flatMap(Optional::stream)
                .forEach(this::executeSupplyAction);
    }

    private void executeSupplyAction(SupplyAction action) {
        fuelSystem.transferFuel(action.getRefueler(), action.getRefuelee());
    }
}