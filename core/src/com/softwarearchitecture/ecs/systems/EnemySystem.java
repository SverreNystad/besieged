package com.softwarearchitecture.ecs.systems;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TileType;
import com.softwarearchitecture.math.Vector2;

/**
 * This class is supposed to check if enemies are at the end of the map, and if so despawns them
 * 
 */
public class EnemySystem implements System {
    private ComponentManager<PositionComponent> positionManager;
    private ComponentManager<PathfindingComponent> pathfindingManager;
    private ComponentManager<SpriteComponent> drawableManager;
    private ComponentManager<TileComponent> tileManager;
    private ComponentManager<HealthComponent> healthManager;


    public EnemySystem() {
        this.positionManager = ECSManager.getInstance().getOrDefaultComponentManager(PositionComponent.class);
        this.drawableManager = ECSManager.getInstance().getOrDefaultComponentManager(SpriteComponent.class);
        this.pathfindingManager = ECSManager.getInstance().getOrDefaultComponentManager(PathfindingComponent.class);
        this.tileManager = ECSManager.getInstance().getOrDefaultComponentManager(TileComponent.class);
        this.healthManager = ECSManager.getInstance().getOrDefaultComponentManager(HealthComponent.class);
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
        for (Entity entity : entities) {
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            Optional<PathfindingComponent> pathfinding = pathfindingManager.getComponent(entity);
            Optional<HealthComponent> health = healthManager.getComponent(entity);
            if (!position.isPresent() || !pathfinding.isPresent() || !health.isPresent()) {
                continue;
            }
            Vector2 pos = position.get().position;
            List<Tile> find = pathfinding.get().path;
            Tile nextTile = pathfinding.get().targetTile;
            Vector2 nextTilePos = new Vector2(nextTile.getX()*tileSize.x, nextTile.getY()*tileSize.y);
            if (nextTile.getType() == TileType.END && nextTilePos.sub(pos).len() < 0.001f){
                pathfinding.get().targetTile = find.get(0);
                position.get().position = new Vector2(find.get(0).getX()*tileSize.x,find.get(0).getY()*tileSize.y);
                health.get().setHealth(health.get().getMaxHealth());
            }

        }
    }

}
