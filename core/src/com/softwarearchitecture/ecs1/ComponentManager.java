package com.softwarearchitecture.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages components of a specific type for entities in ecs architecture.
 * The ComponentManager allows for the addition, retrieval, and removal of
 * components
 * associated with entities, using the entity's UUID as the key.
 *
 * @param <T> The type of component this manager is responsible for.
 */
public class ComponentManager<T> {
    private Map<UUID, T> components = new HashMap<>();

    /**
     * Adds a component to an entity. If the entity already has a component
     * of this type, it will be overwritten.
     *
     * @param entity    The entity to which the component will be added.
     * @param component The component to add to the entity.
     */
    public void addComponent(Entity entity, T component) {
        components.put(entity.getId(), component);
    }

    /**
     * Retrieves a component associated with a given entity. If the entity does not
     * have
     * a component of this type, {@code null} is returned.
     *
     * @param entity The entity whose component is to be retrieved.
     * @return The component associated with the entity, or {@code null} if the
     *         entity does not have
     *         a component of this type.
     */
    public T getComponent(Entity entity) {
        return components.get(entity.getId());
    }

    /**
     * Removes the component associated with a given entity. If the entity does not
     * have
     * a component of this type, the method does nothing.
     *
     * @param entity The entity whose component is to be removed.
     */
    public void removeComponent(Entity entity) {
        components.remove(entity.getId());
    }
}