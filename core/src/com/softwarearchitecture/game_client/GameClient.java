package com.softwarearchitecture.game_client;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.clock.Clock;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.game_client.states.GameOver;
import com.softwarearchitecture.game_client.states.InGame;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.ScreenManager;
import com.softwarearchitecture.game_server.GameState;

public class GameClient {
    private ScreenManager screenManager;
    private Controllers defaultControllers;

    private long maxServerResponseTime = 10_000;
    private long lastServerResponse;

    public GameClient(Controllers defaultControllers, UUID yourId) throws IllegalArgumentException {
        this.defaultControllers = defaultControllers;
        screenManager = ScreenManager.getInstance();
        screenManager.nextState(new Menu(defaultControllers, yourId));

    }

    public void update() {
        screenManager.activateCurrentStateIfChanged();

        float deltaTime = Clock.getInstance().getAndResetDeltaTime();

        ECSManager.getInstance().update(deltaTime);

        // Check if the player is in a multiplayer game
        UUID gameId = null;
        if (screenManager.getGameId() != null && screenManager.isCurrentStateOfType(InGame.class)) {
            gameId = screenManager.getGameId();
            Optional<GameState> game = defaultControllers.onlineClientMessagingController.requestGameState(gameId);
            if (game.isPresent()) {
                if (this.lastServerResponse == 0) {
                    this.lastServerResponse = game.get().timeStamp;
                }
                GameState state = game.get();

                // Check for server heartbeat
                if (System.currentTimeMillis() - this.lastServerResponse >= this.maxServerResponseTime) {
                    screenManager.nextState(new GameOver(defaultControllers, gameId, "LOST CONNECTION"));
                }
                // No update
                if (state.timeStamp != this.lastServerResponse) {
                    this.lastServerResponse = state.timeStamp;
                }
            }
        } else if (defaultControllers.gameServer.getGameId() != null
                && screenManager.isCurrentStateOfType(InGame.class)) {
            gameId = defaultControllers.gameServer.getGameId();
            Optional<GameState> game = Optional.empty();
            game = screenManager.isLocalServer()
                    ? defaultControllers.localClientMessagingController.requestGameState(gameId)
                    : defaultControllers.onlineClientMessagingController.requestGameState(gameId);

            if (game.isPresent()) {
                game.get();
            }
        }
    }
}