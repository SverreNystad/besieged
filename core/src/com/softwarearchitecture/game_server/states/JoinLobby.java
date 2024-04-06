package com.softwarearchitecture.game_server.states;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.softwarearchitecture.GameApp;
import com.softwarearchitecture.game_server.buttons.Button;
import com.softwarearchitecture.game_server.buttons.Factory;
import com.softwarearchitecture.game_server.buttons.Observer;
import com.softwarearchitecture.game_server.buttons.TypeEnum;
import com.softwarearchitecture.game_server.buttons.GridLayout;
import com.softwarearchitecture.math.Rectangle;

public class JoinLobby extends State implements Observer {

    private Stage stage;
    private TextField lobbyCodeField;
    private Label enterLobbyLabel;
    private Skin skin;

    public JoinLobby() {
        super();
        List<TypeEnum> buttonTypes = new ArrayList<>();
        buttonTypes.add(TypeEnum.GAME_MENU);
        buttons = createButtons(buttonTypes);

        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json")); // Placeholder

        // Create and position the label
        this.enterLobbyLabel = new Label("Enter lobby code: ", skin);
        enterLobbyLabel.setPosition(100, 300); // Adjust position as needed

        // Create and position the text field
        this.lobbyCodeField = new TextField("", skin);
        lobbyCodeField.setPosition(100, 250); // Adjust position as needed

        // Add the UI elements to the stage
        stage.addActor(enterLobbyLabel);
        stage.addActor(lobbyCodeField);

        // Make sure to set the input processor to the stage
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Creates buttons based on the button types
     * 
     * @param: buttonTypes: List<ButtonType>
     * @return: List<Button>
     */
    private List<Button> createButtons(List<TypeEnum> buttonTypes) {

        int numberOfButtons = buttonTypes.size();
        int buffergrids = 2;
        List<Rectangle> buttonRectangles = new GridLayout(numberOfButtons + buffergrids, numberOfButtons)
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
        stage.act(deltaTime);

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        Rectangle rect;
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, GameApp.WIDTH, GameApp.HEIGHT);
        // Draw the lobby code. Position it as needed.
        for (Button button : buttons) {
            // button.render(spriteBatch); Easier to draw from here where spriteBatch is
            // already open
            rect = button.getHitBox();
            spriteBatch.draw(button.getTexture(), rect.getX(), rect.getY(),
                    rect.getWidth(), rect.getHeight());

        }

        spriteBatch.end();
        // Draw the stage after ending the spriteBatch
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        skin.dispose();
    }

    /**
     * Handles button actions based on the type of the button.
     * This state is only the intermediary menus that traverses to other states.
     * 
     * @param type: ButtonType.
     */
    @Override
    public void onAction(TypeEnum type) {
        switch (type) {
            case GAME_MENU:
                screenManager.nextState(new Menu(MenuEnum.MENU));
                break;

            case JOIN:
                screenManager.nextState(new Lobby());
                break;

            default:
                break;
        }
    }
}
