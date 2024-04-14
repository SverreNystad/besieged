package com.softwarearchitecture.game_client;

import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.ScreenManager;

public class GameClient {
    private ScreenManager screenManager;
    private UUID yourId;

    public GameClient(Controllers defaultControllers) throws IllegalArgumentException {
        yourId = UUID.randomUUID();

        screenManager = ScreenManager.getInstance();
        screenManager.nextState(new Menu(defaultControllers, yourId));

    }

    public void update() {
        screenManager.activateCurrentStateIfChanged();

        float deltaTime = 1f; // TODO: Implement deltatime

        ECSManager.getInstance().update(deltaTime);
    }
}