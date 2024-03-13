package com.softwarearchitecture.ecs.systems;

import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;

public class MovementSystem implements System {
    private ComponentManager<PositionComponent> positionManager;
    private ComponentManager<VelocityComponent> velocityManager;

    public MovementSystem(ComponentManager<PositionComponent> positionManager,
            ComponentManager<VelocityComponent> velocityManager) {
        this.positionManager = positionManager;
        this.velocityManager = velocityManager;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            if (positionManager.getComponent(entity) != null && velocityManager.getComponent(entity) != null) {
                // Logic to update entity's position based on its velocity
                PositionComponent pos = positionManager.getComponent(entity);
                VelocityComponent vel = velocityManager.getComponent(entity);
                pos.position.x += vel.velocity.x * deltaTime;
                pos.position.y += vel.velocity.y * deltaTime;
            }
        }
    }
}
