package com.softwarearchitecture.game_server;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.TargetComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;

public class GameState implements Externalizable {
    private UUID id;
    private Entity village;
    private Entity playerOne;
    private Optional<Entity> playerTwo;
    private List<List<MapTile>> map;


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        // Writing gameID
        out.writeObject(id);
        // Village health
        ComponentManager<HealthComponent> healthManager = manager.getComponentManager(HealthComponent.class);
        HealthComponent villageHealth = (HealthComponent) healthManager.getComponent(village);
        if (villageHealth != null) {
            out.writeInt(villageHealth.getHealth());
        } else {
            throw new IllegalStateException("Village has no health component");
        }
        // Player money
        ComponentManager<MoneyComponent> goldManager = manager.getComponentManager(MoneyComponent.class);
        MoneyComponent playerOneMoney = (MoneyComponent) goldManager.getComponent(playerOne);
        if (playerOneMoney != null) {
            out.writeInt(playerOneMoney.amount);
        } else {
            throw new IllegalStateException("Player one has no money component");
        }
        if (playerTwo.isPresent()) {
            MoneyComponent playerTwoMoney = (MoneyComponent) goldManager.getComponent(playerTwo.get());
            if (playerTwoMoney != null) {
                out.writeInt(playerTwoMoney.amount);
            } else {
                throw new IllegalStateException("Player two has no money component");
            }
        } else {
            out.writeInt(0);
        }

        // Cards (for each player)
        // TODO: Implement cards and then make them serializable here

        // Map
        out.writeObject(map);

        // Towers
        ComponentManager<TowerComponent> towerManager = manager.getComponentManager(TowerComponent.class);
        ComponentManager<TargetComponent> targetManager = manager.getComponentManager(TargetComponent.class);
        ArrayList<TowerComponent> towers = new ArrayList<>();
        ArrayList<TargetComponent> targets = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            TowerComponent tower = (TowerComponent) towerManager.getComponent(entity);
            TargetComponent target = (TargetComponent) targetManager.getComponent(entity);
            
