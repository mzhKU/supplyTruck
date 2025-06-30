package ch.mzh.infrastructure;

import com.badlogic.gdx.math.Vector2;

import ch.mzh.model.TerrainType;

public class GameGrid {
    private final int width;
    private final int height;
    private final int tileSize;
    private final TerrainType[][] terrain;
    
    public GameGrid(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.terrain = new TerrainType[width][height];
        
        // Initialize terrain (for now, mostly open ground with some obstacles)
        initializeTerrain();
    }
    
    private void initializeTerrain() {
        // Fill with open ground
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                terrain[x][y] = TerrainType.OPEN_GROUND;
            }
        }
        
        // Add some rough terrain patches
        for (int i = 0; i < width*100-20; i++) {
            int x = (int)(Math.random() * width);
            int y = (int)(Math.random() * height);
            if (x > 20 && x < width - 20 && y > 20 && y < height - 20) {
                terrain[x][y] = TerrainType.ROUGH_TERRAIN;
            }
        }
        
        // Add some obstacles
        for (int i = 0; i < width*100-40; i++) {
            int x = (int)(Math.random() * width);
            int y = (int)(Math.random() * height);
            if (x > 20 && x < width - 20 && y > 20 && y < height - 20) {
                terrain[x][y] = TerrainType.OBSTACLE;
            }
        }
    }
    
    public boolean isInvalidPosition(Position2D position) {
        return position.getX() < 0 || position.getX() >= width || position.getY() < 0 || position.getY() >= height;
    }
    
    public TerrainType getTerrainAt(Position2D position) {
        if (isInvalidPosition(position)) return TerrainType.OBSTACLE;
        return terrain[position.getX()][position.getY()];
    }
    
    public Vector2 gridToWorld(int gridX, int gridY) {
        return new Vector2(gridX * tileSize, gridY * tileSize);
    }
    
    public Vector2 worldToGrid(float worldX, float worldY) {
        return new Vector2((int)(worldX / tileSize), (int)(worldY / tileSize));
    }
    
    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getTileSize() { return tileSize; }
    public float getWorldWidth() { return width * tileSize; }
    public float getWorldHeight() { return height * tileSize; }
}