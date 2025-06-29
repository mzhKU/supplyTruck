package ch.mzh.model;

public class SupplyTruck extends Entity {
    private int fuel;
    private int tankCapacity;


    public SupplyTruck(EntityType type, int gridX, int gridY, int fuel, int tankCapacity) {
        super(type, gridX, gridY);
        this.fuel = fuel;
        this.tankCapacity = tankCapacity;
    }

    public void consumeFuel(int movementCost) {
        fuel -= movementCost;
    }

    public int getFuel() { return this.fuel; }

}
