package com.softwarearchitecture.game_server.buttons;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

public class Button {

    // should there also be an interface for other stuff that gets drawn on the
    // screen, like text and such?

    private int width;
    private int height;
    private ButtonType type;
    private Rectangle hitBox;
    private Vector2 position;
    private List<ButtonObserver> observers;
    public Texture texture;

    public Button(Rectangle hitbox, ButtonObserver observer, ButtonType type, Texture texture) {

        this.type = type;
        this.hitBox = hitbox;
        observers = new ArrayList<>();
        attachObserver(observer);

        // TODO: Add texture

    }

    public boolean isClicked(Vector2 mouse) {
        /*
         * True if the button is clicked
         */
        if (!Gdx.input.justTouched())
            return false;
        float mouseX = mouse.x;
        float mouseY = mouse.y;

        return hitBox.contains(mouseX, mouseY);

    }

    private void notifyObservers() {
        for (ButtonObserver observer : observers) {
            observer.onAction(type);
        }
    }

    private void attachObserver(ButtonObserver observer) {
        observers.add(observer);
    }

    public void update(Vector2 mouse) {
        if (isClicked(mouse)) {
            notifyObservers();
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        // dispose of some kind

    }

}
