package com.softwarearchitecture.game_client;

import java.util.Optional;

import com.softwarearchitecture.ecs.Controllers;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.MenuEnum;
import com.softwarearchitecture.game_client.states.ScreenManager;
import com.softwarearchitecture.game_client.states.State;

public class GameClient {
    // private State currentState;
    private Controllers defaultControllers;
    private ScreenManager screenManager;

    public GameClient(Controllers defaultControllers) throws IllegalArgumentException {
        this.defaultControllers = defaultControllers;

        screenManager = ScreenManager.getInstance();
        screenManager.nextState(new Menu(MenuEnum.MENU, defaultControllers));
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
