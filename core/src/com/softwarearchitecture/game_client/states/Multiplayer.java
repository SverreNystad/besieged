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

public class Multiplayer extends State implements Observer {

    /**
     * Generic state is a state that can be used for multiple purposes
     * for different types of menus.
     * 
     * parameters: type: GenericStateType, wich is an enum that defines
     * the use of the state
     */
    public Multiplayer(Controllers defaultControllers, UUID yourId) {
        super(defaultControllers, yourId);
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
        ECSManager.getInstance().addLocalEntity(background);
        System.out.println("Menu activated");

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);

        // Add the sign in button
        Entity logo = new Entity();
        SpriteComponent logoSprite = new SpriteComponent(TexturePack.SIGN, new Vector2(0.5f, 1f));
        PositionComponent logoPosition = new PositionComponent(new Vector2(0.5f - 0.25f, 0f), 1);
        logo.addComponent(SpriteComponent.class, logoSprite);
        logo.addComponent(PositionComponent.class, logoPosition);
        ECSManager.getInstance().addLocalEntity(logo);

        // Set up the UI elements
        List<Entity> buttons = new ArrayList<>();
        float buttonWidth = 0.165f;
        float buttonHeight = 0.1f;
        float gap = 0.02f;
        float translateY = 0.13f;

        // Create button rectangles
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.JOIN,
                new Vector2(0.403f - buttonWidth / 2, translateY + (buttonHeight + gap) * 2),
                new Vector2(buttonWidth, buttonHeight), this, 2));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.HOST,
                new Vector2(0.503f, translateY + (buttonHeight + gap) * 2),
                new Vector2(buttonWidth, buttonHeight), this, 2));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.HIGHSCORES,
                new Vector2(0.5f - 0.35f / 2, translateY + (buttonHeight + gap) * 0),
                new Vector2(0.35f, buttonHeight), this, 2));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.BACK_MENU,
                new Vector2(0.495f - 0.35f / 2, translateY + (buttonHeight + gap) * 1),
                new Vector2(0.35f, buttonHeight), this, 2));
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

        switch (type) {
            case JOIN:
                System.out.println("Join button pressed");
                screenManager.nextState(new JoinLobby(defaultControllers, yourId));
                break;

            case HOST:
                System.out.println("Host button pressed");
                screenManager.nextState(new ChooseMap(defaultControllers, yourId, true));
                break;

            case HIGHSCORES:
                System.out.println("Highscores button pressed");
                screenManager.nextState(new Highscores(defaultControllers, yourId));
                break;

            case BACK_MENU:
                System.out.println("Back button pressed");
                screenManager.nextState(new Menu(defaultControllers, yourId));
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");

        }
    }

}
