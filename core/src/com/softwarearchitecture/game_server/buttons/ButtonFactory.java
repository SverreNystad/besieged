package com.softwarearchitecture.game_server.buttons;

import java.util.List;
import com.softwarearchitecture.game_server.buttons.Button;

import com.badlogic.gdx.graphics.Texture;
import com.softwarearchitecture.math.Rectangle;

public class ButtonFactory {

    // TODO: logic for creating buttons

    // public static Button createButton(int x, int y, ButtonObserver observer,
    // ButtonType type, int width, int height) {
    // return new Button(x, y, observer, type, width, height);
    // }

    public static Button createButton(ButtonType button, Rectangle hitBox, ButtonObserver observer) {

        /*
         * Creates a button based on the button type
         * Possible refactor: have the observer in factory instead of as a parameter
         */

        // factory that makes buttons based on the state enum
        Texture texture = new Texture("badlogic.jpg");
        switch (button) {
            case OPTIONS:
                // create options buttons
                texture = new Texture("button_placeholder.png");
                return new Button(hitBox, observer, button, texture);

            case GAME_MENU:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            case QUIT:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            case JOIN:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            case HOST:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            case PAUSE:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            case MULTI_PLAYER:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            case SINGLE_PLAYER:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            case PLAY:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            case BACK:
                texture = new Texture("badlogic.jpg");
                return new Button(hitBox, observer, button, texture);
            default:
                throw new IllegalArgumentException("Invalid button type");

        }

    }

}
