package com.softwarearchitecture.game_server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
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

    public static final List<Class<? extends Card>> card_classes = new ArrayList<Class<? extends Card>>(
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

    public static final String game_version = "0.1";
    private Entity village = new Entity();
    public UUID gameID;
    public Entity playerOne;
    public Entity playerTwo;
    private Map map;


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(game_version);

        // Writing gameID
        out.writeObject(gameID);
        out.writeObject(playerOne);
        out.writeObject(playerTwo);

        if (playerOne == null || playerTwo == null) {
            return;
        }

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
        // serializeShopCards(out);
    }

    private void serializeVillageHealth(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<HealthComponent> healthManager = manager.getOrDefaultComponentManager(HealthComponent.class);
        Optional<HealthComponent> villageHealth = healthManager.getComponent(village);
        if (villageHealth.isPresent()) {
            out.writeObject(village);
            out.writeObject(villageHealth.get());
        } else {
            throw new IllegalStateException("Village has no health component");
        }
    }

    private void serializePlayerMoney(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<MoneyComponent> goldManager = manager.getOrDefaultComponentManager(MoneyComponent.class);
        Optional<MoneyComponent> playerOneMoney = goldManager.getComponent(playerOne);
        if (playerOneMoney.isPresent()) {
            out.writeObject(playerOneMoney.get());
        } else {
            throw new IllegalStateException("Player one has no money component");
        }
        if (playerTwo != null) {
            Optional<MoneyComponent> playerTwoMoney = goldManager.getComponent(playerTwo);
            if (playerTwoMoney.isPresent()) {
                out.writeObject(playerTwoMoney.get());
            } else {
                throw new IllegalStateException("Player two has no money component");
            }
        } else {
            out.writeObject(null);
        }
    }

    private void serializePlayerCards(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<CardHolderComponent> cardHolderManager = manager.getOrDefaultComponentManager(CardHolderComponent.class);
        Optional<CardHolderComponent> playerOneCardHolder = cardHolderManager.getComponent(playerOne);
        if (playerOneCardHolder.isPresent()) {
            out.writeObject(playerOneCardHolder.get());
        } else {
            throw new IllegalStateException("Player one has no card holder component");
        }
        if (playerTwo != null) {
            Optional<CardHolderComponent> playerTwoCardHolder = cardHolderManager.getComponent(playerTwo);
            if (playerTwoCardHolder.isPresent()) {
                out.writeObject(playerTwoCardHolder.get());
            } else {
                throw new IllegalStateException("Player two has no card holder component");
            }
        } else {
            out.writeObject(null);
        }
    }

    private void serializeTowers(ObjectOutput out) throws IOException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<TowerComponent> towerManager = manager.getOrDefaultComponentManager(TowerComponent.class);
        ComponentManager<TargetComponent> targetManager = manager.getOrDefaultComponentManager(TargetComponent.class);
        ArrayList<Entity> towerEntities = new ArrayList<>();
        ArrayList<TowerComponent> towers = new ArrayList<>();
        ArrayList<TargetComponent> targets = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            Optional<TowerComponent> tower = towerManager.getComponent(entity);
            Optional<TargetComponent> target = targetManager.getComponent(entity);
            
            if (tower.isPresent() && target.isPresent()) {
                towerEntities.add(entity);
                towers.add(tower.get());
                targets.add(target.get());
            } else if (tower.isPresent() && !target.isPresent()) {
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
        ComponentManager<PositionComponent> positionManager = manager.getOrDefaultComponentManager(PositionComponent.class);
        ComponentManager<EnemyComponent> enemyManager = manager.getOrDefaultComponentManager(EnemyComponent.class);
        ComponentManager<HealthComponent> healthManager = manager.getOrDefaultComponentManager(HealthComponent.class);
        ArrayList<Entity> enemyEntities = new ArrayList<>();
        ArrayList<HealthComponent> enemyHealths = new ArrayList<>();
        ArrayList<PositionComponent> enemyPositions = new ArrayList<>();
        ArrayList<EnemyComponent> enemies = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            Optional<EnemyComponent> enemy = enemyManager.getComponent(entity);
            Optional<HealthComponent> health = healthManager.getComponent(entity);

            if (position.isPresent() && enemy.isPresent() && health.isPresent()) {
                enemyEntities.add(entity);
                enemyHealths.add(health.get());
                enemyPositions.add(position.get());
                enemies.add(enemy.get());
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
        ComponentManager<PlacedCardComponent> placedCardManager = manager.getOrDefaultComponentManager(PlacedCardComponent.class);
        ComponentManager<PositionComponent> positionManager = manager.getOrDefaultComponentManager(PositionComponent.class);
        ArrayList<Entity> placedCardEntities = new ArrayList<>();
        ArrayList<PlacedCardComponent> placedCards = new ArrayList<>();
        ArrayList<PositionComponent> placedCardPositions = new ArrayList<>();
        for (Entity entity : manager.getEntities()) {
            Optional<PlacedCardComponent> placedCard = placedCardManager.getComponent(entity);
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            if (placedCard.isPresent() && position.isPresent()) {
                placedCardEntities.add(entity);
                placedCards.add(placedCard.get());
                placedCardPositions.add(position.get());
            } else if (placedCard != null && position == null) {
                throw new IllegalStateException("Placed card and position components must be present together");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Object readObject = in.readObject();
        if (!(readObject instanceof String)) {
            throw new IllegalStateException("Game version must be a string");
        }
        if (!game_version.equals((String) readObject)) {
            System.out.println("Game version: " + game_version + game_version.length());
            String read_game_version = (String) readObject;
            System.out.println("Read version: " + read_game_version + read_game_version.length());
            
            throw new IllegalStateException("Game version mismatch");
        }
        
        readObject = in.readObject();
        if (!(readObject instanceof UUID)) {
            throw new IllegalStateException("The game id was not present");
        }
        gameID = (UUID) readObject;

        
        // Players
        deserializePlayers(in);
        if (playerTwo == null) {
            return;
        }
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
                if (mapListList.get(0).get(0) instanceof TileType) {
                    throw new IllegalStateException("Map must be a list of lists of MapTiles");
                }
            }
        }
        map = (Map) readObject;
        // Towers
        deserializeTowers(in);
        // Enemies
        deserializeEnemies(in);
        // Placed cards
        deserializePlacedCards(in);
    }
    
    private void deserializePlayers(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<PlayerComponent> playerManager = manager.getOrDefaultComponentManager(PlayerComponent.class);
        Object readObject = in.readObject();
        if (!(readObject instanceof Entity)) {
            System.out.println(readObject);
            throw new IllegalStateException("Player one must be an entity");
        }
        playerOne = (Entity) readObject;
        manager.addEntity(playerOne);
        
        // Check for player two
        readObject = in.readObject();
        if (!(readObject instanceof Entity)) {
            return;
        }
        playerTwo = (Entity) readObject;
        manager.addEntity(playerTwo);
    }

    private void deserializeVillageHealth(ObjectInput in) throws IOException, ClassNotFoundException {
        ECSManager manager = ECSManager.getInstance();
        ComponentManager<HealthComponent> healthManager = manager.getOrDefaultComponentManager(HealthComponent.class);
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
        ComponentManager<MoneyComponent> goldManager = manager.getOrDefaultComponentManager(MoneyComponent.class);
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
        ComponentManager<CardHolderComponent> cardHolderManager = manager.getOrDefaultComponentManager(CardHolderComponent.class);
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
        ComponentManager<TowerComponent> towerManager = manager.getOrDefaultComponentManager(TowerComponent.class);
        ComponentManager<TargetComponent> targetManager = manager.getOrDefaultComponentManager(TargetComponent.class);
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
        ComponentManager<PositionComponent> positionManager = manager.getOrDefaultComponentManager(PositionComponent.class);
        ComponentManager<EnemyComponent> enemyManager = manager.getOrDefaultComponentManager(EnemyComponent.class);
        ComponentManager<HealthComponent> healthManager = manager.getOrDefaultComponentManager(HealthComponent.class);
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
        ComponentManager<PlacedCardComponent> placedCardManager = manager.getOrDefaultComponentManager(PlacedCardComponent.class);
        ComponentManager<PositionComponent> positionManager = manager.getOrDefaultComponentManager(PositionComponent.class);
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

    public static GameState deserializeFromByteArray(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        GameState obj = (GameState) ois.readObject();
        ois.close();
        return obj;
    }

    public static byte[] serializeToByteArray(GameState state) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(state);
        oos.close();
        return baos.toByteArray();
    }
}