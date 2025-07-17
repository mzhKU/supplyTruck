package ch.mzh.model;

import ch.mzh.infrastructure.Position2D;

public class BaseRefuelPosition extends Position2D {

    public BaseRefuelPosition(Position2D p) {
        super(p.getX(), p.getY());
    }

}
