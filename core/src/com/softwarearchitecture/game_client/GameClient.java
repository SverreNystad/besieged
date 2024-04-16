package com.softwarearchitecture.game_client;

import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.ScreenManager;

public class GameClient {
    private ScreenManager screenManager;
    private Controllers defaultControllers;

    public GameClient(Controllers defaultControllers, UUID yourId) throws IllegalArgumentException {
        this.defaultControllers = defaultControllers;
        screenManager = ScreenManager.getInstance();
        screenManager.nextState(new Menu(defaultControllers, yourId));

    }

    public void update() {
        screenManager.activateCurrentStateIfChanged();

        float deltaTime = 1f; // TODO: Implement deltatime

        ECSManager.getInstance().update(deltaTime);

        // Check if the player is in a multiplayer game
        UUID gameId = null;
        if (screenManager.getGameId() != null) {
            gameId = screenManager.getGameId();
            defaultControllers.clientMessagingController.requestGameState(gameId);
        }
        if (defaultControllers.gameServer.getGameId() != null) {
            gameId = defaultControllers.gameServer.getGameId();
            defaultControllers.clientMessagingController.requestGameState(gameId);
        }
    }
}