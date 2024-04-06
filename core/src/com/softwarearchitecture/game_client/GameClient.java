package com.softwarearchitecture.game_client;

import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.game_client.states.MainMenu;

public class GameClient {
    private State currentState;
    private GraphicsController graphicsController;
    private InputSystem inputSystem;

    public GameClient(GraphicsController graphicsController, InputSystem inputSystem) throws IllegalArgumentException {
        this.graphicsController = graphicsController;
        this.inputSystem = inputSystem;
        
        if (graphicsController == null) {
            System.out.println("Graphics controller cannot be null.");
            throw new IllegalArgumentException("Graphics controller cannot be null.");
        }
        
        currentState = new MainMenu();
    }

    public void run() {
        // Adjustments to run method should be made according to LibGDX lifecycle, 
        // Input handling is done asynchronously via LibGDXInput.
    }
}
