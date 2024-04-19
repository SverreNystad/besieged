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
            float vel = velocity.get().velocity;
            List<Tile> find = pathfinding.get().path;
            Tile nextTile = pathfinding.get().targetTile;
            int currentIndex = find.indexOf(nextTile);
            Vector2 nextWaypoint = new Vector2(nextTile.getX()*tileSize.x, nextTile.getY()*tileSize.y+tileSize.y/4);
            float remainingStepSize = moveTowards(pos, nextWaypoint, vel * deltaTime);
            while (remainingStepSize != -1f && find.size() > currentIndex+1) {
                pathfinding.get().targetTile = find.get(currentIndex+1);  // Move to next waypoint and remove it from the path
                nextTile = pathfinding.get().targetTile;
                currentIndex = find.indexOf(nextTile);
                nextWaypoint = new Vector2(nextTile.getX()*tileSize.x, nextTile.getY()*tileSize.y+tileSize.y/4);
                remainingStepSize = moveTowards(pos, nextWaypoint, remainingStepSize);
            }
        }
    }
    /**
     * Changes currentPosition by velocity or sets current position as target position if target is reached
     * @param currentPosition
     * @param targetPosition
     * @param stepLength
     * @param deltaTime
     * @return Remaining distance to target. {@code -1} if target not reached.
     */
    private float moveTowards(Vector2 currentPosition, Vector2 targetPosition, float stepLength) {

        Vector2 direction = targetPosition.cpy().sub(currentPosition).nor();
        float distance = currentPosition.dst(targetPosition);
        if (distance > stepLength) {
            currentPosition.add(direction.scl(stepLength));
            return -1f;  // Target not reached
        } else {
            currentPosition.set(targetPosition);
            return stepLength - distance;  // Target reached
        }
    }




}
