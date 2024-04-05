package com.softwarearchitecture.game_server.states;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_server.buttons.Button;
import com.softwarearchitecture.game_server.buttons.Factory;
import com.softwarearchitecture.game_server.buttons.Observer;
import com.softwarearchitecture.game_server.buttons.TypeEnum;
import com.softwarearchitecture.game_server.buttons.GridLayout;
import com.softwarearchitecture.math.Rectangle;

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
        background = new Texture("badlogic.jpg");

    }

    private List<TypeEnum> getButtonEnums(MenuEnum type) {

        /**
         * Returns a list of button types based on the type of the state.
         * parameters: type: GenericStateType
         * returns: List<ButtonType>
         */

        List<TypeEnum> buttonTypes = new ArrayList<>();

        switch (type) {
            case MULTI_PLAYER:
                buttonTypes.add(TypeEnum.JOIN);
                buttonTypes.add(TypeEnum.HOST);
                buttonTypes.add(TypeEnum.GAME_MENU);

                break;

            case MENU:
                buttonTypes.add(TypeEnum.MULTI_PLAYER);
                buttonTypes.add(TypeEnum.SINGLE_PLAYER);
                buttonTypes.add(TypeEnum.OPTIONS);
                buttonTypes.add(TypeEnum.QUIT);
                break;

            case GAME_OVER:
                buttonTypes.add(TypeEnum.GAME_MENU);
                break;

            case PAUSE:
                buttonTypes.add(TypeEnum.GAME_MENU);
                buttonTypes.add(TypeEnum.OPTIONS);
                buttonTypes.add(TypeEnum.QUIT);
                buttonTypes.add(TypeEnum.BACK);

                break;

            case SINGLE_PLAYER:
                buttonTypes.add(TypeEnum.PLAY);
                buttonTypes.add(TypeEnum.GAME_MENU);
                break;

            default:
                break;
        }
        return buttonTypes;
    }

    private List<Button> createButtons(List<TypeEnum> buttonTypes) {

        /**
         * Creates buttons based on the button types
         * parameters: buttonTypes: List<ButtonType>
         * returns: List<Button>
         */

        int numberOfButtons = buttonTypes.size();
        int buffergrids = 2;
        List<Rectangle> buttonRectangles = new GridLayout(numberOfButtons, numberOfButtons)
                .getButtonsVertically(numberOfButtons);
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < numberOfButtons; i++) {
            buttons.add(Factory.createButton(buttonTypes.get(i), buttonRectangles.get(i), this));

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

    @Override
    public void render(SpriteBatch spriteBatch) {
        Rectangle rect;
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, GameApp.WIDTH, GameApp.HEIGHT);
        for (Button button : buttons) {
            // button.render(spriteBatch); Easier to draw from here where spriteBatch is
            // already open
            rect = button.getHitBox();

            spriteBatch.draw(button.getTexture(), rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        }
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }

    @Override
    public void onAction(TypeEnum type) {

        /**
         * Handles button actions based on the type of the button.
         * This state is only the intermediary menus that traverses to other states.
         * parameters: type: ButtonType.
         */
        switchState(type); // this method is in the state class
    }

    public void switchState(TypeEnum type) {
        /*
         * Switches the state of the game based on the button type
         */
        switch (type) {

            case OPTIONS:
                gameStateManager.setOverlapping(new Options());
                break;
            case GAME_MENU:
                gameStateManager.set(new Menu(MenuEnum.MENU));
                break;

            case QUIT:
                // not sure what should happen here
                break;
            case JOIN:
                gameStateManager.set(new JoinLobby());
                break;

            case HOST:
                gameStateManager.set(new HostLobby());
                break;

            case PAUSE:
                gameStateManager.setOverlapping(new Menu(MenuEnum.PAUSE));
                break;

            case MULTI_PLAYER:
                gameStateManager.set(new Menu(MenuEnum.MULTI_PLAYER));
                break;

            case SINGLE_PLAYER:
                gameStateManager.set(new Menu(MenuEnum.SINGLE_PLAYER));
                break;
            case PLAY:
                gameStateManager.set(new InGame());
                break;
            case BACK:
                gameStateManager.popTop();
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");

        }

    }

}
