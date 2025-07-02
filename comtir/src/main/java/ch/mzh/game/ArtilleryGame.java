package ch.mzh.game;

import ch.mzh.components.*;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.SupplyTruck;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import ch.mzh.infrastructure.EntityManager;
import ch.mzh.infrastructure.GameGrid;
import ch.mzh.infrastructure.GameRenderer;
import ch.mzh.input.InputHandler;
import ch.mzh.model.Cannon;
import ch.mzh.model.Entity;
import ch.mzh.model.EntityType;

import static ch.mzh.model.EntityType.SUPPLY_TRUCK;

public class ArtilleryGame extends ApplicationAdapter implements Observer {
    private OrthographicCamera camera;
    private GameGrid gameGrid;
    private EntityManager entityManager;
    private GameRenderer gameRenderer;
    private InputHandler inputHandler;
    private FuelSystem fuelSystem;
    
    // Camera movement
    private static final float CAMERA_SPEED = 600f;
    private static final float ZOOM_SPEED = 1.0f;
    private static final float MIN_ZOOM = 0.1f;
    private static final float MAX_ZOOM = 2.0f;
        
    @Override
    public void create() {
        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.0f;
        
        // Initialize game systems
        gameGrid = new GameGrid(128, 128, 32); // 128x128 grid with 32px tiles
        entityManager = new EntityManager();
        gameRenderer = new GameRenderer(camera, gameGrid);
        
        // Initialize input handling
        inputHandler = new InputHandler(camera, gameGrid, entityManager);
        Gdx.input.setInputProcessor(inputHandler);
        
        inputHandler.addObserver(this);
        
        // Create initial entities for testing
        setupInitialEntities();
    }
    
    private void setupInitialEntities() {
        // Create home base at bottom-left area
        Entity homeBase = new Entity("Base 1", EntityType.BASE, new Position2D(10, 10));
        entityManager.addEntity(homeBase);
        
        // Create cannon near base
        Component cannonMovement = new MovementComponent();
        Component cannonFuel = new FuelComponent(50, 2);

        Entity cannon = new Cannon("Cannon 1", EntityType.CANNON, new Position2D(15, 15));
        cannon.addComponent(cannonMovement);
        cannon.addComponent(cannonFuel);
        entityManager.addEntity(cannon);
        
        // Create some troops
        Component troopMovement = new MovementComponent();
        for (int i = 0; i < 5; i++) {
            Entity troop = new Entity("Troop " + i, EntityType.TROOP, new Position2D(12 + i, 12));
            troop.addComponent(troopMovement);
            entityManager.addEntity(troop);
        }
        
        // Create a supply truck
        Component truckMovement = new MovementComponent();
        Component truckFuel = new FuelComponent(100, 1);
        Component truckSupply = new SupplyComponent(200, 1);

        Entity supplyTruck = new SupplyTruck("Supply Truck 1", SUPPLY_TRUCK, new Position2D(8, 8));
        supplyTruck.addComponent(truckMovement);
        supplyTruck.addComponent(truckFuel);
        supplyTruck.addComponent(truckSupply);
        entityManager.addEntity(supplyTruck);

        fuelSystem = new FuelSystem(entityManager);
    }
    
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        
        handleKeyboardInput(deltaTime);
                
        camera.update();
        
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render game
        gameRenderer.render(entityManager.getEntities(), inputHandler.getSelectedEntity());
        
        renderDebugInfo();
    }

    public void onEntityMoved(Entity movedEntity) {
        boolean transferred = fuelSystem.handleEntityMovement(movedEntity);

        if (transferred) {
            System.out.println("Fuel transferred.");
        }
    }

    @Override
    public void onEntitySelected(Entity entity) {
        printSelectionInfo(entity);
    }

    @Override
    public void onEntityDeselected() {
        printDeselectionInfo();
    }

    private void handleKeyboardInput(float deltaTime) {
        // Camera movement
        Vector3 cameraMovement = new Vector3();
        
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cameraMovement.x -= CAMERA_SPEED * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cameraMovement.x += CAMERA_SPEED * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cameraMovement.y += CAMERA_SPEED * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cameraMovement.y -= CAMERA_SPEED * deltaTime;
        }
        
        camera.translate(cameraMovement);
        
        // Zoom controls
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom = Math.min(camera.zoom + ZOOM_SPEED * deltaTime, MAX_ZOOM);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom = Math.max(camera.zoom - ZOOM_SPEED * deltaTime, MIN_ZOOM);
        }
        
        // Keep camera within bounds
        float halfWidth = camera.viewportWidth * camera.zoom / 2;
        float halfHeight = camera.viewportHeight * camera.zoom / 2;
        
        camera.position.x = Math.max(halfWidth, Math.min(camera.position.x, gameGrid.getWorldWidth() - halfWidth));
        camera.position.y = Math.max(halfHeight, Math.min(camera.position.y, gameGrid.getWorldHeight() - halfHeight));
    }
    
    private void renderDebugInfo() {}
    
    private void printSelectionInfo(Entity selectedEntity) {
        System.out.println("Selected: " + selectedEntity.getType() + " at (" + selectedEntity.getPosition().getX() + ", " + selectedEntity.getPosition().getY() + ")");
    }

    private void printDeselectionInfo() {
        System.out.println("Deselected entity");
    }

    @Override
    public void dispose() {
        inputHandler.removeObserver(this);
        inputHandler.dispose();
    }
}