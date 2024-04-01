package com.softwarearchitecture.testecs;

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
    private static ECSManager instance;

    /** Stores the entities */
    private Set<Entity> entities;

    /** Stores the systems */
    private Set<System> systems;

    /** Stores component managers for different component types */
    private Map<Class<?>, ComponentManager<?>> componentManagers;

    // Private constructor to prevent instantiation
    private ECSManager() {
        entities = new HashSet<>();
        systems = new HashSet<>();
        componentManagers = new HashMap<>();
    }

    /**
     * @return The singleton instance of the ECSManager.
     */
    public static synchronized ECSManager getInstance() {
        if (instance == null) {
            instance = new ECSManager();
        }
        return instance;
    }

    /**
     * Adds an entity to the ECSManager.
     * 
     * @param entity The entity to be added.
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
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
     * Adds a component manager for a specific component type to the ECSManager.
     * 
     * @param componentType The class representing the component type.
     * @param manager       The component manager for the specified component type.
     * @param <T>           The type of the component.
     */
    public <T> void addComponentManager(Class<T> componentType, ComponentManager<T> manager) {
        componentManagers.put(componentType, manager);
    }

    /**
     * Returns the component manager for a specific component type.
     * 
     * @param componentType The class representing the component type.
     * @param <T>           The type of the component.
     * @return The component manager for the specified component type.
     */
    public <T> ComponentManager<T> getComponentManager(Class<T> componentType) {
        // Cast is safe due to the controlled way ComponentManagers are added
        @SuppressWarnings("unchecked")
        ComponentManager<T> manager = (ComponentManager<T>) componentManagers.get(componentType);
        return manager;
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
        for (System system : systems) {
            // Update each system
            system.update(entities, deltaTime);
        }
    }
}