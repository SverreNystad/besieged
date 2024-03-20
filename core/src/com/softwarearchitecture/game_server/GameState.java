package com.softwarearchitecture.game_server;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.softwarearchitecture.ecs.Card;
import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.CardHolderComponent;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.ShopComponent;
import com.softwarearchitecture.ecs.components.TargetComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;
import com.softwarearchitecture.game_server.cards.elemental_cards.IceCard;
import com.softwarearchitecture.game_server.cards.elemental_cards.LightningCard;
import com.softwarearchitecture.game_server.cards.elemental_cards.TechnologyCard;
import com.softwarearchitecture.game_server.cards.tower_cards.BowCard;
import com.softwarearchitecture.game_server.cards.tower_cards.MagicCard;
import com.softwarearchitecture.game_server.cards.tower_cards.MeleeCard;
import com.softwarearchitecture.game_server.cards.tower_cards.PowerCard;


public class GameState implements Externalizable {
    public static final List<Class<? extends Card>> card_classes = new ArrayList<>(
        Arrays.asList(
            IceCard.class,
            LightningCard.class,
            TechnologyCard.class,
            BowCard.class,
            MagicCard.class,
            MeleeCard.class,
            PowerCard.class
            )
    );
    private static final String game_version = "0.1";
    private UUID id;
    private Entity village;
    private Entity playerOne;
    private Entity playerTwo;
    private List<List<MapTile>> map;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(game_version);
        // Writing gameID
        out.writeObject(id);
        out.writeObject(playerOne);
        out.writeObject(playerTwo);
        // Village health
        serializeVillageHealth(out);
        // Player money
        serializePlayerMoney(out);
        // Cards (for each player)
        serializePlayerCards(out);
        // Map
        out.writeObject(map);
        // Towers
        serializeTowers(out);
        // Enemies
        serializeEnemies(out);
        // Placed cards
        serializePlacedCards(out);
        // Store cards
        serializeShopCards(out);
    }

    private void serializeVillageHealth(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<HealthComponent> healthManager = manager.getComponentManager(HealthComponent.class);
        HealthComponent villageHealth = (HealthComponent) healthManager.getComponent(village);
        if (villageHealth != null) {
            out.writeObject(village);
            out.writeObject(villageHealth);
        } else {
            throw new IllegalStateException("Village has no health component");
        }
    }

    private void serializePlayerMoney(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<MoneyComponent> goldManager = manager.getComponentManager(MoneyComponent.class);
        MoneyComponent playerOneMoney = (MoneyComponent) goldManager.getComponent(playerOne);
        if (playerOneMoney != null) {
            out.writeObject(playerOneMoney);
        } else {
            throw new IllegalStateException("Player one has no money component");
        }
        if (playerTwo != null) {
            MoneyComponent playerTwoMoney = (MoneyComponent) goldManager.getComponent(playerTwo);
            if (playerTwoMoney != null) {
                out.writeObject(playerTwoMoney);
            } else {
                throw new IllegalStateException("Player two has no money component");
            }
        } else {
            out.writeObject(null);
        }
    }

    private void serializePlayerCards(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<CardHolderComponent> cardHolderManager = manager.getComponentManager(CardHolderComponent.class);
        CardHolderComponent playerOneCardHolder = (CardHolderComponent) cardHolderManager.getComponent(playerOne);
        if (playerOneCardHolder != null) {
            out.writeObject(playerOneCardHolder);
        } else {
            throw new IllegalStateException("Player one has no card holder component");
        }
        if (playerTwo != null) {
            CardHolderComponent playerTwoCardHolder = (CardHolderComponent) cardHolderManager.getComponent(playerTwo);
            if (playerTwoCardHolder != null) {
                out.writeObject(playerTwoCardHolder);
            } else {
                throw new IllegalStateException("Player two has no card holder component");
            }
        } else {
            out.writeObject(null);
        }
    }

    private void serializeTowers(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<TowerComponent> towerManager = manager.getComponentManager(TowerComponent.class);
        ComponentManager<TargetComponent> targetManager = manager.getComponentManager(TargetComponent.class);
        ArrayList<Entity> towerEntities = new ArrayList<>();
        ArrayList<TowerComponent> towers = new ArrayList<>();
        ArrayList<TargetComponent> targets = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            TowerComponent tower = (TowerComponent) towerManager.getComponent(entity);
            TargetComponent target = (TargetComponent) targetManager.getComponent(entity);
            
            if (tower != null && target != null) {
                towerEntities.add(entity);
                towers.add(tower);
                targets.add(target);
            } else if (tower != null && target == null) {
                throw new IllegalStateException("Tower and target components must be present together");
            }
        }
        if (towers.size() != targets.size()) {
            throw new IllegalStateException("Towers and targets must be present in equal numbers");
        }
        out.writeObject(towerEntities);
        out.writeObject(towers);
        out.writeObject(targets);
    }

    private void serializeEnemies(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<PositionComponent> positionManager = manager.getComponentManager(PositionComponent.class);
        ComponentManager<EnemyComponent> enemyManager = manager.getComponentManager(EnemyComponent.class);
        ComponentManager<HealthComponent> healthManager = manager.getComponentManager(HealthComponent.class);
        ArrayList<Entity> enemyEntities = new ArrayList<>();
        ArrayList<HealthComponent> enemyHealths = new ArrayList<>();
        ArrayList<PositionComponent> enemyPositions = new ArrayList<>();
        ArrayList<EnemyComponent> enemies = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            PositionComponent position = (PositionComponent) positionManager.getComponent(entity);
            EnemyComponent enemy = (EnemyComponent) enemyManager.getComponent(entity);
            HealthComponent health = (HealthComponent) healthManager.getComponent(entity);

            if (position != null && enemy != null && health != null) {
                enemyEntities.add(entity);
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
        out.writeObject(enemyEntities);
        out.writeObject(enemyHealths);
        out.writeObject(enemyPositions);
        out.writeObject(enemies);
    }

    private void serializePlacedCards(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<PlacedCardComponent> placedCardManager = manager.getComponentManager(PlacedCardComponent.class);
        ComponentManager<PositionComponent> positionManager = manager.getComponentManager(PositionComponent.class);
        ArrayList<Entity> placedCardEntities = new ArrayList<>();
        ArrayList<PlacedCardComponent> placedCards = new ArrayList<>();
        ArrayList<PositionComponent> placedCardPositions = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            PlacedCardComponent placedCard = (PlacedCardComponent) placedCardManager.getComponent(entity);
            PositionComponent position = (PositionComponent) positionManager.getComponent(entity);
            if (placedCard != null && position != null) {
                placedCardEntities.add(entity);
                placedCards.add(placedCard);
                placedCardPositions.add(position);
            } else if (placedCard != null && position == null) {
                throw new IllegalStateException("Placed card and position components must be present together");
            }
        }
    }

    private void serializeShopCards(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<ShopComponent> shopManager = manager.getComponentManager(ShopComponent.class);
        ArrayList<Entity> shopEntities = new ArrayList<>();
        ArrayList<ShopComponent> shopCards = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            ShopComponent shopCard = (ShopComponent) shopManager.getComponent(entity);
            if (shopCard != null) {
                shopEntities.add(entity);
                shopCards.add(shopCard);
            }
        }
        if (shopEntities.size() != shopCards.size() || shopEntities.size() < 1) {
            throw new IllegalStateException("Shop cards must be present in equal numbers and at least one must be present");
        }
        out.writeObject(shopEntities);
        out.writeObject(shopCards);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Object readObject = in.readObject();
        if (!(readObject instanceof String)) {
            throw new IllegalStateException("Game version must be a string");
        }
        if (game_version != (String) in.readObject()) {
            throw new IllegalStateException("Game version mismatch");
        }
        // Reading gameID
        readObject = in.readObject();
        if (!(readObject instanceof UUID)) {
            throw new IllegalStateException("Game ID must be a UUID");
        }
        if (id != (UUID) readObject) {
            throw new IllegalStateException("Game ID mismatch");
        }
        // Players
        deserializePlayers(in);
        // Village health
        deserializeVillageHealth(in);
        // Player money
        deserializePlayerMoney(in);
        // Cards (for each player)
        deserializePlayerCards(in);
        // Map
        readObject = in.readObject();
        if (!(readObject instanceof List)) {
            throw new IllegalStateException("Map must be a list");
        } else {
            List<?> mapList = (List<?>) readObject;
            if (mapList.get(0) instanceof List) {
                throw new IllegalStateException("Map must be a list of lists");
            } else {
                List<List<?>> mapListList = (List<List<?>>) mapList;
                if (mapListList.get(0).get(0) instanceof MapTile) {
                    throw new IllegalStateException("Map must be a list of lists of MapTiles");
                }
            }
        }
        map = (List<List<MapTile>>) readObject;
        // Towers
        deserializeTowers(in);
        // Enemies
        deserializeEnemies(in);
        // Placed cards
        deserializePlacedCards(in);
        // Store cards
        deserializeShopCards(in);
    }
    
    private void deserializePlayers(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        Object readObject = in.readObject();
        if (!(readObject instanceof Entity)) {
            throw new IllegalStateException("Player one must be an entity");
        }
        playerOne = (Entity) readObject;
        manager.addEntity(playerOne);
        readObject = in.readObject();
        if (!(readObject instanceof Entity)) {
            throw new IllegalStateException("Player two must be an entity");
        }
        playerTwo = (Entity) readObject;
        manager.addEntity(playerTwo);
    }

    private void deserializeVillageHealth(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<HealthComponent> healthManager = manager.getComponentManager(HealthComponent.class);
        Object readObject = in.readObject();
        if (!(readObject instanceof Entity)) {
            throw new IllegalStateException("Village must be an entity");
        }
        village = (Entity) readObject;
        manager.addEntity(village);
        readObject = in.readObject();
        if (!(readObject instanceof HealthComponent)) {
            throw new IllegalStateException("Village health must be a health component");
        }
        HealthComponent villageHealth = (HealthComponent) readObject;
        healthManager.addComponent(village, villageHealth);
    }

    private void deserializePlayerMoney(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<MoneyComponent> goldManager = manager.getComponentManager(MoneyComponent.class);
        Object readObject = in.readObject();
        if (!(readObject instanceof MoneyComponent)) {
            throw new IllegalStateException("Player one money must be a money component");
        }
        MoneyComponent playerOneMoney = (MoneyComponent) readObject;
        goldManager.addComponent(playerOne, playerOneMoney);
        readObject = in.readObject();
        if (playerTwo != null) {
            if (!(readObject instanceof MoneyComponent)) {
                throw new IllegalStateException("Player two money must be a money component");
            }
            MoneyComponent playerTwoMoney = (MoneyComponent) readObject;
            goldManager.addComponent(playerTwo, playerTwoMoney);
        } else {
            if (readObject != null) {
                throw new IllegalStateException("Player two money must be null, since player two is not part of the game!");
            }
        }
    }

    private void deserializePlayerCards(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<CardHolderComponent> cardHolderManager = manager.getComponentManager(CardHolderComponent.class);
        Object readObject = in.readObject();
        if (!(readObject instanceof CardHolderComponent)) {
            throw new IllegalStateException("Player one card holder must be a card holder component");
        }
        CardHolderComponent playerOneCardHolder = (CardHolderComponent) readObject;
        cardHolderManager.addComponent(playerOne, playerOneCardHolder);
        readObject = in.readObject();
        if (playerTwo != null) {
            if (!(readObject instanceof CardHolderComponent)) {
                throw new IllegalStateException("Player two card holder must be a card holder component");
            }
            CardHolderComponent playerTwoCardHolder = (CardHolderComponent) readObject;
            cardHolderManager.addComponent(playerTwo, playerTwoCardHolder);
        } else {
            if (readObject != null) {
                throw new IllegalStateException("Player two card holder must be null, since player two is not part of the game!");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void deserializeTowers(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<TowerComponent> towerManager = manager.getComponentManager(TowerComponent.class);
        ComponentManager<TargetComponent> targetManager = manager.getComponentManager(TargetComponent.class);
        Object readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof Entity) {
            throw new IllegalStateException("Towers must be an array list of entities");
        }
        ArrayList<Entity> towerEntities = (ArrayList<Entity>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof TowerComponent) {
            throw new IllegalStateException("Towers must be an array list of tower components");
        }
        ArrayList<TowerComponent> towers = (ArrayList<TowerComponent>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof TargetComponent) {
            throw new IllegalStateException("Towers must be an array list of target components");
        }
        ArrayList<TargetComponent> targets = (ArrayList<TargetComponent>) readObject;
        if (!(towers.size() == targets.size() && towers.size() == towerEntities.size())) {
            throw new IllegalStateException("Towers and targets must be present in equal numbers");
        }

        for (int i = 0; i < towerEntities.size(); i++) {
            Entity towerEntity = towerEntities.get(i);
            TowerComponent tower = towers.get(i);
            TargetComponent target = targets.get(i);
            manager.addEntity(towerEntity);
            towerManager.addComponent(towerEntity, tower);
            targetManager.addComponent(towerEntity, target);
        }
    }

    @SuppressWarnings("unchecked")
    private void deserializeEnemies(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<PositionComponent> positionManager = manager.getComponentManager(PositionComponent.class);
        ComponentManager<EnemyComponent> enemyManager = manager.getComponentManager(EnemyComponent.class);
        ComponentManager<HealthComponent> healthManager = manager.getComponentManager(HealthComponent.class);
        Object readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof Entity) {
            throw new IllegalStateException("Enemies must be an array list of entities");
        }
        ArrayList<Entity> enemyEntities = (ArrayList<Entity>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof HealthComponent) {
            throw new IllegalStateException("Enemies must be an array list of health components");
        }
        ArrayList<HealthComponent> enemyHealths = (ArrayList<HealthComponent>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof PositionComponent) {
            throw new IllegalStateException("Enemies must be an array list of position components");
        }
        ArrayList<PositionComponent> enemyPositions = (ArrayList<PositionComponent>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof EnemyComponent) {
            throw new IllegalStateException("Enemies must be an array list of enemy components");
        }
        ArrayList<EnemyComponent> enemies = (ArrayList<EnemyComponent>) readObject;

        if (!(enemyHealths.size() == enemyPositions.size() && enemyHealths.size() == enemies.size())) {
            throw new IllegalStateException("Enemy healths, positions, and enemies must be present in equal numbers");
        }

        for (int i = 0; i < enemyEntities.size(); i++) {
            Entity enemyEntity = enemyEntities.get(i);
            HealthComponent health = enemyHealths.get(i);
            PositionComponent position = enemyPositions.get(i);
            EnemyComponent enemy = enemies.get(i);
            manager.addEntity(enemyEntity);
            healthManager.addComponent(enemyEntity, health);
            positionManager.addComponent(enemyEntity, position);
            enemyManager.addComponent(enemyEntity, enemy);
        }
    }

    @SuppressWarnings("unchecked")
    private void deserializePlacedCards(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<PlacedCardComponent> placedCardManager = manager.getComponentManager(PlacedCardComponent.class);
        ComponentManager<PositionComponent> positionManager = manager.getComponentManager(PositionComponent.class);
        Object readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof Entity) {
            throw new IllegalStateException("Placed cards must be an array list of entities");
        }
        ArrayList<Entity> placedCardEntities = (ArrayList<Entity>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof PlacedCardComponent) {
            throw new IllegalStateException("Placed cards must be an array list of placed card components");
        }
        ArrayList<PlacedCardComponent> placedCards = (ArrayList<PlacedCardComponent>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof PositionComponent) {
            throw new IllegalStateException("Placed cards must be an array list of position components");
        }
        ArrayList<PositionComponent> placedCardPositions = (ArrayList<PositionComponent>) readObject;

        if (!(placedCards.size() == placedCardPositions.size() && placedCards.size() == placedCardEntities.size())) {
            throw new IllegalStateException("Placed cards, positions, and entities must be present in equal numbers");
        }

        for (int i = 0; i < placedCardEntities.size(); i++) {
            Entity placedCardEntity = placedCardEntities.get(i);
            PlacedCardComponent placedCard = placedCards.get(i);
            PositionComponent position = placedCardPositions.get(i);
            manager.addEntity(placedCardEntity);
            placedCardManager.addComponent(placedCardEntity, placedCard);
            positionManager.addComponent(placedCardEntity, position);
        }
    }

    @SuppressWarnings("unchecked")
    private void deserializeShopCards(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<ShopComponent> shopManager = manager.getComponentManager(ShopComponent.class);
        Object readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof Entity) {
            throw new IllegalStateException("Shop cards must be an array list of entities");
        }
        ArrayList<Entity> shopEntities = (ArrayList<Entity>) readObject;
        readObject = in.readObject();
        if (!(readObject instanceof ArrayList) && ((ArrayList<?>) readObject).get(0) instanceof ShopComponent) {
            throw new IllegalStateException("Shop cards must be an array list of shop components");
        }
        ArrayList<ShopComponent> shopCards = (ArrayList<ShopComponent>) readObject;

        if (shopEntities.size() != shopCards.size() || shopEntities.size() < 1) {
            throw new IllegalStateException("Shop cards must be present in equal numbers and at least one must be present");
        }

        for (int i = 0; i < shopEntities.size(); i++) {
            Entity shopEntity = shopEntities.get(i);
            ShopComponent shopCard = shopCards.get(i);
            manager.addEntity(shopEntity);
            shopManager.addComponent(shopEntity, shopCard);
        }
    }

}
