package com.softwarearchitecture.game_server.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_server.buttons.Button;
import com.softwarearchitecture.game_server.buttons.ButtonFactory;
import com.softwarearchitecture.game_server.buttons.ButtonObserver;
import com.softwarearchitecture.game_server.buttons.ButtonType;
import com.softwarearchitecture.game_server.buttons.GridLayout;
import com.softwarearchitecture.math.Rectangle;



public class HostLobbyState extends State implements ButtonObserver{

    private String lobbyCode;
    private BitmapFont font;

    public HostLobbyState() {
        super();
        List<ButtonType> buttonTypes = new ArrayList<>();
        buttonTypes.add(ButtonType.GAME_MENU);
        buttons = createButtons(buttonTypes);

        // placeholder background logic not implemented
        background = new Texture("viking_image2.png");

        this.lobbyCode = generateLobbyCode(6); // Generate a 6-character code
        this.font = new BitmapFont(); // Initialize your font (consider using a specific font file)
    }

    private static String generateLobbyCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rng = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(rng.nextInt(characters.length())));
        }
        return code.toString();
    }
    private List<Button> createButtons(List<ButtonType> buttonTypes) {

        /**
         * Creates buttons based on the button types
         * parameters: buttonTypes: List<ButtonType>
         * returns: List<Button>
         */

        int numberOfButtons = buttonTypes.size();
        int buffergrids = 2;
        List<Rectangle> buttonRectangles = new GridLayout(numberOfButtons + buffergrids, numberOfButtons)
                .getButtonsVertically(numberOfButtons);
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < numberOfButtons; i++) {
            buttons.add(ButtonFactory.createButton(buttonTypes.get(i), buttonRectangles.get(i), this));

        }

        return buttons;
    }


    @Override
    protected void handleInput() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleInput'");
    }

    @Override
    protected void update(float deltaTime) {
        updateButtons(deltaTime);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        Rectangle rect;
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, GameApp.WIDTH, GameApp.HEIGHT);
        // Draw the lobby code. Position it as needed.
        font.draw(spriteBatch, "Lobby Code: " + lobbyCode, 100, 200); // Example position
        for (Button button : buttons) {
            // button.render(spriteBatch); Easier to draw from here where spriteBatch is
            // already open
            rect = button.getHitBox();
            spriteBatch.draw(button.getTexture(), rect.getX(), rect.getY(),
                    rect.getWidth(), rect.getHeight());

        }

    
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }

}
