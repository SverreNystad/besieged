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
import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_server.TexturePack;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

public class Menu extends State implements Observer {

    private MenuEnum type;
    /**
     * Generic state is a state that can be used for multiple purposes
     * for different types of menus.
     * 
     * parameters: type: GenericStateType, wich is an enum that defines
     * the use of the state
     */
    public Menu(MenuEnum type, Controllers defaultControllers) {
        super(defaultControllers);
        this.type = type;
    }

    @Override
    protected void activate() {
        // Set background image
        String backgroundPath = TexturePack.BACKGROUND_TOR;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0f, 0f), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        TextComponent textComponent = new TextComponent("Menu!", new Vector2(0.05f, 0.05f));
        background.addComponent(TextComponent.class, textComponent);
        ECSManager.getInstance().addEntity(background);

        // Set up the UI elements
        List<TypeEnum> buttontypes = getButtonEnums(type);
        buttons = createButtons(buttontypes);

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(this.defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(this.defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
        
        System.out.println("Menu created");
    }    

    /**
     * Returns a list of button types based on the type of the state.
     * 
     * @param type: GenericStateType
     * @return List<ButtonType>
     */
    private List<TypeEnum> getButtonEnums(MenuEnum type) {

        List<TypeEnum> buttons = new ArrayList<>();

        switch (type) {
            case MULTI_PLAYER:
                buttons.add(TypeEnum.JOIN);
                buttons.add(TypeEnum.HOST);
                buttons.add(TypeEnum.GAME_MENU);

                break;

            case MENU:
                buttons.add(TypeEnum.MULTI_PLAYER);
                buttons.add(TypeEnum.SINGLE_PLAYER);
                buttons.add(TypeEnum.OPTIONS);
                buttons.add(TypeEnum.QUIT);
                break;

            case GAME_OVER:
                buttons.add(TypeEnum.GAME_MENU);
                break;

            case PAUSE:
                buttons.add(TypeEnum.GAME_MENU);
                buttons.add(TypeEnum.OPTIONS);
                buttons.add(TypeEnum.QUIT);
                buttons.add(TypeEnum.BACK);

                break;

            case SINGLE_PLAYER:
                buttons.add(TypeEnum.PLAY);
                buttons.add(TypeEnum.GAME_MENU);
                break;

            default:
                break;
        }
        return buttons;
    }

    /**
     * Creates buttons based on the button types.
     * 
     * @param: buttonTypes: List<ButtonType>
     * @return: List<Button>
     */
    private List<Entity> createButtons(List<TypeEnum> buttonTypes) {
        int numberOfButtons = buttonTypes.size();
        Vector2 containerUVPosition = new Vector2(0.25f, 0.25f); // Position of the container in UV coordinates
        float containerUVWidth = 0.5f; // Width of the container in UV coordinates
        float containerUVHeight = 0.5f; // Height of the container in UV coordinates
        List<Rectangle> buttonRectangles = ButtonFactory.FindUVButtonPositions(numberOfButtons, containerUVPosition,
                containerUVWidth,
                containerUVHeight); // Num knapper, start posisjon til nederste knapp (nede til venstre), hvor stor
        // del av skjerment alle knappene skal ta, hvor stor avstand skal være mellom
        // knappene.

        List<Entity> buttons = new ArrayList<>();

        for (int i = 0; i < numberOfButtons; i++) {
            Rectangle rectangle = buttonRectangles.get(i);
            Vector2 buttonPosition = rectangle.getPosition();
            Vector2 buttonDimentions = new Vector2(rectangle.getWidth(), rectangle.getHeight());
            buttons.add(ButtonFactory.createAndAddButtonEntity(buttonTypes.get((buttonTypes.size()-1) - i), buttonPosition, buttonDimentions,
                    this, 0)); // TypeEnum button, Vector2 position, Vector2 size, Observer observer, int
                               // z_index
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
        // Switches the state of the game based on the button type

        switch (type) {
            case OPTIONS:
                System.out.println("Options button pressed");
                screenManager.nextState(new Options(defaultControllers));
                break;
            case GAME_MENU:
                System.out.println("Game menu button pressed");
                screenManager.nextState(new Menu(MenuEnum.MENU, defaultControllers));
                break;

            case QUIT:
                // not sure what should happen here
                System.exit(0);
                break;
            case JOIN:
                System.out.println("Join button pressed");
                screenManager.nextState(new JoinLobby(defaultControllers));
                break;

            case HOST:
                System.out.println("Host button pressed");
                screenManager.nextState(new HostLobby(defaultControllers));
                break;

            case PAUSE:
                System.out.println("Pause button pressed");
                // screenManager.saveState(this);
                screenManager.nextState(new Menu(MenuEnum.PAUSE, defaultControllers));
                break;

            case MULTI_PLAYER:
                System.out.println("Multiplayer button pressed");
                screenManager.nextState(new Menu(MenuEnum.MULTI_PLAYER, defaultControllers));
                break;

            case SINGLE_PLAYER:
                System.out.println("Singleplayer button pressed");
                screenManager.nextState(new Menu(MenuEnum.SINGLE_PLAYER, defaultControllers));
                break;
            case PLAY:
                System.out.println("Play button pressed");
                screenManager.nextState(new InGame(defaultControllers));
                break;
            case BACK:
                screenManager.previousState();
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");

        }
    }

}
