package com.softwarearchitecture.game_client;

import java.util.List;
import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.ScreenManager;
import com.softwarearchitecture.game_server.GameState;

public class GameClient {
    private ScreenManager screenManager;
    private UUID yourId;

    public GameClient(Controllers defaultControllers) throws IllegalArgumentException {
        yourId = UUID.randomUUID();

        List<GameState> games = defaultControllers.clientMessagingController.getAllAvailableGames();
        System.out.println("Available games: " + games);
        screenManager = ScreenManager.getInstance();
        screenManager.nextState(new Menu(defaultControllers, yourId));

    }

    public void update() {
        screenManager.activateCurrentStateIfChanged();

        float deltaTime = 1f; // TODO: Implement deltatime

        ECSManager.getInstance().update(deltaTime);
    }
}
