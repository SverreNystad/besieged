package com.softwarearchitecture.game_server.states;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_server.buttons.Button;
import com.softwarearchitecture.game_server.buttons.ButtonType;
import com.softwarearchitecture.math.Vector2;

public abstract class State {

    protected GameStateManager gameStateManager;
    protected OrthographicCamera cam;
    protected Vector2 mouse = new Vector2(0, 0);
    protected List<Button> buttons;
    protected Texture background;

    protected State() {
        this.gameStateManager = new GameStateManager();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, GameApp.WIDTH, GameApp.HEIGHT);
    }

    protected void updateButtons(float deltaTime) {
        mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        for (Button button : buttons) {
            button.update(mouse);
        }
    }

    public void switchState(ButtonType type) {
        /*
         * Switches the state of the game based on the button type
         */
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
            case BACK:
                gameStateManager.popTop();
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");

        }

    }

    protected abstract void update(float deltaTime);

    protected abstract void handleInput();

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void dispose();
}
