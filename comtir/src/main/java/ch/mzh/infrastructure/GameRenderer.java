package ch.mzh.infrastructure;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import ch.mzh.game.Observer;
import ch.mzh.input.InputHandler;
import ch.mzh.model.Entity;
import ch.mzh.model.TerrainType;

public class GameRenderer implements Observer {
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private GameGrid gameGrid;

    public GameRenderer(OrthographicCamera camera, GameGrid gameGrid) {
        this.camera = camera;
        this.gameGrid = gameGrid;
        this.shapeRenderer = new ShapeRenderer();
    }
    
    public void render(Array<Entity> entities) {
        render(entities, null);
    }
    
    public void render(Array<Entity> entities, Entity selectedEntity) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        // Render grid and terrain
        renderGrid();
        
        // Render entities
        renderEntities(entities, selectedEntity);
        
        // Render selection indicator if there's a selected entity
        if (selectedEntity != null) {
            renderSelectionIndicator(selectedEntity);
        }
    }
    
    private void renderGrid() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Calculate visible grid bounds for optimization
        Vector2 bottomLeft = gameGrid.worldToGrid(camera.position.x - camera.viewportWidth * camera.zoom / 2,
                                                 camera.position.y - camera.viewportHeight * camera.zoom / 2);
        Vector2 topRight = gameGrid.worldToGrid(camera.position.x + camera.viewportWidth * camera.zoom / 2,
                                               camera.position.y + camera.viewportHeight * camera.zoom / 2);
        
        int startX = Math.max(0, (int)bottomLeft.x - 1);
        int endX = Math.min(gameGrid.getWidth(), (int)topRight.x + 1);
        int startY = Math.max(0, (int)bottomLeft.y - 1);
        int endY = Math.min(gameGrid.getHeight(), (int)topRight.y + 1);
        
        // Render terrain tiles
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                TerrainType terrain = gameGrid.getTerrainAt(x, y);
                Vector2 worldPos = gameGrid.gridToWorld(x, y);
                
                // Set color based on terrain type
                switch (terrain) {
                    case OPEN_GROUND:
                        shapeRenderer.setColor(0.4f, 0.6f, 0.3f, 1.0f); // Green
                        break;
                    case ROUGH_TERRAIN:
                        shapeRenderer.setColor(0.6f, 0.5f, 0.3f, 1.0f); // Brown
                        break;
                    case OBSTACLE:
                        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1.0f); // Gray
                        break;
                }
                
                shapeRenderer.rect(worldPos.x, worldPos.y, 
                                 gameGrid.getTileSize() - 1, gameGrid.getTileSize() - 1);
            }
        }
        
        shapeRenderer.end();
        
        // Render grid lines
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.5f);
        
        // Only render grid lines when zoomed in enough
        if (camera.zoom < 1.0f) {
            // Vertical lines
            for (int x = startX; x <= endX; x++) {
                float worldX = x * gameGrid.getTileSize();
                shapeRenderer.line(worldX, startY * gameGrid.getTileSize(),
                                 worldX, endY * gameGrid.getTileSize());
            }
            
            // Horizontal lines
            for (int y = startY; y <= endY; y++) {
                float worldY = y * gameGrid.getTileSize();
                shapeRenderer.line(startX * gameGrid.getTileSize(), worldY,
                                 endX * gameGrid.getTileSize(), worldY);
            }
        }
        
        shapeRenderer.end();
    }
    
    private void renderEntities(Array<Entity> entities, Entity selectedEntity) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        for (Entity entity : entities) {
            if (!entity.isActive()) continue;
            
            Vector2 worldPos = gameGrid.gridToWorld(entity.getGridX(), entity.getGridY());
            boolean isSelected = (entity == selectedEntity);
            
            // Set color and size based on entity type
            switch (entity.getType()) {
                case CANNON:
                    if (isSelected) {
                        shapeRenderer.setColor(0.3f, 0.3f, 1.0f, 1.0f); // Brighter blue when selected
                    } else {
                        shapeRenderer.setColor(0.1f, 0.1f, 0.8f, 1.0f); // Blue
                    }
                    shapeRenderer.rect(worldPos.x + 4, worldPos.y + 4, 24, 24);
                    break;
                case TROOP:
                    if (isSelected) {
                        shapeRenderer.setColor(0.3f, 1.0f, 0.3f, 1.0f); // Brighter green when selected
                    } else {
                        shapeRenderer.setColor(0.1f, 0.8f, 0.1f, 1.0f); // Green
                    }
                    shapeRenderer.circle(worldPos.x + 16, worldPos.y + 16, 8);
                    break;
                case BASE:
                    if (isSelected) {
                        shapeRenderer.setColor(1.0f, 1.0f, 0.3f, 1.0f); // Brighter yellow when selected
                    } else {
                        shapeRenderer.setColor(0.8f, 0.8f, 0.1f, 1.0f); // Yellow
                    }
                    shapeRenderer.rect(worldPos.x + 2, worldPos.y + 2, 28, 28);
                    break;
                case SUPPLY_TRUCK:
                    if (isSelected) {
                        shapeRenderer.setColor(1.0f, 0.6f, 0.3f, 1.0f); // Brighter orange when selected
                    } else {
                        shapeRenderer.setColor(0.8f, 0.4f, 0.1f, 1.0f); // Orange
                    }
                    shapeRenderer.rect(worldPos.x + 6, worldPos.y + 6, 20, 20);
                    break;
            }
        }
        
        shapeRenderer.end();
    }

    private void renderSelectionIndicator(Entity selectedEntity) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f); // White selection border
        
        Vector2 worldPos = gameGrid.gridToWorld(selectedEntity.getGridX(), selectedEntity.getGridY());
        
        // Draw selection border around the tile
        shapeRenderer.rect(worldPos.x, worldPos.y, gameGrid.getTileSize(), gameGrid.getTileSize());
        
        // Draw corner markers for better visibility
        int cornerSize = 6;
        int tileSize = gameGrid.getTileSize();
        
        // Top-left corner
        shapeRenderer.line(worldPos.x, worldPos.y + tileSize, worldPos.x + cornerSize, worldPos.y + tileSize);
        shapeRenderer.line(worldPos.x, worldPos.y + tileSize, worldPos.x, worldPos.y + tileSize - cornerSize);
        
        // Top-right corner
        shapeRenderer.line(worldPos.x + tileSize - cornerSize, worldPos.y + tileSize, worldPos.x + tileSize, worldPos.y + tileSize);
        shapeRenderer.line(worldPos.x + tileSize, worldPos.y + tileSize, worldPos.x + tileSize, worldPos.y + tileSize - cornerSize);
        
        // Bottom-left corner
        shapeRenderer.line(worldPos.x, worldPos.y, worldPos.x + cornerSize, worldPos.y);
        shapeRenderer.line(worldPos.x, worldPos.y, worldPos.x, worldPos.y + cornerSize);
        
        // Bottom-right corner
        shapeRenderer.line(worldPos.x + tileSize - cornerSize, worldPos.y, worldPos.x + tileSize, worldPos.y);
        shapeRenderer.line(worldPos.x + tileSize, worldPos.y, worldPos.x + tileSize, worldPos.y + cornerSize);
        
        shapeRenderer.end();
    }

    @Override
    public void onEntitySelected(Entity entity) {
        // Store the selected entity to render the selection indicator in the next frame
        System.out.println("Entity selected for rendering: " + entity.getType());
    }

    @Override
    public void onEntityDeselected() {
        // Clear the selected entity so the selection indicator disappears
        System.out.println("Entity deselected");
    }
}