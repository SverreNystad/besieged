package com.softwarearchitecture.ecs.systems;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.TileComponentManager;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TileType;
import com.softwarearchitecture.math.Vector2;

public class MovementSystem implements System {
    private ComponentManager<PositionComponent> positionManager;
    private ComponentManager<VelocityComponent> velocityManager;
    private ComponentManager<SpriteComponent> drawableManager;
    private ComponentManager<PathfindingComponent> pathfindingManager;
    private Viewport viewport;
    private TileComponentManager tileManager;

    public MovementSystem(ComponentManager<PositionComponent> positionManager,
                          ComponentManager<VelocityComponent> velocityManager,
                          ComponentManager<SpriteComponent> drawableManager,
                          ComponentManager<PathfindingComponent> pathfindingManager,
                          Viewport viewport, TileComponentManager tileManager) {
        this.positionManager = positionManager;
        this.velocityManager = velocityManager;
        this.drawableManager = drawableManager;
        this.pathfindingManager = pathfindingManager;
        this.viewport = viewport;
        this.tileManager = tileManager;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            Optional<VelocityComponent> velocity = velocityManager.getComponent(entity);
            Optional<PathfindingComponent> pathfinding = pathfindingManager.getComponent(entity);

            if (position.isPresent() && velocity.isPresent() && pathfinding.isPresent()) {
                PositionComponent pos = position.get();
                VelocityComponent vel = velocity.get();
                PathfindingComponent find = pathfinding.get();

                if (find.getPath() == null || find.getPath().isEmpty()) {
                    find.setPath(findPath(pos.position, tileManager));  // Start pathfinding
                } else {
                    Vector2 nextWaypoint = find.getPath().peek();
                    if (moveTowards(pos.position, nextWaypoint, vel.velocity, deltaTime)) {
                        find.getPath().poll();  // Move to next waypoint and remove it from the path
                    }
                }
            }
        }
    }

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

    private Queue<Vector2> findPath(Vector2 start, TileComponentManager tileManager) {
        Vector2 startTilePos = toTileCoordinates(start);
        List<Vector2> potentialTargets = getNeighbors(startTilePos, tileManager);

        for (Vector2 target : potentialTargets) {
            Tile tile = tileManager.getTileAt((int) target.x, (int) target.y);
            if (tile != null && (tile.getType() == TileType.PATH || tile.getType() == TileType.END)) {
                Queue<Vector2> path = new LinkedList<>();
                path.add(target); // Use real world coordinates, convert if necessary
                return path;
            }
        }
        return new LinkedList<>();
    }

    private Vector2 toTileCoordinates(Vector2 position) {
        // Conversion logic to tile coordinates
        return new Vector2((float) Math.floor(position.x), (float) Math.floor( position.y));
    }

    private List<Vector2> getNeighbors(Vector2 tilePosition, TileComponentManager tileManager) {
        List<Vector2> neighbors = new ArrayList<>();
        // Example neighbors; modify as needed
        neighbors.add(new Vector2(tilePosition.x + 1, tilePosition.y));
        neighbors.add(new Vector2(tilePosition.x - 1, tilePosition.y));
        neighbors.add(new Vector2(tilePosition.x, tilePosition.y + 1));
        neighbors.add(new Vector2(tilePosition.x, tilePosition.y - 1));
        return neighbors;
    }



    private void updateDrawable(SpriteComponent drawable, PositionComponent pos) {
        float screen_width = viewport.getWorldWidth();
        float screen_height = viewport.getWorldHeight();

        // Example conversion - assume game world dimensions match screen dimensions
        float drawableWidthInWorld = 1.0f; // Fixed max-width of the entity in the world
        float drawableHeightInWorld = 1.0f; // Fixed max-height of the entity in the world

        throw new UnsupportedOperationException("Not implemented yet! Only the position needs to be updated. Not the sprite.");
        // // Conversion from world position to screen position
        // drawable.screen_u = convertWorldToScreenX(pos.position.x, screen_width);
        // drawable.screen_v = convertWorldToScreenY(pos.position.y, screen_height);

        // // Conversion from world size to screen size
        // drawable.u_size = convertWorldToScreenSize(drawableWidthInWorld, screen_width);
        // drawable.v_size = convertWorldToScreenSize(drawableHeightInWorld, screen_height);
    }

    // Convert world X-coordinate to screen U coordinate
    private float convertWorldToScreenX(float worldX, float screenWidth) {
        return worldX / screenWidth;
    }

    // Convert world Y-coordinate to screen V coordinate
    private float convertWorldToScreenY(float worldY, float screenHeight) {
        return worldY / screenHeight;
    }

    // Convert world size to screen size (assuming uniform scaling)
    private float convertWorldToScreenSize(float worldSize, float screenSize) {
        return worldSize / screenSize;
    }
}
