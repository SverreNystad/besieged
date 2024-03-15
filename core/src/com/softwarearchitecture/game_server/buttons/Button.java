package com.softwarearchitecture.game_server.buttons;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Button {

    private int width;
    private int height;
    private ButtonType type;
    private Rectangle hitBox;
    private Vector3 position;
    private List<ButtonObserver> observers;
    public Texture texture;

    public Button(int x, int y, ButtonObserver observer, ButtonType type, int width, int height) {
        this.width = width;
        this.height = height;
        this.position = new Vector3(x, y, 0);
        this.type = type;
        this.hitBox = new Rectangle(position.x, position.y, width, height);
        observers = new ArrayList<>();
        attachObserver(observer);

        // TODO: Add texture, maybe through a entity component system maybe?

    }

    public boolean isClicked(Vector3 mouse) {
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

    public void update(Vector3 mouse) {
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

    public Vector3 getPosition() {
        return position;
    }

    public void dispose() {
        // dispose of some kind

    }

}
