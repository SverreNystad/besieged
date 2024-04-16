package com.softwarearchitecture.game_server;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlayerComponent;

/**
 * Represents a game server that manages the lifecycle and state of a multiplayer game.
 * The server coordinates game creation, state updates, player actions, the main game loop and teardown after game over.
 */
public class GameServer {
    private UUID gameId;
    private UUID playerOneID;
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
        waitForPlayerToJoin(gameState);

        // TODO: Add relevant systems

        // Main gameplay loop
        boolean gamesOver = false;
        while (!gamesOver) {

            float deltatime = 1.0f;
            ECSManager.getInstance().update(deltatime);
            
            // Process each pending player action.
            for (PlayerInput action : messageController.lookForPendingActions(gameId)) {
                // Actions to process player inputs and update game state
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
        System.out.println("Game created with ID: " + gameId);
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
    private void waitForPlayerToJoin(GameState gameState) {
        while (messageController.getGameState(gameId).playerTwo == null) {
            Optional<UUID> playerTwo = messageController.lookForPendingPlayer(gameId);
            System.out.println("[INFO] Looking for player two");
            if (!playerTwo.isPresent())
            continue;
            System.out.println("[INFO] The player two joined with: " + playerTwo.get().toString());
            gameState.playerTwo = new Entity();
            gameState.playerTwo.addComponent(PlayerComponent.class, new PlayerComponent(playerTwo.get()));
            ECSManager.getInstance().addEntity(gameState.playerTwo);
            
            messageController.setNewGameState(this.gameId, gameState);
            System.out.println("[INFO] Player two has joined the game");
            System.out.println("[INFO] Game is now full");
            break;
        }
    }

 
    public void setPlayerId(UUID playerId) {
        this.playerOneID = playerId;
    }

    public UUID getGameId() {
        return gameId;
    }
}
