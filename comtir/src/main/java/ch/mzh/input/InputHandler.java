package ch.mzh.input;

import ch.mzh.components.FuelComponent;
import ch.mzh.components.MovementComponent;
import ch.mzh.game.Observer;

import java.util.ArrayList;
import java.util.List;

import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ch.mzh.infrastructure.EntityManager;
import ch.mzh.infrastructure.GameGrid;

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
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    private void handleMovementCommandOfSelectedEntity(int screenX, int screenY) {
        if (isImmobile(selectedEntity)) return;

        convertScreenToWorldCoordinates(screenX, screenY);
        Vector2 gridPos = gameGrid.worldToGrid(mouseWorldPos.x, mouseWorldPos.y);
        Position2D targetPosition = new Position2D((int) gridPos.x, (int) gridPos.y);
        if (gameGrid.isInvalidPosition(targetPosition)) {
            System.out.println("Invalid target position: (" + targetPosition.getX() + ", " + targetPosition.getY() + ")");
            return;
        }

        /*
        TerrainType targetTerrain = gameGrid.getTerrainAt(targetPosition);
        if (!targetTerrain.isPassable()) {
            System.out.println("Cannot move to impassable terrain at (" + targetPosition.getX() + ", " + targetPosition.getY() + ")");
            return;
        }
        */

        Entity entityAtTarget = entityManager.getEntityAt(targetPosition);
        if (entityAtTarget != null && entityAtTarget != selectedEntity) {
            System.out.println("Cannot move to occupied position: " + entityAtTarget.getType() + " at (" + targetPosition.getX() + ", " + targetPosition.getY() + ")");
            return;
        }

        MovementComponent movement = selectedEntity.getComponent(MovementComponent.class);
        boolean moved = movement.move(selectedEntity, targetPosition);

        if (moved) {
            printMovement(selectedEntity, targetPosition);
            observers.stream().forEach(o -> o.onEntityMoved(selectedEntity));
        }
        else {
            System.out.println("Could not move."); // TODO: add reasons for not being able to move.
        }
    }

    private int getDistance(Position2D targetPosition, Position2D startingPosition) {
        return Math.abs(targetPosition.getX() - startingPosition.getX()) + Math.abs(targetPosition.getY() - startingPosition.getY());
    }

    private boolean isImmobile(Entity selectedEntity) {
        if (!selectedEntity.hasComponent(MovementComponent.class))      {
            System.out.println("This entity cannot move: " + selectedEntity.getName());
            return true;
        }
        return false;
    }

    private void handleEntitySelection(int screenX, int screenY) {
        convertScreenToWorldCoordinates(screenX, screenY);
        Vector2 gridPos = gameGrid.worldToGrid(mouseWorldPos.x, mouseWorldPos.y);
        Position2D mousePositionGrid = new Position2D((int) gridPos.x, (int) gridPos.y);

        if (gameGrid.isInvalidPosition(mousePositionGrid)) {
            selectedEntity = null;
            return;
        }

        Entity clickedEntity = entityManager.getEntityAt(mousePositionGrid);
        updateEntityChangeListeners(clickedEntity);
    }

    private void updateEntityChangeListeners(Entity entity) {
        if (entity != null) {
            if (entity != selectedEntity) {
                selectedEntity = entity;
                observers.stream().forEach(o -> o.onEntitySelected(selectedEntity));
            }
        } else {
            selectedEntity = null;
            observers.stream().forEach(Observer::onEntityDeselected);
        }
    }

    private void convertScreenToWorldCoordinates(int screenX, int screenY) {
        mouseWorldPos.set(screenX, screenY, 0);
        camera.unproject(mouseWorldPos);
    }

    private void printMovement(Entity selectedEntity, Position2D targetPosition) {
        FuelComponent fuel = selectedEntity.getComponent(FuelComponent.class);
        System.out.println("Moved " + selectedEntity.getName() + " to (" + targetPosition.getX() + ", " + targetPosition.getY() + "), fuel used: " + fuel.getLastFuelUsage() + ", fuel remaining: " + fuel.getCurrentFuel() + ".");
    }

    public void dispose() {
        // Clean up if needed
    }
}