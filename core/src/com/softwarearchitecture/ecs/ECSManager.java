package com.softwarearchitecture.ecs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The {@code ECSManager} class serves as the central hub for managing the
 * Entity-Component-System (ECS) architecture in Besieged.
 * It facilitates the handling of entities, components, and systems to
 * enable a decoupled and flexible game or application architecture.
 * This class follows the Singleton design pattern to ensure only one instance
 * exists throughout the application lifecycle.
 * <p>
 * For more information on the ECS pattern, see
 * <a href="https://en.wikipedia.org/wiki/Entity_component_system">Entity
 * Component System</a>.
 * <p>
 * For more information on the Singleton design pattern, see
 * <a href="https://en.wikipedia.org/wiki/Singleton_pattern">Singleton
 * Pattern</a>.
 */
public class ECSManager {
    
    /** Singleton */
    private static ThreadLocal<ECSManager> instance;

    /** Stores the entities */
    private Set<Entity> entities;
    private Set<Entity> toAdd; // Entities to be added before the next update

    /** Stores the systems */
    private Set<System> systems;

    /** Stores component managers for different component types */
    private Map<Class<?>, ComponentManager<?>> componentManagers;

    private TileComponentManager tileComponentManager;

    // Private constructor to prevent instantiation
    private ECSManager() {
        entities = new HashSet<>();
        toAdd = new HashSet<>();
        systems = new HashSet<>();
        componentManagers = new HashMap<>();
        tileComponentManager = new TileComponentManager();
    }

    /**
     * @return The singleton instance of the ECSManager.
     */
    public static synchronized ECSManager getInstance() {
        if (instance == null) {
            instance = ThreadLocal.withInitial(ECSManager::new);
        }
        return instance.get();
    }

    /**
     * Adds an entity to the ECSManager.
     * 
     * @param entity The entity to be added.
     */
    public void addEntity(Entity entity) {
        toAdd.add(entity);
    }

    /**
     * @return The set of entities managed by the ECSManager.
     */
    public Set<Entity> getEntities() {
        return entities;
    }

    /**
     * Clears all entities from the ECSManager.
     */
    public void clearEntities() {
        entities.clear();
    }

    /**
     * Returns the component manager for the specified component type.
     * 
     * @param componentType The class representing the component type.
     * @param <T>           The type of the component.
     * @return The component manager for the specified component type.
     */
    @SuppressWarnings("unchecked")
    public <T> ComponentManager<T> getOrDefaultComponentManager(Class<T> componentType) throws IllegalStateException {
        // Cast is safe due to the controlled way ComponentManagers are added
        ComponentManager<T> manager = (ComponentManager<T>) componentManagers.get(componentType);
        if (manager == null) {
            manager = new ComponentManager<T>();
            componentManagers.put(componentType, manager);
        }
        return manager;
    }

    public TileComponentManager getTileComponentManager() {
        return tileComponentManager;  // Provide direct access to TileComponentManager
    }

    /**
     * Adds a system to the ECSManager.
     * 
     * @param system The system to be added.
     */
    public void addSystem(System system) {
        systems.add(system);
    }

    /**
     * Returns the set of systems managed by the ECSManager.
     * 
     * @return A set of systems.
     */
    public Set<System> getSystems() {
        return systems;
    }

    /**
     * Updates all systems managed by the ECSManager.
     * 
     * @param deltaTime The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        for (Entity entity : toAdd) {
            entities.add(entity);
        }

        for (System system : this.systems) {
            // Update each system
            system.update(this.entities, deltaTime); // TODO: Render function should be the last system called!
        }
    }

    /**
     * Clears all entities, systems, and component managers from the ECSManager.
     */
    public void clearAll() {
        entities.clear();
        systems.clear();
        componentManagers.clear();
    }
}