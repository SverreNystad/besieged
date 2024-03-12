package com.softwarearchitecture.ECS;

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
 * For more information on the ECS pattern, see <a href="https://en.wikipedia.org/wiki/Entity_component_system">Entity Component System</a>.
 * <p>
 * For more information on the Singleton design pattern, see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">Singleton Pattern</a>.
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

    /**  Public method to get the singleton instance */
    public static synchronized ECSManager getInstance() {
        if (instance == null) {
            instance = new ECSManager();
        }
        return instance;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public <T> void addComponentManager(Class<T> componentType, ComponentManager<T> manager) {
        componentManagers.put(componentType, manager);
    }

    public <T> ComponentManager<T> getComponentManager(Class<T> componentType) {
        // Cast is safe due to the controlled way ComponentManagers are added
        @SuppressWarnings("unchecked")
        ComponentManager<T> manager = (ComponentManager<T>) componentManagers.get(componentType);
        return manager;
    }

    public void addSystem(System system) {
        systems.add(system);
    }

    public Set<System> getSystems() {
        return systems;
    }

    public void update(float deltaTime) {
        for (System system : systems) {
            system.update(entities, deltaTime);
        }
    }
}