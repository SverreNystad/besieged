package com.softwarearchitecture.game_server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.systems.AttackSystem;
import com.softwarearchitecture.ecs.systems.EnemySystem;
import com.softwarearchitecture.ecs.systems.MovementSystem;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.PairableCards.TowerType;
import com.softwarearchitecture.math.Vector2;

/**
 * Represents a game server that manages the lifecycle and state of a multiplayer game.
 * The server coordinates game creation, state updates, player actions, the main game loop and teardown after game over.
 */
public class GameServer {
    private UUID gameId;
    private UUID playerOneID;
    private UUID playerTwoID;
    private ServerMessagingController messageController;

    private Map gameMap;

    /**
     * Initializes a new game server with a message controller for communication and the UUID of player one.
     * @param messageController The communication controller used for interacting with clients.
     * @param playerOneID The unique identifier for the first player in the game.
     */
    public GameServer(ServerMessagingController messageController, UUID playerOneID) {
        this.messageController = messageController;
        this.playerOneID = playerOneID;
    }

    /**
     * Starts the server operation, creating a game instance and entering the main gameplay loop.
     * The method handles game setup, player joining, and periodic state updates until the game ends.
     */
    public void run(String mapName) {
        GameState gameState = hostGame(mapName);

        // Wait for player two to join
        playerTwoID = waitForPlayerToJoin(gameState);

        // TODO: Add relevant entities
        Entity village = new Entity();
        village.addComponent(HealthComponent.class, new HealthComponent(100));
        ECSManager.getInstance().addEntity(village);
        gameState.village = village;
        
        gameState.playerTwo = new Entity();
        gameState.playerTwo.addComponent(PlayerComponent.class, new PlayerComponent(playerTwoID));
        ECSManager.getInstance().addEntity(gameState.playerTwo);
        
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
        setupGame(mapName);

        // Main gameplay loop
        boolean gamesOver = false;
        while (!gamesOver) {

            float deltatime = 1.0f;
            ECSManager.getInstance().update(deltatime);
            
            // Process each pending player action.
            for (PlayerInput action : messageController.lookForPendingActions(playerOneID)) {
                // Actions to process player inputs and update game state
                System.out.println("[SERVER] Processing player one action: " + action.getAction());
                handlePlayerAction(action);
            }
            for (PlayerInput action : messageController.lookForPendingActions(playerTwoID)) {
                // Actions to process player inputs and update game state
                System.out.println("[SERVER] Processing player two action: " + action.getAction());
                handlePlayerAction(action);
            }


            // Update all clients with the latest game state.

            messageController.setNewGameState(gameId, gameState);

            // TODO: check if game is over
        }

        // Teardown of server and delete game from games listing
        messageController.removeGame(gameId);
    }

    private GameState hostGame(String mapName) {
        this.gameId = messageController.createGame(mapName);
        System.out.println("[SERVER] Game created with ID: " + gameId);
        GameState gameState = messageController.getGameState(gameId);
        if (messageController.getGameState(gameId).playerOne == null) {
            gameState.playerOne = new Entity();
            gameState.playerOne.addComponent(PlayerComponent.class, new PlayerComponent(playerOneID));
            ECSManager.getInstance().addEntity(gameState.playerOne);
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
        while (messageController.getGameState(gameId).playerTwo == null) {
            Optional<UUID> playerTwo = messageController.lookForPendingPlayer(gameId);
            System.out.println("[SERVER] Looking for player two");
            if (!playerTwo.isPresent())
                continue;
            playerTwoID = playerTwo.get();
            System.out.println("[SERVER] The player two joined with: " + playerTwo.get().toString());
            break;
        }
        return playerTwoID;
    }


    private void setupGame(String mapName) {
        String backgroundPath = TexturePack.BACKGROUND_TOR;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0, 0), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        TextComponent textComponent = new TextComponent("In Game!", new Vector2(0.05f, 0.05f));
        background.addComponent(TextComponent.class, textComponent);
        ECSManager.getInstance().addEntity(background);

        // Map and tiles
        this.gameMap = MapFactory.createMap(mapName);
        initializeMapEntities(gameMap);
    

        // Add systems to the ECSManager
        MovementSystem MovementSystem = new MovementSystem();
        EnemySystem EnemySystem = new EnemySystem();
        AttackSystem attackSystem = new AttackSystem(gameMap);

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

        // Set tileWidth and tileHeight in the gameMap
        gameMap.setTileWidth(tileWidth);
        gameMap.setTileHeight(tileHeight);

        // Create Path entitiy
        List<Tile> enemyPath = gameMap.getPath();
        PathfindingComponent pathfindingComponent = new PathfindingComponent(enemyPath);
        Entity path = new Entity();
        path.addComponent(PathfindingComponent.class, pathfindingComponent);
        ECSManager.getInstance().addEntity(path);

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
                ECSManager.getInstance().addEntity(tileEntity);

            }
        }

    }

    private void handlePlayerAction(PlayerInput action) {
        // TODO: ADD PAUSE ACTION

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
        

        // Card already placed, place tower
        if (tile.hasCard()) {
            System.out.println("Placing tower on tile at position (" + x + ", " + y + ")");
            PlacedCardComponent existingCardComponent = ECSManager.getInstance()
                    .getOrDefaultComponentManager(PlacedCardComponent.class).getComponent(tileEntity).get();
            CardType existingCardType = existingCardComponent.cardType;
            Optional<TowerType> towerType = PairableCards.getTower(selectedCardType, existingCardType);

            if (towerType.isPresent()) {
                // Remove the card thats already there
                ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class)
                        .removeComponent(tileEntity);
                Entity card = tile.getCard();
                ECSManager.getInstance().removeEntity(card);
                tile.removeCard();

                // Create the tower entity
                Entity towerEntity = TowerFactory.createTower(selectedCardType, existingCardType, new Vector2(x, y));

                // Update the tile with the new tower
                updateTileWithTower(tile, tileEntity, towerEntity);
            }
        }
        // No card on tile, place card
        else {
            System.out.println("Placing card on tile at position (" + x + ", " + y + ")");
            // Add a PlacedCardComponent the Tile-entity (to keep track of the cards' type)
            PlacedCardComponent placedCardComponent = new PlacedCardComponent(selectedCardType);
            ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class).addComponent(tileEntity,
                    placedCardComponent);

            // Create the card entity
            Entity cardEntity = CardFactory.createCard(selectedCardType, new Vector2(x, y), true);

            // Update the tile with the new card
            updateTileWithCard(tile, tileEntity, cardEntity);
        }
    }

    private Entity getTileEntityByPosition(Vector2 tilePosition) {
        float tileWidth = gameMap.getTileWidth();
        float tileHeight = gameMap.getTileHeight();

        for (Entity entity : ECSManager.getInstance().getEntities()) {
            if (entity.getComponent(TileComponent.class).isPresent()
                    && entity.getComponent(PositionComponent.class).isPresent()) {
                PositionComponent positionComponent = entity.getComponent(PositionComponent.class).get();
                // Convert the UV coordinates back to XY coordinates
                int xCoord = (int) (positionComponent.getPosition().x / tileWidth);
                int yCoord = (int) (positionComponent.getPosition().y / tileHeight);

                if (xCoord == (int) tilePosition.x && yCoord == tilePosition.y) {
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

            ECSManager.getInstance().addEntity(towerEntity);
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

            ECSManager.getInstance().addEntity(cardEntity);
        }
    }

    public void setPlayerId(UUID playerId) {
        this.playerOneID = playerId;
    }

    public UUID getGameId() {
        return gameId;
    }
}
