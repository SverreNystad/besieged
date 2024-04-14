package com.softwarearchitecture.game_client.states;

import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.game_server.GameServer;
import com.softwarearchitecture.math.Vector2;

public class Lobby extends State implements Observer {
    Thread serverThread = null;
    boolean isHost;

    public Lobby(Controllers defaultControllers, boolean isHost, UUID yourId) {
        super(defaultControllers, yourId);
        this.isHost = isHost;
    }

    @Override
    protected void activate() {
        String backgroundPath = TexturePack.BACKGROUND_ABYSS;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0, 0), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        ECSManager.getInstance().addEntity(background);
        TextComponent textComponent = new TextComponent("Lobby!", new Vector2(0.05f, 0.05f));
        background.addComponent(TextComponent.class, textComponent);
        // Buttons
        ButtonFactory.createAndAddButtonEntity(ButtonEnum.BACK, new Vector2(0, 1),
                new Vector2(0.1f, 0.2f), this, 0);

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);

        // Instantiate GameServer on a new thread
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
            serverThread = null;
        }
        if (isHost) {
            Runnable run_server = () -> {
                GameServer server = new GameServer(defaultControllers.serverMessagingController, yourId);
                server.run();
            };
            serverThread = new Thread(run_server);
            serverThread.start();
        }
    }

    @Override
    public void onAction(ButtonEnum type) {
        switch (type) {
            case BACK:
                ScreenManager.getInstance().previousState();
                break;
            default:
                break;
        }
    }

}
