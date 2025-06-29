package ch.mzh.input;

import ch.mzh.components.MovementComponent;
import ch.mzh.game.Observer;

import java.util.ArrayList;
import java.util.List;

import ch.mzh.model.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ch.mzh.infrastructure.EntityManager;
import ch.mzh.infrastructure.GameGrid;

import static ch.mzh.model.EntityType.CANNON;
import static ch.mzh.model.EntityType.SUPPLY_TRUCK;

public class InputHandler extends InputAdapter implements Observable {

	private OrthographicCamera camera;
    private GameGrid gameGrid;
    private EntityManager entityManager;
    private Entity selectedEntity;
    private Vector3 mouseWorldPos;

    private final List<Observer> observers = new ArrayList<>();
    
    public InputHandler(OrthographicCamera camera, GameGrid gameGrid, EntityManager entityManager) {
        this.camera = camera;
        this.gameGrid = gameGrid;
        this.entityManager = entityManager;
        this.mouseWorldPos = new Vector3();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            handleEntitySelection(screenX, screenY);
            return true;
        } else if (button == Input.Buttons.RIGHT) { 
            handleMovementCommandOfSelectedEntity(screenX, screenY);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Update mouse world position for potential hover effects
        convertScreenToWorldCoordinates(screenX, screenY);
        return false;
    }

    @Override
    public void addObserver(Observer observer) {
        System.out.println("Adding observer: " + observer);
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    private void handleMovementCommandOfSelectedEntity(int screenX, int screenY) {
        if (!selectedEntity.hasComponent(MovementComponent.class)) {
            return;
        }

        // Convert screen coordinates to world coordinates
        convertScreenToWorldCoordinates(screenX, screenY);
        
        // Convert world coordinates to grid coordinates
        Vector2 gridPos = gameGrid.worldToGrid(mouseWorldPos.x, mouseWorldPos.y);
        int targetX = (int) gridPos.x;
        int targetY = (int) gridPos.y;
        
        if (!gameGrid.isValidPosition(targetX, targetY)) {
            System.out.println("Invalid target position: (" + targetX + ", " + targetY + ")");
            return;
        }
        
        // Check if target terrain is passable
        TerrainType targetTerrain = gameGrid.getTerrainAt(targetX, targetY);
        if (!targetTerrain.isPassable()) {
            System.out.println("Cannot move to impassable terrain at (" + targetX + ", " + targetY + ")");
            return;
        }
        
        // Check if another entity is already at target position
        Entity entityAtTarget = entityManager.getEntityAt(targetX, targetY);
        if (entityAtTarget != null && entityAtTarget != selectedEntity) {
            System.out.println("Cannot move to occupied position: " + entityAtTarget.getType() + " at (" + targetX + ", " + targetY + ")");
            return;
        }
        
        // Calculate movement cost and check if we have enough fuel
        int movementCost = calculateMovementCost(selectedEntity, targetX, targetY);


        EntityType selectedEntityTyp = selectedEntity.getType();
        if (selectedEntityTyp == SUPPLY_TRUCK) {
            SupplyTruck truck = ((SupplyTruck) selectedEntity);
            if (truck.getFuel() < movementCost) {
                System.out.println("Not enough fuel! Need " + movementCost + " but only have " + truck.getFuel());
                return;
            }
            truck.consumeFuel(movementCost);
            System.out.println("Moved cannon to (" + targetX + ", " + targetY + ") - Fuel remaining: " + truck.getFuel());
        }

        if (selectedEntityTyp == CANNON) {
            Cannon cannon = (Cannon) selectedEntity;
            if (cannon.getFuel() < movementCost) {
                System.out.println("Not enough fuel! Need " + movementCost + " but only have " + cannon.getFuel());
                return;
            }
            cannon.consumeFuel(movementCost);
            System.out.println("Moved cannon to (" + targetX + ", " + targetY + ") - Fuel remaining: " + cannon.getFuel());
        }

        selectedEntity.setGridPosition(targetX, targetY);
    }
    
    private int calculateMovementCost(Entity entity, int targetX, int targetY) {
        int startX = entity.getGridX();
        int startY = entity.getGridY();
        
        // Simple Manhattan distance for now (later we can implement proper pathfinding)
        int distance = Math.abs(targetX - startX) + Math.abs(targetY - startY);
        
        // For now, assume average terrain cost of 1.5 (will be more accurate with pathfinding)
        return (int) Math.ceil(distance * 1.5);
    }


    private void handleEntitySelection(int screenX, int screenY) {
        // Convert screen coordinates to world coordinates
        convertScreenToWorldCoordinates(screenX, screenY);
        
        // Convert world coordinates to grid coordinates
        Vector2 gridPos = gameGrid.worldToGrid(mouseWorldPos.x, mouseWorldPos.y);
        int gridX = (int) gridPos.x;
        int gridY = (int) gridPos.y;
        
        // Check if coordinates are within grid bounds
        if (!gameGrid.isValidPosition(gridX, gridY)) {
            selectedEntity = null;
            return;
        }
        
        // Look for entity at clicked position
        Entity clickedEntity = entityManager.getEntityAt(gridX, gridY);
        updateEntityChangeListeners(clickedEntity);
    }

    private void updateEntityChangeListeners(Entity entity) {
        if (entity != null) {
            // Select the clicked entity
            if (entity != this.selectedEntity) {
                this.selectedEntity = entity;
                observers.stream().forEach(o -> o.onEntitySelected(this.selectedEntity));
            }
        } else {
            // Deselect if clicking on empty space
            selectedEntity = null;
            observers.stream().forEach(Observer::onEntityDeselected);
        }
    }

    private void convertScreenToWorldCoordinates(int screenX, int screenY) {
        mouseWorldPos.set(screenX, screenY, 0);
        camera.unproject(mouseWorldPos);
    }
    
    public Vector2 getMouseGridPosition() {
        return gameGrid.worldToGrid(mouseWorldPos.x, mouseWorldPos.y);
    }
    
    public void dispose() {
        // Clean up if needed
    }
}