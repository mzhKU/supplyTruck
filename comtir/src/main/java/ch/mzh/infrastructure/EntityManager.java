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
    
    public Array<Entity> getSelectableEntities() {
        Array<Entity> result = new Array<Entity>();
        for (Entity entity : entities) {
            if (entity.isActive() && entity.isSelectable()) {
                result.add(entity);
            }
        }
        return result;
    }


    /*
    public List<Entity> getSupplyEntitiesAroundSelectedEntity(int selectedX, int selectedY, Entity excludeSelf) {
        Array<Entity> allEntities = getEntities();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                if (dx == 0 && dy == 0) continue;

                int newX = selectedX  + dx;
                int newY = selectedY + dy;

                // Check if the new position is within grid bounds
                if (!isInvalidPosition(newX, newY, gridWidth, gridHeight)) {
                    neighbors.add(new Position2D(newX, newY));
                }
            }
        }

        return List.of();
    }
    */

    public List<Entity> getEntitiesInRange(Position2D supplierPosition, int transferRange, Entity excludeSelf) {
        List<Entity> entitiesInRange = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity == excludeSelf) continue;
            int distance = calculateManhattanDistance(supplierPosition, entity.getPosition());
            if (distance <= transferRange) {
                entitiesInRange.add(entity);
            }
        }
        return entitiesInRange;
    }

    public List<Entity> getRefuelableEntitiesInRange(Position2D supplierPosition, int range, Entity excludeSelf) {
        return getEntitiesInRange(supplierPosition, range, excludeSelf).stream()
                .filter(entity -> {
                    FuelComponent fuel = entity.getComponent(FuelComponent.class);
                    return fuel != null && !fuel.isFull();
                })
                .collect(toList());
    }

    private int calculateManhattanDistance(Position2D supplierPosition, Position2D targetPosition) {
        return Math.abs(supplierPosition.getX() - targetPosition.getX()) + Math.abs(supplierPosition.getY() - targetPosition.getY());
    }
}