package com.softwarearchitecture.game_server.states;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_server.buttons.Button;

public abstract class State {

    protected GameStateManager gameStateManager;
    protected OrthographicCamera cam;
    protected Vector3 mouse;
    protected List<Button> buttons;

    protected State() {
        this.gameStateManager = new GameStateManager();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, GameApp.WIDTH, GameApp.HEIGHT);
    }

    protected abstract void handleInput();

    protected abstract void update(float deltaTime);

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void dispose();
}
