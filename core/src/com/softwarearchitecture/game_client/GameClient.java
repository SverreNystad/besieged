package com.softwarearchitecture.game_client;

import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.game_client.states.MainMenu;

public class GameClient {
    // private State currentState;
    private GraphicsController graphicsController;

    public GameClient(GraphicsController graphicsController) throws IllegalArgumentException {
        // currentState = new MainMenu();
        if (graphicsController == null) {
            throw new IllegalArgumentException("Graphics controller cannot be null.");
        }
        this.graphicsController = graphicsController;
    }

    public void init() {
        // currentState.init(graphicsController);
    }

    public void update() {
        float deltaTime = 1f; // TODO: Implement deltatime
        // currentState.update(deltaTime);
    }
}
