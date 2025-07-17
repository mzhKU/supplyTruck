package ch.mzh.game.rules;

import ch.mzh.game.SupplyAction;
import ch.mzh.game.SupplyRule;
import ch.mzh.infrastructure.EntityManager;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;
import ch.mzh.model.EntityType;

import java.util.Optional;
import java.util.Set;

import static ch.mzh.utilities.Distance.calculateManhattanDistance;

public class VehicleMovesNextToBase implements SupplyRule {

    private static final Set<EntityType> REFUELABLE_TYPES = Set.of(EntityType.CANNON, EntityType.SUPPLY_TRUCK);

    @Override
    public Optional<SupplyAction> apply(EntityManager entityManager, Entity movedEntity, Position2D endPosition) {
        // TODO: Introduce a vehicle type
        if (!REFUELABLE_TYPES.contains(movedEntity.getType())) {
            return Optional.empty();
        }

        Entity base = entityManager.getEntity("Base 1");
        if (calculateManhattanDistance(base.getPosition(), endPosition) == 1) {
            return Optional.of(new SupplyAction(base, movedEntity));
        }
        return Optional.empty();
    }
}
