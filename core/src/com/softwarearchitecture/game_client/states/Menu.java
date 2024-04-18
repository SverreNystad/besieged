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
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.math.Vector2;

// import systems
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;

public class Menu extends State implements Observer {

    /**
     * Generic state is a state that can be used for multiple purposes
     * for different types of menus.
     * 
     * parameters: type: GenericStateType, wich is an enum that defines
     * the use of the state
     */
    public Menu(Controllers defaultControllers, UUID yourId) {
        super(defaultControllers, yourId);
    }

    @Override
    protected void activate() {
        // Set background image
        String backgroundPath = TexturePack.BACKGROUND_MAIN_MENU;
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

        // Add the logo at the top
        Entity logo = new Entity();
        SpriteComponent logoSprite = new SpriteComponent(TexturePack.LOGO, new Vector2(0.5f, 0.5f));
        PositionComponent logoPosition = new PositionComponent(new Vector2(0.5f - 0.25f, 0.52f), 1);
        logo.addComponent(SpriteComponent.class, logoSprite);
        logo.addComponent(PositionComponent.class, logoPosition);
        ECSManager.getInstance().addLocalEntity(logo);

        // Set up the UI elements
        List<Entity> buttons = new ArrayList<>();
        float buttonWidth = 0.3f;
        float buttonHeight = 0.1f;
        float gap = 0.02f;
        float translateY = 0.1f;

        // Create button rectangles
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.PLAY,
                new Vector2(0.5f - buttonWidth / 2, translateY + (buttonHeight + gap) * 3),
                new Vector2(buttonWidth, buttonHeight), this, 1));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.MULTI_PLAYER,
                new Vector2(0.5f - buttonWidth / 2, translateY + (buttonHeight + gap) * 2),
                new Vector2(buttonWidth, buttonHeight), this, 1));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.OPTIONS,
                new Vector2(0.5f - buttonWidth / 2, translateY + (buttonHeight + gap) * 1),
                new Vector2(buttonWidth, buttonHeight), this, 1));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.QUIT,
                new Vector2(0.5f - buttonWidth / 2, translateY + (buttonHeight + gap) * 0),
                new Vector2(buttonWidth, buttonHeight), this, 1));
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
            case OPTIONS:
                System.out.println("Options button pressed");
                screenManager.nextState(new Options(defaultControllers, yourId));
                break;

            case QUIT:
                // not sure what should happen here
                System.exit(0);
                break;

            case MULTI_PLAYER:
                System.out.println("Multiplayer button pressed");
                screenManager.nextState(new Multiplayer(defaultControllers, yourId));
                break;

            case PLAY:
                System.out.println("Play button pressed");
                screenManager.nextState(new ChooseMap(defaultControllers, yourId, false));
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");

        }
    }

}
