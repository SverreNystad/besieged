package com.softwarearchitecture.game_client;

import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.systems.InputSystem;

public class GameClient {
    // private State currentState;
    private GraphicsController graphicsController;
    private InputSystem inputSystem;

    public GameClient(GraphicsController graphicsController, InputSystem inputSystem) throws IllegalArgumentException {
        this.graphicsController = graphicsController;
        this.inputSystem = inputSystem;
        
        // currentState = new MainMenu();
    }

    public void init() {
        // currentState.init(graphicsController);
    }

    public void update() {
        float deltaTime = 1f; // TODO: Implement deltatime
        // currentState.update(deltaTime);
    }
}
