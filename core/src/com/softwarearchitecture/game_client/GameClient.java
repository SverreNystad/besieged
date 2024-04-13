package com.softwarearchitecture.game_client;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Controllers;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.MenuEnum;
import com.softwarearchitecture.game_client.states.ScreenManager;

public class GameClient {
    private Controllers defaultControllers;
    private ScreenManager screenManager;
    private static Process serverProcess = null;
    private UUID yourId;

    public GameClient(Controllers defaultControllers) throws IllegalArgumentException {
        this.defaultControllers = defaultControllers;
        yourId = UUID.randomUUID();
        
        screenManager = ScreenManager.getInstance();
        screenManager.nextState(new Menu(MenuEnum.MENU, defaultControllers, yourId));

    }
    

    public void init() {
        // currentState.init(graphicsController);
    }

    public void update() {
        screenManager.activateCurrentStateIfChanged();

        float deltaTime = 1f; // TODO: Implement deltatime

        ECSManager.getInstance().update(deltaTime);
    }
}
