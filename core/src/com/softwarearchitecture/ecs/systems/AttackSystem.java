package com.softwarearchitecture.ecs.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.AreaOfEffectComponent;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.game_server.TowerFactory;
import com.softwarearchitecture.math.Vector2;


/**
 * The {@code AttackSystem} class manages the interaction between tower entities and enemy entities,
 * enabling towers to attack enemies within their range. It utilizes several component managers to
 * facilitate the retrieval and updating of necessary components such as {@link TowerComponent},
 * {@link EnemyComponent}, and {@link HealthComponent}.
 * 
 * <p>This system checks the proximity of each tower to potential enemy targets, applying damage if
 * enemies are within attack range. It supports both single-target and area-of-effect attacks,
 * depending on the tower's specifications.</p>
 * 
 * <p>It also has the capability to 'copy' a tower through the {@link TowerFactory}, simulating effects
 * like replication or cloning as a gameplay mechanic.</p>
 */
public class AttackSystem implements System {
    private final Map gameMap;
    private HashMap<Entity, Entity> activeAttacks = new HashMap<>(); // Map to track which enemy each tower is currently attacking

    public AttackSystem(Map gameMap) {
        this.gameMap = gameMap;
    }


    /**
     * Updates the attack state of all towers, enabling them to attack enemies within their range.
     * This method processes each entity, distinguishing between towers and enemies, and handles
     * their interactions based on proximity and attack capabilities.
     *
     * @param entities   the set of all active entities that may include both towers and enemies
     * @param deltaTime  the time elapsed since the last update, used for managing attack timings
     */
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

            // Handle area of effect if applicable
            Optional<AreaOfEffectComponent> areaOfEffectComponent = tower.getComponent(AreaOfEffectComponent.class);
            if (areaOfEffectComponent.isPresent()) {
                for (Entity enemy : enemies) {
                    Vector2 enemyPosition = enemy.getComponent(PositionComponent.class).get().position;
                    float distance = Vector2.dst(towerPosition, enemyPosition);
                    if (distance <= uvDistance.len()) {
                        attackEnemy(tower, enemy, damage, true);
                    }
                }
                copyTower(tower);
                towerComponent.resetAttackTimer();
                continue; // Skip to next tower
            }
            
            // Handle individual attacks
            // Check if the tower is already attacking an enemy and if that enemy is still in range
            if (activeAttacks.containsKey(tower)) {
                Entity currentEnemy = activeAttacks.get(tower);
                if (currentEnemy != null && entities.contains(currentEnemy)) {
                    Vector2 currentEnemyPosition = currentEnemy.getComponent(PositionComponent.class).get().position;
                    float distance = Vector2.dst(towerPosition, currentEnemyPosition);
                    if (distance <= uvDistance.len()) {
                        // Enemy is still in range, continue to attack
                        attackEnemy(tower, currentEnemy, damage, false);
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
                    attackEnemy(tower, enemy, damage, false);
                    towerComponent.resetAttackTimer();
                    break; // Ensure the tower only attacks one enemy
                } 
            }
        }
    }

    
    /**
     * Converts a range in game units to a vector representing distance in UV coordinates,
     * based on the tile dimensions of the game map.
     *
     * @param range the range in game units
     * @return a {@code Vector2} representing the distance in UV coordinates
     */
    private Vector2 convertRangeToUVDistance(float range) {
        float tileWidth = gameMap.getTileWidth();
        float tileHeight = gameMap.getTileHeight();
        return new Vector2(range * tileWidth, range * tileHeight);
    }

    /**
     * Performs an attack on an enemy entity by a tower entity. It manages health reduction, animations,
     * and sound effects based on whether the attack is a standard or area of effect.
     *
     * @param tower            the tower performing the attack
     * @param enemy            the enemy being attacked
     * @param damage           the damage to apply to the enemy's health
     * @param isAreaOfEffect   indicates whether the attack is an area effect
     */
    private void attackEnemy(Entity tower, Entity enemy, int damage, boolean isAreaOfEffect) {
        java.lang.System.out.println("Angriper!");
        ComponentManager<TowerComponent> towerManager = ECSManager.getInstance().getOrDefaultComponentManager(TowerComponent.class);
        Optional<TowerComponent> towerComp = towerManager.getComponent(tower);
        if (towerComp.isPresent()) {
            towerComp.get().playSound = false;
        }
        
        ComponentManager<AnimationComponent> animationManager = ECSManager.getInstance().getOrDefaultComponentManager(AnimationComponent.class);
        Optional<AnimationComponent> animationComp = animationManager.getComponent(tower);
        if (animationComp.isPresent()) {
            animationComp.get().isPlaying = true;
        }
        
        HealthComponent healthComp = enemy.getComponent(HealthComponent.class).get();
        
        healthComp.setHealth(healthComp.getHealth() - damage);
        
        if (!isAreaOfEffect)
            copyTower(tower);

    }

    /**
     * Replicates a tower entity when a specific gameplay mechanic is triggered,
     * effectively cloning the tower and placing the new instance into the game world.
     *
     * @param tower the tower to copy
     */
    private void copyTower(Entity tower) {
        Entity newTowerEntity = TowerFactory.copyTower(tower);
        if (newTowerEntity != null) {
            ECSManager.getInstance().addLocalEntity(newTowerEntity);
            ECSManager.getInstance().removeLocalEntity(tower);
        }
    }

}