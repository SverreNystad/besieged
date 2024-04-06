package com.softwarearchitecture.launcher;

import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.graphics.LibGDXGraphics;
import com.softwarearchitecture.input.LibGDXInput;

public class GameLauncher {
    /**
     * Start the game.
     */
    public static void startGame() {
        GraphicsController graphicsController = new LibGDXGraphics();
        InputSystem inputSystem = new InputSystem();
        LibGDXInput libGDXInput = new LibGDXInput();

        // Set up input-system
        libGDXInput.onTouch(touchLocation -> inputSystem.onTouch(touchLocation));
        libGDXInput.onRelease(touchLocation -> inputSystem.onRelease(touchLocation));
        
        GameClient gameClient = new GameClient(graphicsController, inputSystem);
        gameClient.run();
    }
}
