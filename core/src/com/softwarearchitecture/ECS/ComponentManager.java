package com.softwarearchitecture.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ComponentManager<T> {
    private Map<UUID, T> components = new HashMap<>();

    public void addComponent(Entity entity, T component) {
        components.put(entity.getId(), component);
    }

    public T getComponent(Entity entity) {
        return components.get(entity.getId());
    }

    public void removeComponent(Entity entity) {
        components.remove(entity.getId());
    }
}