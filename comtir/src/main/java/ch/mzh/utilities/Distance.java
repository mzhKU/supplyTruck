package ch.mzh.utilities;

import ch.mzh.infrastructure.Position2D;

public class Distance {

    public static int calculateManhattanDistance(Position2D from, Position2D to) {
        return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
    }
}
