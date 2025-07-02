package ch.mzh.game;

import ch.mzh.model.Entity;

public interface Observer {

    void onEntityMoved(Entity entity);

    void onEntitySelected(Entity entity);
    
    void onEntityDeselected();
}