            if (tower != null && target != null) {
                towers.add(tower);
                targets.add(target);
            } else if (tower != null && target == null) {
                throw new IllegalStateException("Tower and target components must be present together");
            }
        }
        if (towers.size() != targets.size()) {
            throw new IllegalStateException("Towers and targets must be present in equal numbers");
        }
        out.writeObject(towers);
        out.writeObject(targets);

        // Enemies
        ComponentManager<PositionComponent> positionManager = manager.getComponentManager(PositionComponent.class);
        ComponentManager<EnemyComponent> enemyManager = manager.getComponentManager(EnemyComponent.class);
        ArrayList<HealthComponent> enemyHealths = new ArrayList<>();
        ArrayList<PositionComponent> enemyPositions = new ArrayList<>();
        ArrayList<EnemyComponent> enemies = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            PositionComponent position = (PositionComponent) positionManager.getComponent(entity);
            EnemyComponent enemy = (EnemyComponent) enemyManager.getComponent(entity);
            HealthComponent health = (HealthComponent) healthManager.getComponent(entity);

            if (position != null && enemy != null && health != null) {
                enemyHealths.add(health);
                enemyPositions.add(position);
                enemies.add(enemy);
            } else if (enemy != null && (position == null || health == null)) {
                throw new IllegalStateException("Enemy, position, and health components must be present together");
            }
        }
        if (!(enemyHealths.size() == enemyPositions.size() && enemyHealths.size() == enemies.size())) {
            throw new IllegalStateException("Enemy healths, positions, and enemies must be present in equal numbers");
        }
        out.writeObject(enemyHealths);
        out.writeObject(enemyPositions);
        out.writeObject(enemies);

        // Placed cards
        // TODO: Implement placed cards and then make them serializable here

        // Store cards
        // TODO: What cards are available to purchase in the store

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'writeExternal'");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Game id
        id = (UUID) in.readObject();

        // Village health
        int villageHealth = in.readInt();
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<HealthComponent> healthManager = manager.getComponentManager(HealthComponent.class);
        HealthComponent villageHealthComponent = (HealthComponent) healthManager.getComponent(village);
        if (villageHealthComponent != null) {
            villageHealthComponent.setHealth(villageHealth);
        } else {
            throw new IllegalStateException("Village has no health component");
        }

        // Player money
        int playerOneMoney = in.readInt();
        ComponentManager<MoneyComponent> goldManager = manager.getComponentManager(MoneyComponent.class);
        MoneyComponent playerOneMoneyComponent = (MoneyComponent) goldManager.getComponent(playerOne);
        if (playerOneMoneyComponent != null) {
            playerOneMoneyComponent.amount = playerOneMoney;
        } else {
            throw new IllegalStateException("Player one has no money component");
        }
        int playerTwoMoney = in.readInt();
        if (playerTwo.isPresent()) {
            MoneyComponent playerTwoMoneyComponent = (MoneyComponent) goldManager.getComponent(playerTwo.get());
            if (playerTwoMoneyComponent != null) {
                playerTwoMoneyComponent.amount = playerTwoMoney;
            } else {
                throw new IllegalStateException("Player two has no money component");
            }
        }

        // Cards (for each player)
        // This is a TODO, see writeExternal

        // Map
        Object obj = in.readObject();
        if (obj instanceof ArrayList) {
            ArrayList<?> list = (ArrayList<?>) obj;
            if (list.size() > 0 && list.get(0) instanceof ArrayList) {
                ArrayList<?> innerList = (ArrayList<?>) list.get(0);
                if (innerList.size() > 0 && innerList.get(0) instanceof MapTile) {
                    this.map = (List<List<MapTile>>) obj;
                } else {
                    throw new IllegalStateException("Map must be an ArrayList of ArrayLists of MapTiles");
                }
            } else {
                throw new IllegalStateException("Map must be an ArrayList of ArrayLists of MapTiles");
            }
        } else {
            throw new IllegalStateException("Map must be an ArrayList of ArrayLists of MapTiles");
        }

        // Towers
        Object towersObj = in.readObject();
        Object targetsObj = in.readObject();
        if (towersObj instanceof ArrayList && targetsObj instanceof ArrayList) {
            ArrayList<?> towers = (ArrayList<?>) towersObj;
            ArrayList<?> targets = (ArrayList<?>) targetsObj;
            if (towers.size() == targets.size()) {
                ComponentManager<TowerComponent> towerManager = manager.getComponentManager(TowerComponent.class);
                ComponentManager<TargetComponent> targetManager = manager.getComponentManager(TargetComponent.class);
                for (int i = 0; i < towers.size(); i++) {
                    if (towers.get(i) instanceof TowerComponent && targets.get(i) instanceof TargetComponent) {
                        // TODO: Find tower with same id and update its components or else create it and add it to the ECS
                        Entity entity = new Entity();
                        throw new UnsupportedOperationException("Unimplemented method 'readExternal'. We need to identify towers on something else than entity id.");
                        //towerManager.addComponent(entity, (TowerComponent) towers.get(i));
                        //targetManager.addComponent(entity, (TargetComponent) targets.get(i));
                    } else {
                        throw new IllegalStateException("Towers and targets must be present together");
                    }
                }
            } else {
                throw new IllegalStateException("Towers and targets must be present in equal numbers");
            }    
        } else {
            throw new IllegalStateException("The towers- and targets- objects must be ArrayLists");
        }

        // Enemies
        Object enemyHealthsObj = in.readObject();
        Object enemyPositionsObj = in.readObject();
        Object enemiesObj = in.readObject();
        if (enemyHealthsObj instanceof ArrayList && enemyPositionsObj instanceof ArrayList && enemiesObj instanceof ArrayList) {
            ArrayList<?> enemyHealths = (ArrayList<?>) enemyHealthsObj;
            ArrayList<?> enemyPositions = (ArrayList<?>) enemyPositionsObj;
            ArrayList<?> enemies = (ArrayList<?>) enemiesObj;
            if (enemyHealths.size() == enemyPositions.size() && enemyHealths.size() == enemies.size()) {
                ComponentManager<PositionComponent> positionManager = manager.getComponentManager(PositionComponent.class);
                ComponentManager<EnemyComponent> enemyManager = manager.getComponentManager(EnemyComponent.class);
                for (int i = 0; i < enemyHealths.size(); i++) {
                    if (enemyHealths.get(i) instanceof HealthComponent && enemyPositions.get(i) instanceof PositionComponent && enemies.get(i) instanceof EnemyComponent) {
                        Entity entity = new Entity();
                        throw new UnsupportedOperationException("Unimplemented method 'readExternal'. We need to identify enemies on something else than entity id.");
                        //positionManager.addComponent(entity, (PositionComponent) enemyPositions.get(i));
                        //enemyManager.addComponent(entity, (EnemyComponent) enemies.get(i));
                        //healthManager.addComponent(entity, (HealthComponent) enemyHealths.get(i));
                    } else {
                        throw new IllegalStateException("Enemy healths, positions, and enemies must be present together");
                    }
                }
            } else {
                throw new IllegalStateException("Enemy healths, positions, and enemies must be present in equal numbers");
            }
        } else {
            throw new IllegalStateException("The enemyHealths-, enemyPositions-, and enemies- objects must be ArrayLists");
        }

        // Placed cards
        // This is a TODO, see writeExternal

        // Store cards
        // This is a TODO, see writeExternal

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readExternal'");
    }
    
}
