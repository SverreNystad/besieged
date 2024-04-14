package com.softwarearchitecture.ecs.systems;


import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;

/**
 * This class is supposed to check if enemies are at the end of the map, and if so despawns them
 * 
 */
public class EnemySystem implements System {
    private ComponentManager<PositionComponent> positionManager;
    private ComponentManager<PathfindingComponent> pathfindingManager;

    public EnemySystem(ComponentManager<PositionComponent> positionManager,
                          ComponentManager<PathfindingComponent> pathfindingManager) {
        this.positionManager = positionManager;
        this.pathfindingManager = pathfindingManager;
    }
    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            Optional<PathfindingComponent> pathfinding = pathfindingManager.getComponent(entity);

        }
    }

}
