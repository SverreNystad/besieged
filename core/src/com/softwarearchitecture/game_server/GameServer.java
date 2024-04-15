package com.softwarearchitecture.game_server;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlayerComponent;

public class GameServer {
    private UUID gameId;
    private UUID playerOneID;
    private ServerMessagingController messageController;

    public GameServer(ServerMessagingController messageController, UUID playerOneID) {
        this.messageController = messageController;
        this.playerOneID = playerOneID;
    }

    public void run() {
        this.gameId = messageController.createGame();
        System.out.println("Game created with ID: " + gameId);
        GameState gameState = messageController.getGameState(gameId);
        if (messageController.getGameState(gameId).playerOne == null) {
            gameState.playerOne = new Entity();
            gameState.playerOne.addComponent(PlayerComponent.class, new PlayerComponent(playerOneID));
            ECSManager.getInstance().addEntity(gameState.playerOne);
        }
        messageController.setNewGameState(this.gameId, gameState);

        // Wait for player two to join
        while (messageController.getGameState(gameId).playerTwo == null) {
            Optional<UUID> playerTwo = messageController.lookForPendingPlayer(gameId);
            System.out.println("[INFO] Looking for player two");
            if (!playerTwo.isPresent())
                continue;
            gameState.playerTwo = new Entity();
            gameState.playerTwo.addComponent(PlayerComponent.class, new PlayerComponent(playerTwo.get()));
            ECSManager.getInstance().addEntity(gameState.playerTwo);

            messageController.setNewGameState(this.gameId, gameState);
            System.out.println("[INFO] Player two has joined the game");
            break;

        }
        System.out.println("[INFO] Game is now full");

    }

    public void setPlayerId(UUID playerId) {
        this.playerOneID = playerId;
    }
}
