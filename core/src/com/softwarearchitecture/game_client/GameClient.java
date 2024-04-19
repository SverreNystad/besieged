package com.softwarearchitecture.game_client;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.game_client.states.InGame;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.ScreenManager;
import com.softwarearchitecture.game_server.GameState;

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
        if (screenManager.getGameId() != null && screenManager.isCurrentStateOfType(InGame.class)) {
            gameId = screenManager.getGameId();
            Optional<GameState> game = defaultControllers.onlineClientMessagingController.requestGameState(gameId);
            if (game.isPresent()) {
                game.get();
            }
        }
        else if (defaultControllers.gameServer.getGameId() != null  && screenManager.isCurrentStateOfType(InGame.class)) {
            gameId = defaultControllers.gameServer.getGameId();
            Optional<GameState> game = Optional.empty();
            game = screenManager.isLocalServer() ? defaultControllers.localClientMessagingController.requestGameState(gameId) : defaultControllers.onlineClientMessagingController.requestGameState(gameId);
            
            if (game.isPresent()) {
                game.get();
            }
        }
    }
}