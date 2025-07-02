package ch.mzh.infrastructure;

import com.badlogic.gdx.utils.Array;

import ch.mzh.model.Entity;

import java.util.ArrayList;
import java.util.List;

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

    public Entity getEntityAt(Position2D position) {
        for (Entity entity : entities) {
            if (entity.isActive() && entity.isSelectable() &&
                entity.getPosition().getX() == position.getX() &&
                entity.getPosition().getY() == position.getY()) {
                return entity;
            }
        }
        return null;
    }

    public List<Entity> getEntitiesInRange(Entity movedEntity, int range) {
        List<Entity> entitiesInRange = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity == movedEntity) continue;
            int distance = calculateManhattanDistance(movedEntity.getPosition(), entity.getPosition());
            if (distance <= range) {
                entitiesInRange.add(entity);
            }
        }
        return entitiesInRange;
    }

    private int calculateManhattanDistance(Position2D supplierPosition, Position2D targetPosition) {
        return Math.abs(supplierPosition.getX() - targetPosition.getX()) + Math.abs(supplierPosition.getY() - targetPosition.getY());
    }
}