package com.softwarearchitecture.game_server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.systems.AttackSystem;
import com.softwarearchitecture.ecs.systems.EnemySystem;
import com.softwarearchitecture.ecs.systems.MovementSystem;
import com.softwarearchitecture.game_client.TexturePack;
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
            for (PlayerInput action : messageController.lookForPendingActions(gameId)) {
                // Actions to process player inputs and update game state
                System.out.println("[SERVER] Processing player action: " + action.getAction());
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
        Map gameMap = MapFactory.createMap(mapName);
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

    public void setPlayerId(UUID playerId) {
        this.playerOneID = playerId;
    }

    public UUID getGameId() {
        return gameId;
    }
}
