package ch.mzh.game.rules;

import ch.mzh.game.SupplyAction;
import ch.mzh.game.SupplyRule;
import ch.mzh.infrastructure.EntityManager;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;
import ch.mzh.model.EntityType;

import java.util.Optional;

import static ch.mzh.utilities.Distance.*;

public class CannonMovesNextToSupplyTruck implements SupplyRule {

    @Override
    public Optional<SupplyAction> apply(EntityManager entityManager, Entity movedEntity, Position2D endPosition) {
        Entity supplyTruck = entityManager.getEntity("Supply Truck 1");
        if (calculateManhattanDistance(supplyTruck.getPosition(), endPosition) == 1 && movedEntity.getType() == EntityType.CANNON) {
            return Optional.of(new SupplyAction(entityManager.getEntity("Supply Truck 1"), movedEntity));
        }
        return Optional.empty();
    }
}
