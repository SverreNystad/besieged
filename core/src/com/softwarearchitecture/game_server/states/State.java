package com.softwarearchitecture.game_server.states;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_server.buttons.Button;
import com.softwarearchitecture.game_server.buttons.TypeEnum;
import com.softwarearchitecture.math.Vector2;

public abstract class State {
    protected ScreenManager screenManager;
    protected OrthographicCamera cam;
    protected Vector2 mouse = new Vector2(0, 0);
    protected List<Button> buttons;
    protected Texture background;

    protected State() {
        this.screenManager = ScreenManager.getInstance();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, GameApp.WIDTH, GameApp.HEIGHT);
    }

    protected void updateButtons(float deltaTime) {
        mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        for (Button button : buttons) {
            button.update(mouse);
        }
    }

    protected abstract void update(float deltaTime);

    protected abstract void handleInput();

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void dispose();
}
