package com.softwarearchitecture.launcher;

import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.graphics.LibGDXGraphics;
import com.softwarearchitecture.sound.LibGDXSound;

public class GameLauncher {
    /**
     * Create a new game client.
     */
    public static GameClient createGameClient() {
        GraphicsController graphicsController = new LibGDXGraphics();
        SoundController audioController = new LibGDXSound();
		GameClient gameClient = new GameClient(graphicsController);
        gameClient.init();
        return gameClient;
    }
}
