package ch.mzh.infrastructure;

import com.badlogic.gdx.utils.Array;

import ch.mzh.model.Entity;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ch.mzh.utilities.Distance.calculateManhattanDistance;

public class EntityManager {
    private Array<Entity> entities;
    private ArrayMap<String, Entity> entitiesMap;
    
    public EntityManager() {
        entities = new Array<Entity>();
        entitiesMap = new ArrayMap<>();
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
        entitiesMap.put(entity.getName(), entity);
    }
    
    public void removeEntity(Entity entity) {
        entities.removeValue(entity, true);
        entitiesMap.removeKey(entity.getName());
    }
    
    public Array<Entity> getEntities() {
        return entities;
    }

    public Entity getEntity(String entityName) {
        return entitiesMap.get(entityName);
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
}