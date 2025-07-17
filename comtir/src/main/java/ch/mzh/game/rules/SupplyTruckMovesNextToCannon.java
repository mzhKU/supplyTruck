package ch.mzh.game.rules;

import ch.mzh.game.SupplyAction;
import ch.mzh.game.SupplyRule;
import ch.mzh.infrastructure.EntityManager;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;
import ch.mzh.model.EntityType;

import java.util.Optional;

import static ch.mzh.utilities.Distance.calculateManhattanDistance;

public class SupplyTruckMovesNextToCannon implements SupplyRule {

    @Override
    public Optional<SupplyAction> apply(EntityManager entityManager, Entity movedEntity, Position2D endPosition) {
        Entity cannon = entityManager.getEntity("Cannon 1");
        if (calculateManhattanDistance(cannon.getPosition(), endPosition) == 1 && movedEntity.getType() == EntityType.SUPPLY_TRUCK) {
            return Optional.of(new SupplyAction(entityManager.getEntity("Supply Truck 1"), cannon));
        }
        return Optional.empty();
    }
}
