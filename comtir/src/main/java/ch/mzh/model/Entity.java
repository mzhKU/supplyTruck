package ch.mzh.model;

import com.badlogic.gdx.math.Vector2;

public class Entity {
    private EntityType type;
    private int gridX, gridY;
    private Vector2 worldPosition;
    private boolean active;
    private boolean selectable;

    public Entity() {}
    
    public Entity(EntityType type, int gridX, int gridY) {
        this.type = type;
        this.gridX = gridX;
        this.gridY = gridY;
        this.worldPosition = new Vector2();
        this.active = true;
        this.selectable = true; // Most entities are selectable by default
        updateWorldPosition();
    }
    
    public void setGridPosition(int x, int y) {
        this.gridX = x;
        this.gridY = y;
        updateWorldPosition();
    }
    
    private void updateWorldPosition() {
        // This will be updated when we have access to the grid
        // For now, assume 32px tiles
        worldPosition.set(gridX * 32, gridY * 32);
    }
    
    // Getters
    public EntityType getType() { return type; }
    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public Vector2 getWorldPosition() { return worldPosition; }
    public boolean isActive() { return active; }
    public boolean isSelectable() { return selectable; }
    
    // Setters
    public void setActive(boolean active) { this.active = active; }
    public void setSelectable(boolean selectable) { this.selectable = selectable; }
}
