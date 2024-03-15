package com.softwarearchitecture.game_server.buttons;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public abstract class Button {
    /**
     * Abstract class for all menu buttons
     * 
     */

    protected Rectangle hitBox;
    protected Vector3 position;
    protected List<ButtonObserver> observers;

    public Texture texture;

    protected abstract void notifyObservers();

    protected abstract void attachObserver(ButtonObserver observer);

    public boolean isClicked(Vector3 mouse) {
        if (!Gdx.input.justTouched())
            return false;
        float mouseX = mouse.x;
        float mouseY = mouse.y;

        return hitBox.contains(mouseX, mouseY);

    }

    public abstract void update(Vector3 mouse);

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void dispose() {
        // dispose of some kind

    }

}
