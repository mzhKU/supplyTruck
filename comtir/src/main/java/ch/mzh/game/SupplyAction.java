package ch.mzh.game;

import ch.mzh.model.Entity;

public class SupplyAction {

    private final Entity refueler;
    private final Entity refuelee;

    public SupplyAction(Entity refueler, Entity refuelee) {
        this.refueler = refueler;
        this.refuelee = refuelee;
    }

    public Entity getRefueler() {
        return refueler;
    }
    public Entity getRefuelee() {
        return refuelee;
    }
}
