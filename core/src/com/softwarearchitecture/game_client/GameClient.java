package com.softwarearchitecture.game_client;

import java.util.HashMap;
import java.util.Optional;

import com.softwarearchitecture.ecs.Controllers;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.MenuEnum;
import com.softwarearchitecture.game_client.states.ScreenManager;
import com.softwarearchitecture.game_client.states.State;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.TouchLocation;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.game_server.MapFactory;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TowerFactory;
import com.softwarearchitecture.math.Vector2;

public class GameClient {
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
