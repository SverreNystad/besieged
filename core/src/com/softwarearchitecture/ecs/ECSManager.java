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
    private Set<Entity> localEntities;
    private Set<Entity> remoteEntities;
    private Set<Entity> toAdd; // Entities to be added before the next update
    private Set<Entity> toRemove; // Entities to be removed before the next update
    private Set<Entity> newlyRemoteAddedEntities; // Entities that were added this frame
    /** Stores the systems */
    private Set<System> systems;

    /** Stores component managers for different component types */
    private Map<Class<?>, ComponentManager<?>> componentManagers;


    // Private constructor to prevent instantiation
    private ECSManager() {
        localEntities = new HashSet<>();
        newlyRemoteAddedEntities = new HashSet();
        remoteEntities = new HashSet<>();
        toAdd = new HashSet<>();
        toRemove = new HashSet<>();
        systems = new HashSet<>();
        componentManagers = new HashMap<>();

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
    public void addLocalEntity(Entity entity) {
        toAdd.add(entity);
    }

    /**
     * @return The set of entities managed by the ECSManager.
     */
    public Set<Entity> getLocalEntities() {
        return localEntities;
    }

    /**
     * Removes an entity from the ECSManager.
     */
    public void removeLocalEntity(Entity entity) {
        toRemove.add(entity);
    }

    /**
     * Adds a external entity to the ECSManager. These would be entities from a server for example.
     * @param entity The entity to be added.
     */
    public void addRemoteEntity(Entity entity) {
        boolean result = remoteEntities.add(entity);
        if (result) {
            newlyRemoteAddedEntities.add(entity);
        }
    }

    public Set<Entity> getAndClearNewlyRemoteAddedEntities() {
        Set<Entity> result = new HashSet<>(newlyRemoteAddedEntities);
        newlyRemoteAddedEntities.clear();
        return result;
    }

    /**
     * Get all external entities.
     */
    public Set<Entity> getRemoteEntities() {
        return remoteEntities;
    }

    /**
     * Remove a external entity from the ECSManager.
     * @param entity The entity to be removed.
     */
    public void removeRemoteEntity(Entity entity) {
        remoteEntities.remove(entity);
    }

    /**
     * Clears all entities from the ECSManager.
     */
    public void clearLocalEntities() {
        localEntities.clear();
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
            localEntities.add(entity);
        }

        for (Entity entity : toRemove) {
            localEntities.remove(entity);
        }

        Set<Entity> entities = new HashSet<>(localEntities);
        entities.addAll(remoteEntities);
        for (System system : this.systems) {
            // Update each system
            system.update(entities, deltaTime); 
        }
    }

    /**
     * Clears all entities, systems, and component managers from the ECSManager.
     */
    public void clearAll() {
        localEntities.clear();
        systems.clear();
        componentManagers.clear();
        remoteEntities.clear();
    }
}