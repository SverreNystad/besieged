package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.ecs.Controllers;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.game_server.TexturePack;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

public class JoinLobby extends State implements Observer {

    public JoinLobby(Controllers defaultControllers) {
        super(defaultControllers);
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
        TextComponent textComponent = new TextComponent("Join lobby!", new Vector2(0.05f, 0.05f));
        background.addComponent(TextComponent.class, textComponent);
        List<TypeEnum> buttonTypes = new ArrayList<>();
        buttonTypes.add(TypeEnum.GAME_MENU);
        buttons = createButtons(buttonTypes);

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
    }
    
    /**
     * Creates buttons based on the button types
     * 
     * @param: buttonTypes: List<ButtonType>
     * @return: List<Button>
     */
    private List<Entity> createButtons(List<TypeEnum> buttonTypes) {

        int numberOfButtons = buttonTypes.size();
        int buffergrids = 2;

        List<Rectangle> buttonRectangles = ButtonFactory.FindUVButtonPositions(numberOfButtons, new Vector2(0, 0), 1,
                1);
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
     * @param type: ButtonType.
     */
    @Override
    public void onAction(TypeEnum type) {
        switch (type) {
            case GAME_MENU:
                screenManager.nextState(new Menu(MenuEnum.MENU, defaultControllers));
                break;

            case JOIN:
                screenManager.nextState(new Lobby(defaultControllers));
                break;

            default:
                break;
        }
    }
}
