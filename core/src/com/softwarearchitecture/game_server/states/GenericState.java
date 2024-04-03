package com.softwarearchitecture.game_server.states;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.softwarearchitecture.game_server.buttons.Button;
import com.softwarearchitecture.game_server.buttons.ButtonObserver;
import com.softwarearchitecture.game_server.buttons.ButtonType;
import com.softwarearchitecture.game_server.buttons.GridLayout;
import com.softwarearchitecture.math.Rectangle;

public class GenericState extends State implements ButtonObserver {

    /**
     * Generic state is a state that can be used for multiple purposes
     * for different types of menus.
     * 
     * parameters: type: GenericStateType, wich is an enum that defines
     * the use of the state
     */

    public GenericState(GenericStateType type) {

        List<ButtonType> buttontypes = getButtonEnums(type);
        buttons = createButtons(buttontypes);
    }

    private List<ButtonType> getButtonEnums(GenericStateType type) {

        /**
         * Returns a list of button types based on the type of the state.
         * parameters: type: GenericStateType
         * returns: List<ButtonType>
         */

        List<ButtonType> buttonTypes = new ArrayList<>();

        switch (type) {
            case MULTI_PLAYER:
                buttonTypes.add(ButtonType.JOIN);
                buttonTypes.add(ButtonType.HOST);
                buttonTypes.add(ButtonType.GAME_MENU);

                break;

            case MENU:
                buttonTypes.add(ButtonType.MULTI_PLAYER);
                buttonTypes.add(ButtonType.SINGLE_PLAYER);
                buttonTypes.add(ButtonType.OPTIONS);
                buttonTypes.add(ButtonType.QUIT);
                break;

            case GAME_OVER:
                buttonTypes.add(ButtonType.GAME_MENU);
                break;

            case PAUSE:
                buttonTypes.add(ButtonType.GAME_MENU);
                buttonTypes.add(ButtonType.OPTIONS);
                buttonTypes.add(ButtonType.QUIT);
                break;

            case SINGLE_PLAYER:
                buttonTypes.add(ButtonType.PLAY);
                buttonTypes.add(ButtonType.GAME_MENU);
                break;

            default:
                break;
        }
        return buttonTypes;
    }

    private List<Button> createButtons(List<ButtonType> buttonTypes) {

        /**
         * Creates buttons based on the button types
         * parameters: buttonTypes: List<ButtonType>
         * returns: List<Button>
         */

        int numberOfButtons = buttonTypes.size();
        int buffergrids = 2;
        List<Rectangle> buttonRectangles = new GridLayout(numberOfButtons + buffergrids, numberOfButtons)
                .getButtonsVertically(numberOfButtons);
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < numberOfButtons; i++) {
            // TODO: add texture
            buttons.add(new Button(buttonRectangles.get(i), this, buttonTypes.get(i), null));
        }

        return buttons;
    }

    @Override
    protected void handleInput() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleInput'");
    }

    @Override
    protected void update(float deltaTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'render'");
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }

    @Override
    public void onAction(ButtonType type) {
        // TODO Auto-generated method stub
        switch (type) {

            case OPTIONS:
                gameStateManager.setOverlapping(new OptionState());
                break;
            case GAME_MENU:
                gameStateManager.push(new GenericState(GenericStateType.MENU));
                break;

            case QUIT:
                // not sure what should happen here
                break;
            case JOIN:
                gameStateManager.push(new JoinLobbyState());
                break;

            case HOST:
                gameStateManager.push(new HostLobbyState());
                break;

            case PAUSE:
                gameStateManager.setOverlapping(new GenericState(GenericStateType.PAUSE));
                break;

            case MULTI_PLAYER:
                gameStateManager.push(new GenericState(GenericStateType.MULTI_PLAYER));
                break;

            case SINGLE_PLAYER:
                gameStateManager.push(new GenericState(GenericStateType.SINGLE_PLAYER));
                break;
            case PLAY:
                gameStateManager.push(new GameState());
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");

        }

    }

}
