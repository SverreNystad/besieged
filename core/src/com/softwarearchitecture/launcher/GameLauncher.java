package com.softwarearchitecture.launcher;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.softwarearchitecture.ecs.Controllers;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.graphics.LibGDXGraphics;
import com.softwarearchitecture.input.LibGDXInput;
import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.sound.LibGDXSound;

public class GameLauncher {
    /**
     * Create a new game client.
     */
    public static GameClient createGameClient(OrthographicCamera camera, Viewport viewport) {
        LibGDXInput libGDXInput = new LibGDXInput();
        GraphicsController graphicsController = new LibGDXGraphics(camera, viewport);
        SoundController soundController = new LibGDXSound(0.5f);

        // Set to main manu
        Controllers defaultControllers = new Controllers(graphicsController, libGDXInput, soundController);

        return new GameClient(defaultControllers);
    }
}
