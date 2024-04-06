package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_client.screen_components.Button;
import com.softwarearchitecture.game_client.states.ButtonFactory;
import com.softwarearchitecture.game_client.screen_components.GridLayout;
import com.softwarearchitecture.game_client.states.Observer;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

public class Menu extends State implements Observer {

    /**
     * Generic state is a state that can be used for multiple purposes
     * for different types of menus.
     * 
     * parameters: type: GenericStateType, wich is an enum that defines
     * the use of the state
     */

    public Menu(MenuEnum type) {
        super();
        List<TypeEnum> buttontypes = getButtonEnums(type);
        buttons = createButtons(buttontypes);
        // placeholder background logic not implemented
        // background = TexturePack.BACKGROUND_VIKING_BATTLE_ICE;

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
        Vector2 containerUVPosition = new Vector2(0, 0); // Position of the container in UV coordinates
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

            buttons.add(ButtonFactory.createAndAddButtonEntity(buttonTypes.get(i), buttonPosition, buttonDimentions,
                    this, 0)); // TypeEnum button, Vector2 position, Vector2 size, Observer observer, int
                               // z_index
        }

        return buttons;
    }

    @Override
    protected void handleInput() {
        // do nothing, no input handling in this state
    }

    @Override
    protected void update(float deltaTime) {
        updateButtons(deltaTime);

    }

    // @Override
    // public void render(SpriteBatch spriteBatch) {

    // Rectangle rect;
    // spriteBatch.begin();
    // spriteBatch.draw(background, 0, 0, GameApp.WIDTH, GameApp.HEIGHT);

    // for (com.softwarearchitecture.game_client.screen_components.Button button :
    // buttons) {
    // rect = button.getHitBox();
    // spriteBatch.draw(button.getTexture(), rect.getX(), rect.getY(),
    // rect.getWidth(), rect.getHeight());

    // }
    // spriteBatch.end();
    // }

    @Override
    public void dispose() {

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
                screenManager.nextState(new Options());
                break;
            case GAME_MENU:
                System.out.println("Game menu button pressed");
                screenManager.nextState(new Menu(MenuEnum.MENU));
                break;

            case QUIT:
                // not sure what should happen here
                System.out.println("Quit button pressed");
                System.exit(0);
                break;
            case JOIN:
                System.out.println("Join button pressed");
                screenManager.nextState(new JoinLobby());
                break;

            case HOST:
                System.out.println("Host button pressed");
                screenManager.nextState(new HostLobby());
                break;

            case PAUSE:
                System.out.println("Pause button pressed");
                screenManager.saveState(this);
                screenManager.nextState(new Menu(MenuEnum.PAUSE));
                break;

            case MULTI_PLAYER:
                System.out.println("Multiplayer button pressed");
                screenManager.nextState(new Menu(MenuEnum.MULTI_PLAYER));
                break;

            case SINGLE_PLAYER:
                System.out.println("Singleplayer button pressed");
                screenManager.nextState(new Menu(MenuEnum.SINGLE_PLAYER));
                break;
            case PLAY:
                System.out.println("Play button pressed");
                screenManager.nextState(new InGame());
                break;
            case BACK:
                System.out.println("Back button pressed");
                screenManager.previousState();
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");

        }
    }

    /**
     * Finds the positions of the buttons in the container.
     * 
     * @param numberOfButtons:     int
     * @param containerUVPosition: Vector2
     * @param containerUVWidth:    float
     * @param containerUVHeight:   float
     * @return List<Vector2>
     */

}
