package com.softwarearchitecture.ecs.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.AreaOfAffectComponent;
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
    private HashMap<Entity, Entity> activeAttacks = new HashMap<>(); // Map to track which enemy each tower is currently attacking

    public AttackSystem(Map gameMap) {
        this.gameMap = gameMap;
    }

    private Vector2 convertRangeToUVDistance(float range) {
        float tileWidth = gameMap.getTileWidth();
        float tileHeight = gameMap.getTileHeight();
        return new Vector2(range * tileWidth, range * tileHeight);
    }
    
    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        List<Entity> towers = new ArrayList<>();
        List<Entity> enemies = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity.getComponent(TowerComponent.class).isPresent()) {
                TowerComponent towerComp = entity.getComponent(TowerComponent.class).get();
                towerComp.updateTimeSinceLastAttack(deltaTime);
                if (towerComp.canAttack()) {
                    towers.add(entity);
                }
            }
            if (entity.getComponent(EnemyComponent.class).isPresent()) {
                enemies.add(entity);
            }
        }

        for (Entity tower : towers) {
            TowerComponent towerComponent = tower.getComponent(TowerComponent.class).get();
            Vector2 towerPosition = tower.getComponent(PositionComponent.class).get().position;
            float range = towerComponent.getRange();
            int damage = towerComponent.getDamage();
            Vector2 uvDistance = convertRangeToUVDistance(range);

            Optional<AreaOfAffectComponent> areaOfAffectComponent = tower.getComponent(AreaOfAffectComponent.class);
            if (areaOfAffectComponent.isPresent()) {
                // If the tower has an area of affect, attack all enemies within the range
                for (Entity enemy : enemies) {
                    Vector2 enemyPosition = enemy.getComponent(PositionComponent.class).get().position;
                    float distance = Vector2.dst(towerPosition, enemyPosition);
                    if (distance <= uvDistance.len()) {
                        attackEnemy(enemy, damage);
                    }
                }
                towerComponent.resetAttackTimer();
                continue; // Skip to next tower
            }
            // Check if the tower is already attacking an enemy and if that enemy is still in range
            
            if (activeAttacks.containsKey(tower)) {
                Entity currentEnemy = activeAttacks.get(tower);
                if (currentEnemy != null && entities.contains(currentEnemy)) {
                    Vector2 currentEnemyPosition = currentEnemy.getComponent(PositionComponent.class).get().position;
                    float distance = Vector2.dst(towerPosition, currentEnemyPosition);
                    if (distance <= uvDistance.len()) {
                        // Enemy is still in range, continue to attack
                        attackEnemy(currentEnemy, damage);
                        towerComponent.resetAttackTimer();
                        continue; // Skip to next tower
                    } else {
                        // Enemy out of range, allow tower to search for a new target
                        activeAttacks.remove(tower);
                    }
                } else {
                    // Current target no longer valid
                    activeAttacks.remove(tower);
                }
            }

            // Find a new enemy to attack if not already attacking or target is out of range
            for (Entity enemy : enemies) {
                Vector2 enemyPosition = enemy.getComponent(PositionComponent.class).get().position;
                float distance = Vector2.dst(towerPosition, enemyPosition);
                if (distance <= uvDistance.len()) {
                    activeAttacks.put(tower, enemy); // Assign new enemy to tower
                    attackEnemy(enemy, damage);
                    towerComponent.resetAttackTimer();
                    break; // Ensure the tower only attacks one enemy
                } 
            }
        }
    }


    private void attackEnemy(Entity enemy, int damage) {
        HealthComponent healthComp = enemy.getComponent(HealthComponent.class).get();
        healthComp.setHealth(healthComp.getHealth() - damage);
    }
}