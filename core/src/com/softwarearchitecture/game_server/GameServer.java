package com.softwarearchitecture.game_server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.clock.Clock;
import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.components.VillageComponent;
import com.softwarearchitecture.ecs.systems.AnimationSystem;
import com.softwarearchitecture.ecs.systems.AttackSystem;
import com.softwarearchitecture.ecs.systems.EnemySystem;
import com.softwarearchitecture.ecs.systems.MovementSystem;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.PairableCards.TowerType;
import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.math.Vector3;

/**
 * Represents a game server that manages the lifecycle and state of a multiplayer game.
 * The server coordinates game creation, state updates, player actions, the main game loop and teardown after game over.
 */
public class GameServer {
    private UUID gameId;
    private UUID playerOneID;
    private UUID playerTwoID;
    private ServerMessagingController onlineMessageController;
    private ServerMessagingController localMessageController;

    private Map gameMap;
    private float aspectRatio;

    /**
     * Initializes a new game server with a message controller for communication and the UUID of player one.
     * @param onlineMessageController The communication controller used for interacting with clients.
     * @param localMessageController The communication controller used for local communication.
     * @param playerOneID The unique identifier for the first player in the game.
     */
    public GameServer(ServerMessagingController onlineMessageController, ServerMessagingController localMessageController, UUID playerOneID, float aspect_ratio) {
        this.onlineMessageController = onlineMessageController;
        this.localMessageController = localMessageController;
        this.aspectRatio = aspect_ratio;
        this.playerOneID = playerOneID;
    }

