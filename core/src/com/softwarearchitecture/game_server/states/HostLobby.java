package com.softwarearchitecture.game_server.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_server.screen_components.Button;
import com.softwarearchitecture.game_server.screen_components.Factory;
import com.softwarearchitecture.game_server.screen_components.GridLayout;
import com.softwarearchitecture.game_server.screen_components.Observer;
import com.softwarearchitecture.game_server.screen_components.TypeEnum;
import com.softwarearchitecture.math.Rectangle;

public class HostLobby extends State implements Observer {

    private String lobbyCode;
    private BitmapFont font;

    public HostLobby() {
        super();
        List<TypeEnum> buttonTypes = new ArrayList<>();
        buttonTypes.add(TypeEnum.GAME_MENU);
        buttons = createButtons(buttonTypes);

        // placeholder background logic not implemented
        background = TexturePack.BACKGROUND_TOR;

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

    /**
     * Creates buttons based on the button types
     * 
     * @param buttonTypes: List<ButtonType>
     * @return List<Button>
     * 
     */
    private List<Button> createButtons(List<TypeEnum> buttonTypes) {

        int numberOfButtons = buttonTypes.size();
        int buffergrids = 2;
        List<Rectangle> buttonRectangles = new GridLayout(numberOfButtons + buffergrids, numberOfButtons, 10)
                .getButtonsVertically(numberOfButtons);
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < numberOfButtons; i++) {
            buttons.add(Factory.createButton(buttonTypes.get(i), buttonRectangles.get(i), this));

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
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        Rectangle rect;
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, GameApp.WIDTH, GameApp.HEIGHT);
        font.draw(spriteBatch, "Lobby Code: " + lobbyCode, 100, 200); // Example position
        for (Button button : buttons) {

            rect = button.getHitBox();
            spriteBatch.draw(button.getTexture(), rect.getX(), rect.getY(),
                    rect.getWidth(), rect.getHeight());
        }
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
    }

    /**
     * Handles button actions based on the type of the button.
     * This state is only the intermediary menus that traverses to other states.
     * 
     * @param: type: ButtonType.
     */
    @Override
    public void onAction(TypeEnum type) {
        switch (type) {
            case GAME_MENU:
                screenManager.nextState(new Menu());
                break;
            case PLAY:
                screenManager.nextState(new InGame());
                break;
            default:
                break;
        }
    }
}
