package ch.mzh.game;

// Main Game Class
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

public class ArtilleryGame extends ApplicationAdapter {
    private OrthographicCamera camera;
    private GameGrid gameGrid;
    private EntityManager entityManager;
    private GameRenderer gameRenderer;
    private InputHandler inputHandler;
    
    // Camera movement
    private static final float CAMERA_SPEED = 600f;
    private static final float ZOOM_SPEED = 1.0f;
    private static final float MIN_ZOOM = 0.1f;
    private static final float MAX_ZOOM = 2.0f;
    
    // Selection system
    private Entity selectedEntity;
    
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
        
        // Selection system
        selectedEntity = null;
        
        // Create initial entities for testing
        setupInitialEntities();
    }
    
    private void setupInitialEntities() {
        // Create home base at bottom-left area
        Entity homeBase = new Entity(EntityType.BASE, 10, 10);
        entityManager.addEntity(homeBase);
        
        // Create cannon near base
        Entity cannon = new Cannon(EntityType.CANNON, 15, 15, 200);
        entityManager.addEntity(cannon);
        
        // Create some troops
        for (int i = 0; i < 5; i++) {
            Entity troop = new Entity(EntityType.TROOP, 12 + i, 12);
            entityManager.addEntity(troop);
        }
        
        // Create a supply truck
        Entity supplyTruck = new Entity(EntityType.SUPPLY_TRUCK, 8, 8);
        entityManager.addEntity(supplyTruck);
    }
    
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        
        // Handle keyboard input (camera movement)
        handleKeyboardInput(deltaTime);
                
        // Check for entity selection changes
        Entity newSelection = inputHandler.getSelectedEntity();
        if (newSelection != selectedEntity) {
            selectedEntity = newSelection;
            if (selectedEntity != null) {
                System.out.println("Selected: " + selectedEntity.getType() + 
                                 " at (" + selectedEntity.getGridX() + ", " + selectedEntity.getGridY() + ")");
            } else {
                System.out.println("Deselected entity");
            }
        }
        
        // Update camera
        camera.update();
        
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render game
        gameRenderer.render(entityManager.getEntities(), selectedEntity);
        
        // Debug info
        renderDebugInfo();
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
        
        camera.position.x = Math.max(halfWidth, Math.min(camera.position.x, 
            gameGrid.getWorldWidth() - halfWidth));
        camera.position.y = Math.max(halfHeight, Math.min(camera.position.y, 
            gameGrid.getWorldHeight() - halfHeight));
    }
    
    private void renderDebugInfo() {
        // This could be expanded with SpriteBatch for text rendering
        // For now, we'll keep it simple
    }
    
    @Override
    public void dispose() {
        gameRenderer.dispose();
        inputHandler.dispose();
    }
}