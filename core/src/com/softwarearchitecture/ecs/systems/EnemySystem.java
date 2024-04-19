package com.softwarearchitecture.ecs.systems;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;
import com.softwarearchitecture.ecs.components.VillageComponent;
import com.softwarearchitecture.game_client.states.GameOverObserver;
import com.softwarearchitecture.game_server.EnemyFactory;
import com.softwarearchitecture.game_server.EnemyFactory.EnemyType;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TileType;
import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.math.Vector3;
import com.softwarearchitecture.ecs.GraphicsController;

/**
 * This class is supposed to check if enemies are at the end of the map, and if
 * so despawns them
 * 
 */
public class EnemySystem implements System {
    private ComponentManager<PositionComponent> positionManager;
    private ComponentManager<VelocityComponent> velocityManager;
    private ComponentManager<PathfindingComponent> pathfindingManager;
    private ComponentManager<SpriteComponent> drawableManager;
    private ComponentManager<TileComponent> tileManager;
    private ComponentManager<HealthComponent> healthManager;
    private ComponentManager<TextComponent> textManager;
    private ComponentManager<MoneyComponent> moneyManager;
    private ComponentManager<EnemyComponent> enemyManager;
    private ComponentManager<PlayerComponent> playerManager;
    private ComponentManager<VillageComponent> villageManger;   
    private int waveSize;
    private int monsterCounter;
    private int waveNumber;
    private float spawnTimer;
    private float waveTimer;
    private List<Tile> path = null;
    private Entity mob;
    private int liveMonsterCounter;
    private int maxLiveMonsters;
    private int villageDamage;
    private GraphicsController graphicsController;
    private boolean firstUpdate = true;
    private GameOverObserver gameOverObserver;
    private Entity waveNumberEntity = null;

    public EnemySystem() {
        this.positionManager = ECSManager.getInstance().getOrDefaultComponentManager(PositionComponent.class);
        this.velocityManager = ECSManager.getInstance().getOrDefaultComponentManager(VelocityComponent.class);
        this.drawableManager = ECSManager.getInstance().getOrDefaultComponentManager(SpriteComponent.class);
        this.pathfindingManager = ECSManager.getInstance().getOrDefaultComponentManager(PathfindingComponent.class);
        this.tileManager = ECSManager.getInstance().getOrDefaultComponentManager(TileComponent.class);
        this.healthManager = ECSManager.getInstance().getOrDefaultComponentManager(HealthComponent.class);
        this.moneyManager = ECSManager.getInstance().getOrDefaultComponentManager(MoneyComponent.class);
        this.textManager = ECSManager.getInstance().getOrDefaultComponentManager(TextComponent.class);
        this.playerManager = ECSManager.getInstance().getOrDefaultComponentManager(PlayerComponent.class);
        this.enemyManager = ECSManager.getInstance().getOrDefaultComponentManager(EnemyComponent.class);
        this.villageManger = ECSManager.getInstance().getOrDefaultComponentManager(VillageComponent.class);
        this.waveSize = 10;
        this.monsterCounter = 0;
        this.waveNumber = 1;
        this.spawnTimer = 20f;
        this.waveTimer = 1000f;
        this.maxLiveMonsters = 5;
        this.liveMonsterCounter = 0;
        this.villageDamage = 0;
    }

