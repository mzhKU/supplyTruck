package ch.mzh.infrastructure;

import ch.mzh.components.FuelComponent;
import com.badlogic.gdx.utils.Array;

import ch.mzh.model.Entity;
import ch.mzh.model.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    public List<Entity> getEntitiesInRange(int supplierX, int supplierY, int transferRange, Entity excludeSelf) {
        List<Entity> entitiesInRange = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity == excludeSelf) continue;
            int distance = calculateManhattanDistance(supplierX, supplierY, entity.getGridX(), entity.getGridY());
            if (distance <= transferRange) {
                entitiesInRange.add(entity);
            }
        }
        return entitiesInRange;
    }

    public List<Entity> getRefuelableEntitiesInRange(int supplierX, int supplierY, int range, Entity excludeSelf) {
        return getEntitiesInRange(supplierX, supplierY, range, excludeSelf).stream()
                .filter(entity -> {
                    FuelComponent fuel = entity.getComponent(FuelComponent.class);
                    return fuel != null && !fuel.isFull();
                })
                .collect(toList());
    }

    private int calculateManhattanDistance(int supplierX, int supplierY, int targetX, int targetY) {
        return Math.abs(supplierX - targetX) + Math.abs(supplierY - targetY);
    }
}