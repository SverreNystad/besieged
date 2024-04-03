package com.softwarearchitecture.game_server.states.trashbin;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.softwarearchitecture.game_server.buttons.ButtonFactory;
import com.softwarearchitecture.game_server.buttons.ButtonObserver;
import com.softwarearchitecture.game_server.buttons.ButtonType;
import com.softwarearchitecture.game_server.buttons.GridLayout;
import com.softwarearchitecture.game_server.states.HostLobbyState;
import com.softwarearchitecture.game_server.states.JoinLobbyState;
import com.softwarearchitecture.game_server.states.OptionState;
import com.softwarearchitecture.game_server.states.State;
import com.softwarearchitecture.math.Rectangle;

public class GameMenuState extends State implements ButtonObserver {

    public GameMenuState() {

        int numberOfButtons = 3;
        List<Rectangle> buttonRectangles = new GridLayout(5, 5).getButtonsVertically(numberOfButtons);

        // buttons = ButtonFactory.createButtons("GameMenuState");

        // TODO: texture for background
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
                gameStateManager.push(new GameMenuState());
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
                gameStateManager.setOverlapping(new PauseState());
                break;

            case MULTI_PLAYER:
                gameStateManager.push(new MultiPlayerState());
                break;

            // case SINGLE_PLAYER:
            // gameStateManager.push(new SinglePlayerState());
            // break;

            default:
                throw new IllegalArgumentException("Invalid button type");

        }

    }
}
