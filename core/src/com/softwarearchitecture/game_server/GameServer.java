package com.softwarearchitecture.game_server;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.ecs.ComponentManager;
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
        GameState gameState = messageController.getGameState(gameId);
        if (messageController.getGameState(gameId).playerOne == null) {
            gameState.playerOne = new Entity();
            gameState.playerOne.addComponent(PlayerComponent.class, new PlayerComponent(playerOneID));
            ECSManager.getInstance().addEntity(gameState.playerOne);
        }
        messageController.setNewGameState(this.gameId, gameState);

        while(true) {
            if (messageController.getGameState(gameId).playerTwo == null) {
                Optional<UUID> playerTwo = messageController.lookForPendingPlayer(gameId);
                if (playerTwo.isEmpty()) continue;

                gameState.playerTwo = new Entity();
                gameState.playerTwo.addComponent(PlayerComponent.class, new PlayerComponent(playerTwo.get()));
                ECSManager.getInstance().addEntity(gameState.playerTwo);

                messageController.setNewGameState(this.gameId, gameState);
            } else continue;

            
        }
    }

}
