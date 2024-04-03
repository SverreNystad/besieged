package com.softwarearchitecture.launcher;

import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.graphics.LibGDXGraphics;

public class GameLauncher {
    /**
     * Start the game.
     */
    public static void startGame() {
        GraphicsController graphicsController = new LibGDXGraphics();
		GameClient gameClient = new GameClient(graphicsController);
        gameClient.run();
    }
}