    /**
     * Starts the server operation, creating a game instance and entering the main gameplay loop.
     * The method handles game setup, player joining, and periodic state updates until the game ends.
     */
    public void run(String mapName, boolean isMultiplayer) {
        ServerMessagingController messageController = isMultiplayer ? onlineMessageController : localMessageController;
        GameState gameState = hostGame(mapName, messageController);

        // Wait for player two to join
        if (isMultiplayer) 
            playerTwoID = waitForPlayerToJoin(gameState);

        // TODO: Add relevant entities
        Entity village = new Entity();
        VillageComponent villageComponent = new VillageComponent();
        HealthComponent healthComponent = new HealthComponent(1000);
        MoneyComponent moneyComponent = new MoneyComponent(1000);
        
        // Add a text component to the village entity
        PositionComponent villagePosition = new PositionComponent(new Vector2(0.80f, 0.90f), 1000);
        String textToDisplay = "Health: " + healthComponent.getHealth() + "\n Money: " + moneyComponent.getAmount();
        TextComponent villageHealthText = new TextComponent(textToDisplay, new Vector2(0.05f, 0.05f));
        villageHealthText.setColor(new Vector3(0f, 0f, 0f));

        village.addComponent(VillageComponent.class, villageComponent);
        village.addComponent(HealthComponent.class, healthComponent);
        village.addComponent(MoneyComponent.class, moneyComponent);
        village.addComponent(PositionComponent.class, villagePosition);
        village.addComponent(TextComponent.class, villageHealthText);
        
        ECSManager.getInstance().addLocalEntity(village);
        
        gameState.playerTwo = new Entity();
        gameState.playerTwo.addComponent(PlayerComponent.class, new PlayerComponent(playerTwoID));
        ECSManager.getInstance().addLocalEntity(gameState.playerTwo);
        
        gameState.playerOne.addComponent(PlayerComponent.class, new PlayerComponent(playerOneID));
        gameState.playerTwo.addComponent(PlayerComponent.class, new PlayerComponent(playerTwoID));
        gameState.playerOne.addComponent(MoneyComponent.class, new MoneyComponent(0));
        gameState.playerTwo.addComponent(MoneyComponent.class, new MoneyComponent(0));

        ECSManager.getInstance().update(0f);
        // System.out.println("[SERVER] Entities 1: " + ECSManager.getInstance().getEntities().size());
        messageController.setNewGameState(this.gameId, gameState);
        System.out.println("[SERVER] Player two has joined the game");
        System.out.println("[SERVER] Game is now full");

        // TODO: Add relevant systems
        setupGame(mapName, gameState);

        // Component managers
        ComponentManager<HealthComponent> healthManager = ECSManager.getInstance().getOrDefaultComponentManager(HealthComponent.class);
        // Main gameplay loop
        boolean gamesOver = false;

        while (!gamesOver) {
            // System.out.println("[SERVER] Deltatime: " + Clock.getInstance().getDeltaTime());
            ECSManager.getInstance().update(Clock.getInstance().getAndResetDeltaTime());

            if (healthManager.getComponent(village).get().getHealth() <= 0) gamesOver = true;

            // Process each pending player action.
            for (PlayerInput action : messageController.lookForPendingActions(playerOneID)) {
                // Actions to process player inputs and update game state
                System.out.println("[SERVER] Processing player one action: " + action.getAction());
                handlePlayerAction(action);
            }
            if (isMultiplayer) {
                for (PlayerInput action : messageController.lookForPendingActions(playerTwoID)) {
                    // Actions to process player inputs and update game state
                    System.out.println("[SERVER] Processing player two action: " + action.getAction());
                    handlePlayerAction(action);
                }
            }


            // Update all clients with the latest game state.
            gameState.timeStamp = System.currentTimeMillis();
            messageController.setNewGameState(gameId, gameState);
            
            float deltaTime = Clock.getInstance().getDeltaTime();
            if (deltaTime < 0.01f) {
                try {
                    Thread.sleep(10 - (long) (deltaTime * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Thread.sleep(10_000); // TODO: Find a better solution than waiting for 10 seconds. If we don't wait the player will never get that the village has 0 health and has to go to the game over screen.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Teardown of server and delete game from games listing
        messageController.removeGame(gameId);
    }

    private GameState hostGame(String mapName, ServerMessagingController messageController) {
        this.gameId = messageController.createGame(mapName);
        System.out.println("[SERVER] Game created with ID: " + gameId);
        GameState gameState = messageController.getGameState(gameId);
        gameState.timeStamp = System.currentTimeMillis();
        if (messageController.getGameState(gameId).playerOne == null) {
            gameState.playerOne = new Entity();
            gameState.playerOne.addComponent(PlayerComponent.class, new PlayerComponent(playerOneID));
            ECSManager.getInstance().addLocalEntity(gameState.playerOne);
        }
        messageController.setNewGameState(this.gameId, gameState);
        return gameState;
    }
    
    /**
     * Waits for the second player to join the game.
     * This method polls the server state until a second player joins the game.
     * This is a blocking action.
     * @param gameState The current state of the game.
     */
    private UUID waitForPlayerToJoin(GameState gameState) {
        UUID playerTwoID = null;
        while (onlineMessageController.getGameState(gameId).playerTwo == null) {
            Optional<UUID> playerTwo = onlineMessageController.lookForPendingPlayer(gameId);
            System.out.println("[SERVER] Looking for player two");
            if (!playerTwo.isPresent())
                continue;
            playerTwoID = playerTwo.get();
            System.out.println("[SERVER] The player two joined with: " + playerTwo.get().toString());
            break;
        }
        return playerTwoID;
    }


    private void setupGame(String mapName, GameState gameState) {
        String backgroundPath = TexturePack.BACKGROUND_BLACK;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0, 0), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        TextComponent textComponent = new TextComponent("In Game!", new Vector2(0.05f, 0.05f));
        background.addComponent(TextComponent.class, textComponent);
        ECSManager.getInstance().addLocalEntity(background);

        // Map and tiles
        this.gameMap = MapFactory.createMap(mapName);
        initializeMapEntities(gameMap);

        initializeVillage(gameState);
    

        // Add systems to the ECSManager
        MovementSystem MovementSystem = new MovementSystem();
        EnemySystem EnemySystem = new EnemySystem();
        AttackSystem attackSystem = new AttackSystem(gameMap);
        AnimationSystem animationSystem = new AnimationSystem();
        
        ECSManager.getInstance().addSystem(animationSystem);
        ECSManager.getInstance().addSystem(MovementSystem);
        ECSManager.getInstance().addSystem(EnemySystem);
        ECSManager.getInstance().addSystem(attackSystem);


    }

    private void initializeMapEntities(Map gameMap) {
        Tile[][] tiles = gameMap.getMapLayout();
        int numOfColumns = tiles.length;
        int numOfRows = tiles[0].length;

        float tileWidth = 1.0f / numOfColumns;
        float tileHeight = 1.0f / numOfRows;

        // Take aspect_ratio into account
        if (tileWidth < tileHeight) {
            tileHeight = tileWidth * aspectRatio;
        } else {
            tileWidth = tileHeight / aspectRatio;
        }
        if (tileWidth * numOfColumns > 1) {
            tileHeight = tileHeight / (tileWidth * numOfColumns);
            tileWidth = 1.0f / numOfColumns;
        } else if (tileHeight * numOfRows > 1) {
            tileWidth = tileWidth / (tileHeight * numOfRows);
            tileHeight = 1.0f / numOfRows;
        }

        System.out.println("2Tile width: " + tileWidth + " Tile height: " + tileHeight);

        // Set tileWidth and tileHeight in the gameMap
        gameMap.setTileWidth(tileWidth);
        gameMap.setTileHeight(tileHeight);

        // Create Path entitiy
        List<Tile> enemyPath = gameMap.getPath();
        PathfindingComponent pathfindingComponent = new PathfindingComponent(enemyPath);
        Entity path = new Entity();
        path.addComponent(PathfindingComponent.class, pathfindingComponent);
        ECSManager.getInstance().addLocalEntity(path);

        for (int i = 0; i < numOfColumns; i++) {
            for (int j = numOfRows - 1; j >= 0; j--) {

                Entity tileEntity = new Entity();
                String tileTexture = gameMap.getTextureForTile(tiles[i][j]);

                Vector2 position = new Vector2(i * tileWidth, j * tileHeight);
                Vector2 size = new Vector2(tileWidth, tileHeight);
                SpriteComponent spriteComponent = new SpriteComponent(tileTexture.toString(), size);
                PositionComponent positionComponent = new PositionComponent(position, 1);
                TileComponent tileComponent = new TileComponent(tiles[i][j]); // Added

                
                tileEntity.addComponent(SpriteComponent.class, spriteComponent);
                tileEntity.addComponent(PositionComponent.class, positionComponent);
                tileEntity.addComponent(TileComponent.class, tileComponent); // Added
                ECSManager.getInstance().addLocalEntity(tileEntity);
                
            }
        }

    }

    private void handlePlayerAction(PlayerInput action) {
        // TODO: ADD PAUSE ACTION
        System.out.println("Action: " + action.getCardType() + " x: " + action.getX() + " y: " + action.getY());

        // TODO: Check for player balance for buying card
        
        handleCardPlacement(action.getCardType(), action.getX(), action.getY());
    }

    private void handleCardPlacement(CardType selectedCardType, int x, int y) {
        System.out.println("Clicked tile at position: (" + x + ", " + y + ")");
        Tile tile = gameMap.getMapLayout()[x][y];

        Entity tileEntity = getTileEntityByPosition(new Vector2(x, y));
        if (tileEntity == null)
            return; // Exit if there is no entity for this tile
        if (selectedCardType == null || !tile.isBuildable() || tile.hasTower()) {
            return;
        }
        

        // Get necessary entities for card placement
        Entity village = ECSManager.getInstance().getLocalEntities().stream()
            .filter(e -> e.getComponent(VillageComponent.class).isPresent()).findFirst().get();
        Entity cardEntity = CardFactory.createCard(selectedCardType, new Vector2(x, y), true);
        
        // Card already placed, place tower
        if (tile.hasCard()) {
            System.out.println("Placing tower on tile at position (" + x + ", " + y + ")");
            PlacedCardComponent existingCardComponent = ECSManager.getInstance()
                    .getOrDefaultComponentManager(PlacedCardComponent.class).getComponent(tileEntity).get();
            CardType existingCardType = existingCardComponent.cardType;
            Optional<TowerType> towerType = PairableCards.getTower(selectedCardType, existingCardType);

            if (towerType.isPresent()) {

                if (!buyCard(village, cardEntity)) {
                    System.out.println("Not enough money to buy card");
                    return;
                }

                // Remove the card thats already there
                ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class)
                        .removeComponent(tileEntity);
                Entity card = tile.getCard();
                ECSManager.getInstance().removeLocalEntity(card);
                tile.removeCard();

                // Create the tower entity
                Entity towerEntity = TowerFactory.createTower(selectedCardType, existingCardType, new Vector2(x, y));

                // Update the tile with the new tower
                updateTileWithTower(tile, tileEntity, towerEntity);
            }
        }
        // No card on tile, place card
        else {
            if (!buyCard(village, cardEntity)) {
                System.out.println("Not enough money to buy card");
                return;
            }
            System.out.println("Placing card on tile at position (" + x + ", " + y + ")");
            // Add a PlacedCardComponent the Tile-entity (to keep track of the cards' type)
            PlacedCardComponent placedCardComponent = new PlacedCardComponent(selectedCardType);
            ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class).addComponent(tileEntity,
                    placedCardComponent);

            // Update the tile with the new card
            updateTileWithCard(tile, tileEntity, cardEntity);
        }
    }

    private boolean buyCard(Entity village, Entity cardEntity) {
        ComponentManager<CostComponent> costComponentManager = ECSManager.getInstance().getOrDefaultComponentManager(CostComponent.class);
        Optional<CostComponent> costComponent = costComponentManager.getComponent(cardEntity);
        if (costComponent.isPresent()) {
            int playerBalance = village.getComponent(MoneyComponent.class).get().amount;
            int costOfCard = costComponent.get().getCost();
            if (playerBalance >= costOfCard) {
                playerBalance -= costOfCard;
                village.getComponent(MoneyComponent.class).get().amount = playerBalance;
                updateTopRightCornerText(village);
                return true;
            }
        }
        return false;
    }

    private void updateTopRightCornerText(Entity village) {
        // Get the text-component of the village and update the health
        ComponentManager<TextComponent> textManager = ECSManager.getInstance().getOrDefaultComponentManager(TextComponent.class);
        ComponentManager<MoneyComponent> moneyManager = ECSManager.getInstance().getOrDefaultComponentManager(MoneyComponent.class);
        ComponentManager<HealthComponent> healthManager = ECSManager.getInstance().getOrDefaultComponentManager(HealthComponent.class);
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

    private Entity getTileEntityByPosition(Vector2 tilePosition) {
        for (Entity entity : ECSManager.getInstance().getLocalEntities()) {
            if (entity.getComponent(TileComponent.class).isPresent()
                    && entity.getComponent(PositionComponent.class).isPresent()) {
                Tile tile = entity.getComponent(TileComponent.class).get().getTile();
                
                if (tile.getX() == (int) tilePosition.x && tile.getY() == (int) tilePosition.y) {
                    return entity;
                }
            }
        }
        return null;
    }

    private void updateTileWithTower(Tile tile, Entity tileEntity, Entity towerEntity) {
        if (tileEntity != null) {
            centerAndResizeEntity(towerEntity, tileEntity, gameMap);
            tile.setTower(towerEntity);

            ECSManager.getInstance().addLocalEntity(towerEntity);
        }
    }


    private void centerAndResizeEntity(Entity entityToPlace, Entity tileEntity, Map gameMap) {
        float padding = 0.05f; // 5% padding on each side

        float entityWidth = gameMap.getTileWidth();
        float entityHeight = gameMap.getTileHeight();
        if (entityToPlace.getComponent(PlacedCardComponent.class).isPresent()) {
            // Cards should be slightly smaller than towers
            entityWidth = gameMap.getTileWidth() * (1 - 2 * padding) * 0.9f;
            entityHeight = gameMap.getTileHeight() * (1 - 2 * padding) * 0.9f;
        }

        // Get the position of the tile (in UV-coordinates)
        PositionComponent tilePositionComponent = tileEntity.getComponent(PositionComponent.class).get();

        // Calculate the centered position for the card/tower within the tile
        Vector2 centeredPosition = new Vector2(
                tilePositionComponent.getPosition().x + padding * gameMap.getTileWidth(),
                tilePositionComponent.getPosition().y + padding * gameMap.getTileHeight() + entityHeight / 4);

        // Update the PositionComponent of the entity to place
        PositionComponent entityPositionComponent = entityToPlace.getComponent(PositionComponent.class).get();
        entityPositionComponent.position = centeredPosition;
        entityToPlace.addComponent(PositionComponent.class, entityPositionComponent);

        // Update the SpriteComponent of the entity to place
        SpriteComponent entitySpriteComponent = entityToPlace.getComponent(SpriteComponent.class).get();
        entitySpriteComponent.size_uv = new Vector2(entityWidth, entityHeight);
        entityToPlace.addComponent(SpriteComponent.class, entitySpriteComponent);
    }
    
    private void updateTileWithCard(Tile tile, Entity tileEntity, Entity cardEntity) {
        if (tileEntity != null) {
            centerAndResizeEntity(cardEntity, tileEntity, gameMap);
            tile.setCard(cardEntity);

            ECSManager.getInstance().addLocalEntity(cardEntity);
        }
    }

    public void initializeVillage(GameState gameState) {

        MoneyComponent moneyComponent = ECSManager.getInstance().getOrDefaultComponentManager(MoneyComponent.class).getComponent(gameState.playerOne).get();
        HealthComponent healthComponent = new HealthComponent(1000);
        PositionComponent villagePosition = new PositionComponent(new Vector2(0.80f, 0.90f), 1000);
        String textToDisplay = "Health: " + healthComponent.getHealth() + "\n Money: " + moneyComponent.getAmount();
        TextComponent villageHealthText = new TextComponent(textToDisplay, new Vector2(0.05f, 0.05f));

        villageHealthText.setColor(new Vector3(0f, 0f, 0f));
        gameState.playerOne.addComponent(HealthComponent.class, healthComponent);
        gameState.playerOne.addComponent(PositionComponent.class, villagePosition);
        gameState.playerOne.addComponent(TextComponent.class, villageHealthText);

        ECSManager.getInstance().addLocalEntity(gameState.playerOne);
    }

    public void setPlayerId(UUID playerId) {
        this.playerOneID = playerId;
    }

    public UUID getGameId() {
        return gameId;
    }
}