    public EnemySystem(GameOverObserver gameOverObserver) {
        this();
        this.gameOverObserver = gameOverObserver;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        Entity village = null;

        for (Entity entity : entities) {
            Optional<HealthComponent> healthComponent = healthManager.getComponent(entity);
            Optional<VillageComponent> villageComponent = villageManger.getComponent(entity);
            if (villageComponent.isPresent() && healthComponent.isPresent()) {  
                village = entity;
                break;
            }
        }
        // Set this.village to the village entity, but only once
        if (firstUpdate == true) {
            initializeWaveNumberDisplay();
            firstUpdate = false;
        }

        // Get tile size so that enemies follow the path correctly
        Vector2 tileSize = new Vector2(0, 0);
        for (Entity entity : entities) {
            if (path == null) {
                Optional<PathfindingComponent> possiblePath = pathfindingManager.getComponent(entity);
                if (possiblePath.isPresent()) {
                    path = possiblePath.get().path;
                }
            }

            Optional<SpriteComponent> sprite = drawableManager.getComponent(entity);
            Optional<TileComponent> tile = tileManager.getComponent(entity);
            if (sprite.isPresent() && tile.isPresent()) {
                tileSize = sprite.get().size_uv;
                continue;
            }
        }

        /* If the path is not initialized correctly escape early */
        if (path == null) {
            return;
        }
        // Check if any enemies have reached the end of the path
        for (Entity entity : entities) {
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            Optional<VelocityComponent> velocity = velocityManager.getComponent(entity);
            Optional<PathfindingComponent> pathfinding = pathfindingManager.getComponent(entity);
            Optional<HealthComponent> health = healthManager.getComponent(entity);
            Optional<EnemyComponent> enemy = enemyManager.getComponent(entity);
            
            if (!position.isPresent() || !velocity.isPresent() || !pathfinding.isPresent() || !health.isPresent()) {
                continue;
            }

            Vector2 pos = position.get().position;
            List<Tile> find = pathfinding.get().path;
            Tile nextTile = pathfinding.get().targetTile;
            int hp = health.get().getHealth();

            // If the enemy has reached the end of the path, move it to the start to be
            // spawned again and set villageDamage != 0 to damage the village
            if (nextTile.getType() == TileType.END) {
                pathfinding.get().targetTile = find.get(0);
                float startPosition_x = find.get(0).getX() * tileSize.x;
                float startPosition_y = find.get(0).getY() * tileSize.y;
                position.get().position = new Vector2(startPosition_x, startPosition_y);
                health.get().setHealth(health.get().getMaxHealth());
                monsterCounter++;
                int remainingEnemyHealth = health.get().getHealth();
                this.villageDamage += remainingEnemyHealth;
            } else if (hp <= 0) {
                position.get().position = new Vector2(-1, -1);
                velocity.get().velocity = new Vector2(0, 0);
                liveMonsterCounter--;
                boolean claimedReward = enemy.get().claimedReward;
                if (!claimedReward) {

                    awardPlayerMoney(village, entity);
                    enemy.get().claimedReward = true;
                }
            }
        }

        spawnTimer -= deltaTime;

        if (spawnTimer <= 0 && monsterCounter < waveSize) {

            // Keep creating enemies under max-limit is met
            if (liveMonsterCounter < maxLiveMonsters) {

                // random enemy
                EnemyType[] enemyTypes = EnemyType.values();
                EnemyType randomEnemy = enemyTypes[(int) (Math.random() * enemyTypes.length)];

                mob = EnemyFactory.createEnemy(randomEnemy, path, tileSize);
                ECSManager.getInstance().addLocalEntity(mob);
                monsterCounter++;
                liveMonsterCounter++;
                spawnTimer = 200f;
            }
            // If the max number of enemies has been met, check if any of them are dead
            else {

                for (Entity entity : entities) {
                    Optional<PositionComponent> position = positionManager.getComponent(entity);
                    Optional<VelocityComponent> velocity = velocityManager.getComponent(entity);
                    Optional<PathfindingComponent> pathfinding = pathfindingManager.getComponent(entity);
                    Optional<HealthComponent> health = healthManager.getComponent(entity);

                    if (!position.isPresent() || !velocity.isPresent() || !pathfinding.isPresent()
                            || !health.isPresent()) {
                        continue;
                    }

                    // If the enemy is dead, reset its position and velocity
                    List<Tile> find = pathfinding.get().path;
                    if (velocity.get().velocity.len() == 0) {
                        float startPosition_x = find.get(0).getX() * tileSize.x;
                        float startPosition_y = find.get(0).getY() * tileSize.y;
                        position.get().position = new Vector2(startPosition_x, startPosition_y);
                        pathfinding.get().targetTile = find.get(0);
                        velocity.get().velocity = velocity.get().baseVelocity;
                        health.get().setHealth(health.get().getMaxHealth());
                        liveMonsterCounter++;
                    }
                }
            }
        }

        // Decrement the wave timer
        if (waveTimer > 0) {
            waveTimer -= deltaTime;
        }

        // If any enemies have gotten through, damage the village (actually applies the
        // damage here)
        if (villageDamage > 0) {
            
            Optional<HealthComponent> healthComponent = healthManager.getComponent(village);
            Optional<VillageComponent> villageComponent = villageManger.getComponent(village);
            if (villageComponent.isPresent() && healthComponent.isPresent()) {
                int villageHealth = healthComponent.get().getHealth();

                villageHealth -= villageDamage;;
                // If the village health is 0, the game is over
                if (villageHealth <= 0) {
                    villageHealth = 0;
                }
                
                healthComponent.get().setHealth(villageHealth);
                
                updateTopRightCornerText(village);
                villageDamage = 0;
            }

        }

        // Start the next wave
        if (monsterCounter >= waveSize && waveTimer <= 0) {
            waveNumber++;
            monsterCounter = 0;
            waveSize += waveNumber * 2 - 2;
            waveTimer = 60f;
            spawnTimer = 0f;
            maxLiveMonsters++;
        }

        updateWaveNumberDisplay();
    }

