package com.softwarearchitecture.game_server.screen_components;

import java.util.List;

import com.softwarearchitecture.game_server.screen_components.Button;
import com.softwarearchitecture.game_server.states.TexturePack;
import com.badlogic.gdx.graphics.Texture;
import com.softwarearchitecture.math.Rectangle;

/**
 * This is a button factory
 */
public class Factory {

    /**
     * Creates a button based on the button type
     * 
     * @param button:   TypeEnum
     * @param hitBox:   Rectangle
     * @param observer: Observer
     * @return Button created by factory
     */
    public static Button createButton(TypeEnum button, Rectangle hitBox, Observer observer) {
        // factory that makes buttons based on the state enum
        Texture texture = TexturePack.BUTTON_OPTIONS;
        switch (button) {
            case OPTIONS:
                // create options buttons
                texture = TexturePack.BUTTON_OPTIONS;
                return new Button(hitBox, observer, button, texture);

            case GAME_MENU:
                texture = TexturePack.BUTTON_MENU;
                return new Button(hitBox, observer, button, texture);
            case QUIT:
                texture = TexturePack.BUTTON_QUIT;
                return new Button(hitBox, observer, button, texture);
            case JOIN:
                texture = TexturePack.BUTTON_JOIN;
                return new Button(hitBox, observer, button, texture);
            case HOST:
                texture = TexturePack.BUTTON_HOST;
                return new Button(hitBox, observer, button, texture);
            case PAUSE:
                texture = TexturePack.BUTTON_PAUSE;
                return new Button(hitBox, observer, button, texture);
            case MULTI_PLAYER:
                texture = TexturePack.BUTTON_MULTIPLAYER;
                return new Button(hitBox, observer, button, texture);
            case SINGLE_PLAYER:
                texture = TexturePack.BUTTON_SINLGEPLAYER;
                return new Button(hitBox, observer, button, texture);
            case PLAY:
                texture = TexturePack.BUTTON_PLAY;
                return new Button(hitBox, observer, button, texture);
            case BACK:
                texture = TexturePack.BUTTON_BACK;
                return new Button(hitBox, observer, button, texture);
            case RESUME:
                texture = TexturePack.RESUME;
                return new Button(hitBox, observer, button, texture);
            default:
                throw new IllegalArgumentException("Invalid button type");
        }

    }

}
