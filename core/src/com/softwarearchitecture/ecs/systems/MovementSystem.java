package com.softwarearchitecture.ecs.systems;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.math.Vector2;

public class MovementSystem implements System {
    private ComponentManager<PositionComponent> positionManager;
    private ComponentManager<VelocityComponent> velocityManager;
    private ComponentManager<SpriteComponent> drawableManager;
    private ComponentManager<PathfindingComponent> pathfindingManager;
    private ComponentManager<TileComponent> tileManager;

    public MovementSystem() {
        this.positionManager = ECSManager.getInstance().getOrDefaultComponentManager(PositionComponent.class);
        this.velocityManager = ECSManager.getInstance().getOrDefaultComponentManager(VelocityComponent.class);
        this.drawableManager = ECSManager.getInstance().getOrDefaultComponentManager(SpriteComponent.class);
        this.pathfindingManager = ECSManager.getInstance().getOrDefaultComponentManager(PathfindingComponent.class);
        this.tileManager = ECSManager.getInstance().getOrDefaultComponentManager(TileComponent.class);

    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        
        //Get tile size
        Vector2 tileSize = new Vector2(0,0);
        for (Entity entity : entities) {
            Optional<SpriteComponent> sprite = drawableManager.getComponent(entity);
            Optional<TileComponent> tile = tileManager.getComponent(entity);
            if (sprite.isPresent() && tile.isPresent()) {
                tileSize = sprite.get().size_uv;
                break;
            }   
        }
        //Move enemies towards next waypoint
        for (Entity entity : entities) {
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            Optional<VelocityComponent> velocity = velocityManager.getComponent(entity);
            Optional<PathfindingComponent> pathfinding = pathfindingManager.getComponent(entity);
            if (!position.isPresent() || !velocity.isPresent() || !pathfinding.isPresent()) {
                continue;
            }
            Vector2 pos = position.get().position;
            Vector2 vel = velocity.get().velocity;
            List<Tile> find = pathfinding.get().path;
            java.lang.System.out.println(pos);

            Tile nextTile = find.get(0);
            Vector2 nextWaypoint = new Vector2(nextTile.getX()*tileSize.x, nextTile.getY()*tileSize.y);
            if (moveTowards(pos, nextWaypoint, vel, deltaTime)) {
                find.remove(0);  // Move to next waypoint and remove it from the path
            }

        }
    }
    /**
     * Changes currentPosition by velocity or sets current position as target position if target is reached
     * @param currentPosition
     * @param targetPosition
     * @param velocity
     * @param deltaTime
     * @return
     */
    private boolean moveTowards(Vector2 currentPosition, Vector2 targetPosition, Vector2 velocity, float deltaTime) {

        Vector2 direction = targetPosition.cpy().sub(currentPosition).nor();
        float distance = currentPosition.dst(targetPosition);
        float stepLength = velocity.len() * deltaTime;
        if (distance > stepLength) {
            currentPosition.add(direction.scl(stepLength));
            return false;  // Target not reached
        } else {
            currentPosition.set(targetPosition);
            return true;  // Target reached
        }
    }


}