    private void awardPlayerMoney(Entity village, Entity enemy) {
        MoneyComponent balance = moneyManager.getComponent(village).get();
        MoneyComponent reward = moneyManager.getComponent(enemy).get();
        balance.amount += reward.amount;
        updateTopRightCornerText(village);
    }

    private void updateTopRightCornerText(Entity village) {
        // Get the text-component of the village and update the health
        ComponentManager<TextComponent> textManager = ECSManager.getInstance()
                .getOrDefaultComponentManager(TextComponent.class);
        ComponentManager<MoneyComponent> moneyManager = ECSManager.getInstance()
                .getOrDefaultComponentManager(MoneyComponent.class);
        ComponentManager<HealthComponent> healthManager = ECSManager.getInstance()
                .getOrDefaultComponentManager(HealthComponent.class);
        Optional<TextComponent> textComponent = textManager.getComponent(village);
        Optional<MoneyComponent> moneyComponent = moneyManager.getComponent(village);
        Optional<HealthComponent> healthComponent = healthManager.getComponent(village);

        if (textComponent.isPresent() && moneyComponent.isPresent() && healthComponent.isPresent()) {
            int villageHealth = healthComponent.get().getHealth();
            int money = moneyComponent.get().amount;
            String textToDisplay = "Health: " + villageHealth + "\n Money: " + money;
            textComponent.get().text = textToDisplay;
        }
        
    }

    public void initializeWaveNumberDisplay() {
        // Create a new Entity for the wave number and add it to the ECS
        this.waveNumberEntity = new Entity();
        TextComponent waveNumberText = new TextComponent("Wave: " + waveNumber, new Vector2(0.05f, 0.05f));
        waveNumberText.setColor(new Vector3(0, 0, 0));
        PositionComponent waveNumberPosition = new PositionComponent(new Vector2(0.02f, 0.90f), 10);
        waveNumberEntity.addComponent(TextComponent.class, waveNumberText);
        waveNumberEntity.addComponent(PositionComponent.class, waveNumberPosition);
        ECSManager.getInstance().addLocalEntity(waveNumberEntity);
    }

    public void updateWaveNumberDisplay() {
        ComponentManager<TextComponent> textManager = ECSManager.getInstance()
                .getOrDefaultComponentManager(TextComponent.class);
        Optional<TextComponent> waveNumberText = textManager.getComponent(waveNumberEntity);

        if (waveNumberText.isPresent()) {
            waveNumberText.get().text = "Wave: " + waveNumber;
        }
    }
}
