package ch.mzh.components.logistics;

import ch.mzh.components.Component;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.Entity;

import static ch.mzh.utilities.Distance.calculateManhattanDistance;

public abstract class SupplyComponent implements Component {

    private final int refuelRange;

    public SupplyComponent(int refuelRange) {
        this.refuelRange = refuelRange;
    }

    public abstract boolean canRefuel(Entity supplier, Entity target);
    public abstract boolean refuelTarget(Entity supplier, Entity target);

    protected boolean isInRange(Position2D targetPosition, Position2D sourcePosition) {
        int distance = calculateManhattanDistance(targetPosition, sourcePosition);
        return distance <= refuelRange;
    }
}
