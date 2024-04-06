package com.softwarearchitecture.launcher;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.systems.AudioSystem;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.graphics.LibGDXGraphics;
import com.softwarearchitecture.input.LibGDXInput;
import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.graphics.LibGDXGraphics;
import com.softwarearchitecture.sound.LibGDXSound;

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

        // Set up audio-system
        // TODO: Get volume from settings
        float settingsVolume = 0.5f;
        SoundController audioController = new LibGDXSound(settingsVolume);
        ComponentManager<SoundComponent> audioManager = ECSManager.getInstance().getOrDefaultComponentManager(SoundComponent.class);
        AudioSystem audioSystem = new AudioSystem(audioManager, audioController);
        ECSManager.getInstance().addSystem(audioSystem);
        // Initiate client
        GameClient gameClient = new GameClient(graphicsController, inputSystem);
        gameClient.run();
    }
}
