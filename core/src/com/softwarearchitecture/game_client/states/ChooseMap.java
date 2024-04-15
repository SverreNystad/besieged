package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.math.Vector2;

// import systems
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;

public class ChooseMap extends State implements Observer {

    private boolean isHost;
    /**
     * Generic state is a state that can be used for multiple purposes
     * for different types of menus.
     * 
     * parameters: type: GenericStateType, wich is an enum that defines
     * the use of the state
     */
    public ChooseMap(Controllers defaultControllers, UUID yourId, boolean isMultiplayer) {
        super(defaultControllers, yourId);
        isHost = isMultiplayer;
    }

    @Override
    protected void activate() {
        // Set background image
        String backgroundPath = TexturePack.BACKGROUND_MULTIPLAYER;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0f, 0f), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        ECSManager.getInstance().addEntity(background);
        System.out.println("Menu activated");

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);

        // Add the choose map
        Entity logo = new Entity();
        SpriteComponent logoSprite = new SpriteComponent(TexturePack.CHOOSE_MAP, new Vector2(0.35f, 0.06f));
        PositionComponent logoPosition = new PositionComponent(new Vector2(0.5f - 0.35f / 2, 0.75f), 1);
        logo.addComponent(SpriteComponent.class, logoSprite);
        logo.addComponent(PositionComponent.class, logoPosition);
        ECSManager.getInstance().addEntity(logo);

        // Set up the UI elements
        List<Entity> buttons = new ArrayList<>();
        float buttonWidth = 0.25f;
        float buttonHeight = 0.3f;
        float gap = 0.02f;
        float translateY = 0.1f;

        // TODO: Make map fetching dynamic

        // Create button rectangles
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.ABYSS,
                new Vector2(0.5f - buttonWidth - 0.01f, translateY + 0.25f),
                new Vector2(buttonWidth, buttonHeight), this, 2));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.TEST,
                new Vector2(0.5f + 0.01f, translateY + 0.25f),
                new Vector2(buttonWidth, buttonHeight), this, 2));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.BACK,
                new Vector2(0.495f - 0.35f / 2, translateY),
                new Vector2(0.35f, 0.1f), this, 2));
    }

    /**
     * Handles button actions based on the type of the button.
     * This state is only the intermediary menus that traverses to other states.
     * 
     * @param type: ButtonType.
     */
    @Override
    public void onAction(ButtonEnum type) {
        // Switches the state of the game based on the button type

        // TODO: MAKE BUTTON ACTIONS DYNAMIC CHOOSING MAPS
        String map = "";
        switch (type) {
            case ABYSS:
                System.out.println("Abyss map button pressed");
                map = "abyss";
                break;

            case TEST:
                System.out.println("Test map button pressed");
                map = "test";
                break;

            case BACK:
                System.out.println("Back button pressed");
                screenManager.nextState(new Multiplayer(defaultControllers, yourId));
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");
        }
        if (isHost) {
            startServer();
        }

        screenManager.nextState(new InGame(defaultControllers, yourId, map));
    }

    private void startServer() {
        // Start the server
        defaultControllers.gameServer.run();
    }

}
