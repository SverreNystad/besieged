package com.softwarearchitecture.launcher;

import java.util.UUID;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.game_client.ClientMessagingController;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.game_server.GameServer;
import com.softwarearchitecture.graphics.LibGDXGraphics;
import com.softwarearchitecture.input.LibGDXInput;
import com.softwarearchitecture.networking.messaging.ClientMessenger;
import com.softwarearchitecture.networking.messaging.ServerMessenger;
import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.sound.LibGDXSound;

public class GameLauncher {
    /**
     * Create a new game client.
     */
    public static GameClient createGameClient(OrthographicCamera camera, Viewport viewport) {
        LibGDXInput libGDXInput = new LibGDXInput(viewport);
        GraphicsController graphicsController = new LibGDXGraphics(camera, viewport);
        SoundController soundController = new LibGDXSound(50);
        ServerMessenger onlineServerMessenger = new ServerMessenger(true);
        ServerMessenger localServerMessenger = new ServerMessenger(false);
        ClientMessagingController onlineClientMessaging = new ClientMessenger(true);
        ClientMessagingController localClientMessaging = new ClientMessenger(false);

        // Set to main manu
        UUID yourId = UUID.randomUUID();
        GameServer gameServer = new GameServer(onlineServerMessenger, localServerMessenger, yourId, graphicsController.getAspectRatio());
        Controllers defaultControllers = new Controllers(graphicsController, libGDXInput, soundController, onlineServerMessenger, onlineClientMessaging, localClientMessaging, gameServer);
        return new GameClient(defaultControllers, yourId);
    }
}
