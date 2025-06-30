package ch.mzh.model;

import ch.mzh.components.Component;
import ch.mzh.components.MovementComponent;
import ch.mzh.infrastructure.Position2D;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {
    private String name;
    private EntityType type;
    private Position2D position;

    private Vector2 worldPosition;
    private boolean active;
    private boolean selectable;

    protected Map<Class<? extends Component>, Component> components;

    public Entity() {}
    
    public Entity(String name, EntityType type, Position2D position) {
        this.name = name;
        this.type = type;
        this.components = new HashMap<>();
        this.position = position;
        this.worldPosition = new Vector2();
        this.active = true;
        this.selectable = true; // Most entities are selectable by default
        updateWorldPosition();
    }

    public void setGridPosition(Position2D position) {
        this.position = position;
        updateWorldPosition();
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentType) {
        return (T) components.get(componentType);
    }

    public <T extends Component> boolean hasComponent(Class<T> componentType) {
        return components.containsKey(componentType);
    }

    public <T extends Component> void removeComponent(Class<T> componentType) {
        components.remove(componentType);
    }

    private void updateWorldPosition() {
        // This will be updated when we have access to the grid, for now, assume 32px tiles
        worldPosition.set(position.getX() * 32, position.getY() * 32);
    }
    
    // Getters
    public String getName() { return this.name; }
    public EntityType getType() { return type; }
    public Position2D getPosition() { return position; }
    public Vector2 getWorldPosition() { return worldPosition; }
    public boolean isActive() { return active; }
    public boolean isSelectable() { return selectable; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setActive(boolean active) { this.active = active; }
    public void setSelectable(boolean selectable) { this.selectable = selectable; }
}
