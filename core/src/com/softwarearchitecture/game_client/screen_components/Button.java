package com.softwarearchitecture.game_client.screen_components;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.game_client.states.Observer;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

public class Button implements ScreenComponents {

    private TypeEnum type;
    private Rectangle hitBox;
    private Vector2 position;
    private List<Observer> observers;
    public Texture texture;

    public Button(Rectangle hitbox, Observer observer, TypeEnum type, Texture texture) {

        this.type = type;
        this.hitBox = hitbox;
        observers = new ArrayList<>();
        attachObserver(observer);
        this.texture = texture;

    }

    /**
     * Updates the button
     * 
     * @param mouse
     */
    public void update(Vector2 mouse) {
        if (isClicked(mouse)) {
            notifyObservers();
        }
    }

    /**
     * Checks if the button is clicked
     * 
     * @param mouse
     * @return
     */
    private boolean isClicked(Vector2 mouse) {

        if (!Gdx.input.justTouched())
            return false;
        float mouseX = mouse.x;
        float mouseY = mouse.y;

        return hitBox.contains(mouseX, mouseY);

    }

    /**
     * Notifies the observers of the button
     * 
     */
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.onAction(type);
        }
    }

    private void attachObserver(Observer observer) {
        observers.add(observer);
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
