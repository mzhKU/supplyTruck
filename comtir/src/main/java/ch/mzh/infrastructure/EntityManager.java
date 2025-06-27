package ch.mzh.infrastructure;

import com.badlogic.gdx.utils.Array;

import ch.mzh.model.Entity;
import ch.mzh.model.EntityType;

public class EntityManager {
    private Array<Entity> entities;
    
    public EntityManager() {
        entities = new Array<Entity>();
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    public void removeEntity(Entity entity) {
        entities.removeValue(entity, true);
    }
    
    public Array<Entity> getEntities() {
        return entities;
    }
    
    public Array<Entity> getEntitiesOfType(EntityType type) {
        Array<Entity> result = new Array<Entity>();
        for (Entity entity : entities) {
            if (entity.getType() == type && entity.isActive()) {
                result.add(entity);
            }
        }
        return result;
    }
    
    public Entity getEntityAt(int gridX, int gridY) {
        for (Entity entity : entities) {
            if (entity.isActive() && entity.isSelectable() &&
                entity.getGridX() == gridX && 
                entity.getGridY() == gridY) {
                return entity;
            }
        }
        return null;
    }
    
    public Array<Entity> getSelectableEntities() {
        Array<Entity> result = new Array<Entity>();
        for (Entity entity : entities) {
            if (entity.isActive() && entity.isSelectable()) {
                result.add(entity);
            }
        }
        return result;
    }
}