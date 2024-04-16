package com.softwarearchitecture.ecs.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.math.Vector2;


/**
 * 
 * Class responsible for handling attacks of Towers on enemies.
 * 
 * <p>
 * @param entities All entities in the game
 * @param deltaTime The time between frames
 * </p>
 * 
 * Loops through all tower-entities. If there are enemies within the range of the tower, it
 * decrements their health-component by the towers' damage-component.
 * 
 */
public class AttackSystem implements System {

    private final Map gameMap;

    public AttackSystem(Map gameMap) {
        this.gameMap = gameMap;
    }

    private Vector2 convertRangeToUVDistance(float range) {
        // Get the width and height of each tile in UV-coordinates
        float tileWidth = gameMap.getTileWidth();
        float tileHeight = gameMap.getTileHeight();
        
        // Calculate UV-distance for the given range
        float uvDistanceX = range * tileWidth;
        float uvDistanceY = range * tileHeight;
        
        // Construct and return the UV-distance vector
        return new Vector2(uvDistanceX, uvDistanceY);
    }
    
    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        // Add all Tower-entities that can attack to a list
        List<Entity> towers = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getComponent(TowerComponent.class).isPresent()) {
                TowerComponent towerComp = entity.getComponent(TowerComponent.class).get();
                towerComp.updateTimeSinceLastAttack(deltaTime);
                if (towerComp.canAttack()) {
                    towers.add(entity);
                }
            }
        }

        // Add all Enemy-entities to a list
        List<Entity> enemies = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getComponent(EnemyComponent.class).isPresent()) {
                enemies.add(entity);
            }
        }

        // Loop through all towers, and check if any enemies are within its range. If they are, attack. 
        for (Entity tower : towers) {
            TowerComponent towerComponent = tower.getComponent(TowerComponent.class).get();
            float range = tower.getComponent(TowerComponent.class).get().getRange();
            int damage = tower.getComponent(TowerComponent.class).get().getDamage();

            // Convert range to UV-distance
            Vector2 uvDistance = convertRangeToUVDistance(range);    

            for (Entity enemy : enemies) {
                Vector2 towerPosition = tower.getComponent(PositionComponent.class).get().getPosition();
                
                Vector2 enemyPosition = enemy.getComponent(PositionComponent.class).get().getPosition();
                float distance = Vector2.dst(towerPosition, enemyPosition);

                if (distance <= uvDistance.len()) {
                    int enemyHealth = enemy.getComponent(HealthComponent.class).get().getHealth();
                    enemyHealth -= damage;
                    enemy.getComponent(HealthComponent.class).get().setHealth(enemyHealth);
                    towerComponent.resetAttackTimer();
                    java.lang.System.out.println("Tower " + tower + " attacked enemy " + enemy);
                    java.lang.System.out.println("Enemy now has " + enemyHealth + " health left");
                } 
            }
        }
    }
    
}
