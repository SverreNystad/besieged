package com.softwarearchitecture.ecs;

import java.util.UUID;

public class Entity {
    private UUID id;
    private ECSManager ecs;

    public Entity() {
        this.id = UUID.randomUUID();
        this.ecs = ECSManager.getInstance();
    }

    public UUID getId() {
        return id;
    }

    public <T> void addComponent(Class<T> componentType, T component) {
        ComponentManager<T> manager = ecs.getComponentManager(componentType);
        if (manager != null) {
            manager.addComponent(this, component);
        }
    }
}