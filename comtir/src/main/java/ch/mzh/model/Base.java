package ch.mzh.model;

import ch.mzh.game.Observer;
import ch.mzh.infrastructure.Position2D;

public class Base extends Entity implements Observer {

    public Base(String name, EntityType type, Position2D position) {
        super(name, type, position);
    }

    @Override
    public void onEntityMoved(Entity entity) {

    }

    @Override
    public void onEntitySelected(Entity entity) {

    }

    @Override
    public void onEntityDeselected() {

    }
}
