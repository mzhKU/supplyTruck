package ch.mzh.infrastructure;

import ch.mzh.model.BaseRefuelPosition;
import com.badlogic.gdx.math.Vector2;

import ch.mzh.model.TerrainType;

import java.util.Arrays;
import java.util.List;

import static ch.mzh.utilities.Distance.calculateManhattanDistance;

public class GameGrid {
    private final int width;
    private final int height;
    private final int tileSize;
    private final TerrainType[][] terrain;
    private Position2D[] gridPositions;
    private List<BaseRefuelPosition> refuelGridPositions;
    
    public GameGrid(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.terrain = new TerrainType[width][height];

        initializeTerrain();
        initializeGridPositions();
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

    private void initializeGridPositions() {
        gridPositions = new Position2D[width*height];
        int j = 0;
        for(int i = 0; i < width*height; i++) {
            int posX = i % width;
            gridPositions[i] = new Position2D(posX, j);
            if (posX == width - 1) {
                j++;
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

    public List<Position2D> getPositionsWithinDistance(Position2D position, int distance) {
        return Arrays.stream(gridPositions)
                .filter(p -> calculateManhattanDistance(p, position) == distance)
                .toList();
    }

    public void setRefuelGridPositions(List<BaseRefuelPosition> positions) {
        this.refuelGridPositions = positions;
    }

    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getTileSize() { return tileSize; }
    public float getWorldWidth() { return width * tileSize; }
    public float getWorldHeight() { return height * tileSize; }
    public List<BaseRefuelPosition> getRefuelGridPositions() { return refuelGridPositions; }
}