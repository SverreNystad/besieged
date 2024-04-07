package com.softwarearchitecture.launcher;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.systems.AudioSystem;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.MenuEnum;
import com.softwarearchitecture.game_client.states.ScreenManager;
import com.softwarearchitecture.graphics.LibGDXGraphics;
import com.softwarearchitecture.input.LibGDXInput;
import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.ecs.components.ButtonComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.sound.LibGDXSound;

public class GameLauncher {
        /**
         * Create a new game client.
         */
        public static GameClient createGameClient() {

                ComponentManager<ButtonComponent> buttonManager = ECSManager.getInstance()
                                .getOrDefaultComponentManager(ButtonComponent.class);
                LibGDXInput libGDXInput = new LibGDXInput();
                InputSystem inputSystem = new InputSystem(buttonManager, libGDXInput);
                ECSManager.getInstance().addSystem(inputSystem);

                // Set to main manu
                ScreenManager screenManager = ScreenManager.getInstance();
                screenManager.nextState(new Menu(MenuEnum.MENU));

                // Set up input-system
                libGDXInput.onTouch(touchLocation -> inputSystem.onTouch(touchLocation));
                libGDXInput.onRelease(touchLocation -> inputSystem.onRelease(touchLocation));

                // Set up graphics-system
                ComponentManager<SpriteComponent> spriComponentManager = ECSManager.getInstance()
                                .getOrDefaultComponentManager(SpriteComponent.class);
                ComponentManager<PositionComponent> positionComponentManager = ECSManager.getInstance()
                                .getOrDefaultComponentManager(PositionComponent.class);
                GraphicsController graphicsController = new LibGDXGraphics();
                RenderingSystem renderingSystem = new RenderingSystem(spriComponentManager, positionComponentManager,
                                graphicsController);
                ECSManager.getInstance().addSystem(renderingSystem);

                // Set up audio-system
                // TODO: Get volume from settings
                float settingsVolume = 0.5f;
                SoundController audioController = new LibGDXSound(settingsVolume);
                ComponentManager<SoundComponent> audioManager = ECSManager.getInstance()
                                .getOrDefaultComponentManager(SoundComponent.class);
                AudioSystem audioSystem = new AudioSystem(audioManager, audioController);
                ECSManager.getInstance().addSystem(audioSystem);
                // Initiate client
                GameClient gameClient = new GameClient(graphicsController, inputSystem);
                // gameClient.run();
                ECSManager.getInstance().update(1);

                gameClient.init();
                return gameClient;
        }
}
