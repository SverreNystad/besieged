package com.softwarearchitecture.game_client.states;

import java.util.List;
import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.math.Vector2;

public abstract class State {
    protected ScreenManager screenManager;
    protected Vector2 mouse = new Vector2(0, 0);
    protected List<Entity> buttons;
    protected Controllers defaultControllers;
    protected UUID yourId;

    protected State(Controllers defaultControllers, UUID yourId) {
        this.defaultControllers = defaultControllers;
        this.screenManager = ScreenManager.getInstance();
        this.yourId = yourId;
    }

    /**
     * Initializes the state, clearing the ECSManager and activating the state.
     */
    public void init() {
        ECSManager.getInstance().clearAll();
        activate();
    }
    
    /**
     * Activates the state, setting up the ECSManager and the buttons.
     */
    protected abstract void activate();
 
}
