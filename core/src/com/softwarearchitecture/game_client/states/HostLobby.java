package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;
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
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

public class HostLobby extends State implements Observer {

    public HostLobby(Controllers defaultControllers, UUID yourId) {
        super(defaultControllers, yourId);
    }

    @Override
    protected void activate() {
        // Set background image
        String backgroundPath = TexturePack.BACKGROUND_ABYSS;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0, 0), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        TextComponent textComponent = new TextComponent("Host lobby!", new Vector2(0.05f, 0.05f));
        background.addComponent(TextComponent.class, textComponent);
        ECSManager.getInstance().addEntity(background);

        // Set up the UI elements
        List<ButtonEnum> buttonTypes = new ArrayList<>();
        buttonTypes.add(ButtonEnum.GAME_MENU);
        buttonTypes.add(ButtonEnum.PLAY);
        buttons = createButtons(buttonTypes);

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(this.defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(this.defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
    }

    /**
     * Creates buttons based on the button types
     * 
     * @param buttonTypes: List<ButtonType>
     * @return List<Button>
     * 
     */
    private List<Entity> createButtons(
            List<ButtonEnum> buttonTypes) {

        int numberOfButtons = buttonTypes.size();
        Vector2 containerUVPosition = new Vector2(0, 0); // Position of the container in UV coordinates
        float containerUVWidth = 0.5f; // Width of the container in UV coordinates
        float containerUVHeight = 0.5f; // Height of the container in UV coordinates
        List<Rectangle> buttonRectangles = ButtonFactory.FindUVButtonPositions(numberOfButtons, containerUVPosition,
                containerUVWidth,
                containerUVHeight);
        List<Entity> buttons = new ArrayList<>();

        for (int i = 0; i < numberOfButtons; i++) {
            Rectangle rectangle = buttonRectangles.get(i);
            Vector2 buttonPosition = rectangle.getPosition();
            Vector2 buttonDimentions = new Vector2(rectangle.getWidth(), rectangle.getHeight());
            buttons.add(ButtonFactory.createAndAddButtonEntity(buttonTypes.get(i), buttonPosition, buttonDimentions,
                    this, 0));
        }

        return buttons;
    }

    /**
     * Handles button actions based on the type of the button.
     * This state is only the intermediary menus that traverses to other states.
     * 
     * @param: type: ButtonType.
     */
    @Override
    public void onAction(ButtonEnum type) {
        switch (type) {
            case GAME_MENU:
                screenManager.nextState(new Menu(defaultControllers, yourId));
                break;
           
            default:
                break;
        }
    }

}
