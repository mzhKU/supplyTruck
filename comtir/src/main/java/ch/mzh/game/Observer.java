package ch.mzh.game;

import ch.mzh.model.Entity;

public interface Observer {

    void onEntitySelected(Entity entity);
    
    void onEntityDeselected();
}
