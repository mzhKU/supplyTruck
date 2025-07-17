package ch.mzh.game;

import ch.mzh.infrastructure.EntityManager;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;

import java.util.Optional;

public interface SupplyRule {

    Optional<SupplyAction> apply(EntityManager entityManager, Entity movedEntity, Position2D endPosition);

}
