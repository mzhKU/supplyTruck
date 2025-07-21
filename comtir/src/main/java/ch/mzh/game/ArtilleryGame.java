package ch.mzh.game;

import ch.mzh.components.*;
import ch.mzh.components.logistics.*;
import ch.mzh.infrastructure.Position2D;
import ch.mzh.model.*;
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

import java.util.List;

import static ch.mzh.model.EntityType.SUPPLY_TRUCK;

public class ArtilleryGame extends ApplicationAdapter implements Observer {
    private OrthographicCamera camera;
    private GameGrid gameGrid;
    private EntityManager entityManager;
    private GameRenderer gameRenderer;
    private InputHandler inputHandler;
    private FuelSystem fuelSystem;
    private SupplyRuleEngine supplyRuleEngine;
    
    // Camera movement
    private static final float CAMERA_SPEED = 600f;
    private static final float ZOOM_SPEED = 1.0f;
    private static final float MIN_ZOOM = 0.1f;
    private static final float MAX_ZOOM = 2.0f;
        
    @Override
    public void create() {
        initializeCamera();
        initializeGrid();
        initializedEntityManager();
        initializeGameRenderer();
        initializeInputHandler();

        createBase();
        createSupplyTruck();
        createCannon();
        createTroops();
        createFuelSystem();
        createSupplyRuleEngine();
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        
        calculateNewCameraPosition(deltaTime);
        camera.update();
        
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render game
        gameRenderer.render(entityManager.getEntities(), inputHandler.getSelectedEntity());
    }

    @Override
    public void onEntitySelected(Entity entity) {
        printSelectionInfo(entity);
    }

    @Override
    public void onEntityDeselected() {
        printDeselectionInfo();
    }

    @Override
    public void dispose() {
        inputHandler.removeObserver(this);
        inputHandler.dispose();
    }

    @Override
    public void onEntityMoved(Entity movedEntity) {
        supplyRuleEngine.processMovement(movedEntity, movedEntity.getPosition());
    }

    private void calculateNewCameraPosition(float deltaTime) {
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

    private void initializeCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.0f;
    }
    private void initializeGrid() {
        gameGrid = new GameGrid(128, 128, 32); // 32px tiles
    }
    private void initializedEntityManager() {
        entityManager = new EntityManager();
    }
    private void initializeGameRenderer() {
        gameRenderer = new GameRenderer(camera, gameGrid);
    }
    private void initializeInputHandler() {
        inputHandler = new InputHandler(camera, gameGrid, entityManager);
        Gdx.input.setInputProcessor(inputHandler);
        inputHandler.addObserver(this);
    }

    private void createBase() {
        Component baseSupplyComponent = new BaseSupplyComponent(1);
        Base homeBase = new Base("Base 1", EntityType.BASE, new Position2D(10, 10));
        homeBase.addComponent(baseSupplyComponent);
        entityManager.addEntity(homeBase);

        List<BaseRefuelPosition> refuelGridPositions = gameGrid.getPositionsWithinDistance(homeBase.getPosition(), 1)
                .stream()
                .map(BaseRefuelPosition::new)
                .toList();
        gameGrid.setRefuelGridPositions(refuelGridPositions);
    }
    private void createSupplyTruck() {
        Component truckMovement = new VehicleMovementComponent();
        Component truckSupply = new VehicleSupplyComponent(1);
        Component truckFuel = new FuelComponent(100, 1);
        Entity supplyTruck = new SupplyTruck("Supply Truck 1", SUPPLY_TRUCK, new Position2D(8, 8));
        supplyTruck.addComponent(truckMovement);
        supplyTruck.addComponent(truckFuel);
        supplyTruck.addComponent(truckSupply);
        entityManager.addEntity(supplyTruck);
    }
    private void createCannon() {
        Component cannonMovement = new VehicleMovementComponent();
        Component cannonFuel = new FuelComponent(50, 2);
        Entity cannon = new Cannon("Cannon 1", EntityType.CANNON, new Position2D(15, 15));
        cannon.addComponent(cannonMovement);
        cannon.addComponent(cannonFuel);
        entityManager.addEntity(cannon);
    }
    private void createTroops() {
        Component troopMovement = new TroopMovementComponent();
        for (int i = 0; i < 5; i++) {
            Entity troop = new Entity("Troop " + i, EntityType.TROOP, new Position2D(12 + i, 12));
            troop.addComponent(troopMovement);
            entityManager.addEntity(troop);
        }
    }
    private void createFuelSystem() {
        fuelSystem = new FuelSystem();
    }
    private void createSupplyRuleEngine() {
        this.supplyRuleEngine = new SupplyRuleEngine(entityManager, fuelSystem);
    }

    private void printSelectionInfo(Entity selectedEntity) {
        System.out.println("Selected: " + selectedEntity.getType() + " at (" + selectedEntity.getPosition().getX() + ", " + selectedEntity.getPosition().getY() + ")");
    }
    private void printDeselectionInfo() {
        System.out.println("Deselected entity");
    }
}