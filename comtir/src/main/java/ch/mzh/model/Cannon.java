package ch.mzh.model;

import ch.mzh.infrastructure.Position2D;

public class Cannon extends Entity {

    private int shots;

    public Cannon(String name, EntityType type, Position2D position) {
        super(name, type, position);
    }
}
