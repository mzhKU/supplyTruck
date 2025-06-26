package ch.mzh.model;

public class Cannon extends Entity {

    private int fuel;
    
    public Cannon(EntityType type, int gridX, int gridY, int fuel) {
        super(type, gridX, gridY);
        this.fuel = fuel;
    }

    public int getFuel() {
        return this.fuel;
    }

    public void consumeFuel(int movementCost) {
        fuel -= movementCost;
    }
}
