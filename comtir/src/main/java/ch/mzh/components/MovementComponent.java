package ch.mzh.components;

import ch.mzh.model.Entity;

public class MovementComponent implements Component {

    private MovementType movementType;

    public MovementComponent(MovementType movementType) {
        this.movementType = movementType;
    }

    public boolean move(Entity entity, int targetX, int targetY) {
        return true;
    }

    public MovementType getMovementType() {
        return this.movementType;
    }
}
