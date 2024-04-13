package com.softwarearchitecture.game_server.states;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_server.screen_components.Button;
import com.softwarearchitecture.game_server.screen_components.Factory;
import com.softwarearchitecture.game_server.screen_components.GridLayout;
import com.softwarearchitecture.game_server.screen_components.Observer;
import com.softwarearchitecture.game_server.screen_components.TypeEnum;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.graphics.LibGDXGraphics;

public class Multiplayer extends State implements Observer {

    /**
     * Generic state is a state that can be used for multiple purposes
     * for different types of menus.
     * 
     * parameters: type: GenericStateType, wich is an enum that defines
     * the use of the state
     */

    public Multiplayer() {
        super();
        // placeholder background logic not implemented
        background = TexturePack.BACKGROUND_MULTIPLAYER;
        this.buttons = new ArrayList<>();

    }

    @Override
    protected void handleInput() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void update(float deltaTime) {
        updateButtons(deltaTime);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        Rectangle rect;
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, GameApp.WIDTH, GameApp.HEIGHT);

        // Draw the logo
        spriteBatch.draw(TexturePack.MULTIPLAYER_SIGN, GameApp.WIDTH / 2 - 550 / 2, GameApp.HEIGHT - 300, 550, 300);

        int buttonWidth = 220;
        int buttonHeight = 60;
        int gap = 20;
        int translateY = 150;

        buttons.add(new Button(
                new Rectangle(GameApp.WIDTH / 2 - 225, translateY + buttonHeight + gap, buttonWidth,
                        buttonHeight),
                this, TypeEnum.JOIN,
                TexturePack.BUTTON_JOIN));
        buttons.add(new Button(
                new Rectangle(GameApp.WIDTH / 2 + 5, translateY + buttonHeight + gap, buttonWidth,
                        buttonHeight),
                this, TypeEnum.HOST,
                TexturePack.BUTTON_HOST));
        buttons.add(new Button(
                new Rectangle(GameApp.WIDTH / 2 - 450 / 2, translateY, 450,
                        60),
                this, TypeEnum.BACK,
                TexturePack.BUTTON_BACK));

        for (Button button : buttons) {
            rect = button.getHitBox();
            spriteBatch.draw(button.getTexture(), rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }

    /**
     * Handles button actions based on the type of the button.
     * This state is only the intermediary menus that traverses to other states.
     * 
     * @param type: ButtonType.
     */
    @Override
    public void onAction(TypeEnum type) {
        // Switches the state of the game based on the button type

        switch (type) {
            case BACK:
                System.out.println("Back button pressed");
                screenManager.nextState(new Menu());
                break;

            case QUIT:
                // not sure what should happen here
                System.out.println("Quit button pressed");
                System.exit(0);
                break;

            case MULTI_PLAYER:
                System.out.println("Multiplayer button pressed");
                screenManager.nextState(new Multiplayer());
                break;

            case SINGLE_PLAYER:
                System.out.println("Singleplayer button pressed");
                screenManager.nextState(new Menu());
                break;

            default:
                throw new IllegalArgumentException("Invalid button type");
        }
    }

}
