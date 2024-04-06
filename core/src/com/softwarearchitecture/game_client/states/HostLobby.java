package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.game_client.screen_components.GridLayout;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

public class HostLobby extends State implements Observer {

    private String lobbyCode;
    private BitmapFont font;

    public HostLobby() {
        super();
        List<TypeEnum> buttonTypes = new ArrayList<>();
        buttonTypes.add(TypeEnum.GAME_MENU);
        buttons = createButtons(buttonTypes);

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
    private List<Entity> createButtons(
            List<TypeEnum> buttonTypes) {

        int numberOfButtons = buttonTypes.size();
        int buffergrids = 2;
        List<Rectangle> buttonRectangles = new GridLayout(numberOfButtons + buffergrids, numberOfButtons)
                .getButtonsVertically(numberOfButtons);
        List<Entity> buttons = new ArrayList<>();

        for (int i = 0; i < numberOfButtons; i++) {
            Rectangle rectangle = buttonRectangles.get(i);
            Vector2 buttonPosition = rectangle.getPosition();
            Vector2 buttonDimentions = new Vector2(rectangle.getWidth(), rectangle.getHeight());
            buttons.add(ButtonFactory.createAndAddButtonEntity(buttonTypes.get(i), buttonPosition, buttonDimentions,
                    this, 0));

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
    public void dispose() {
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
                screenManager.nextState(new Menu(MenuEnum.MENU));
                break;
            case PLAY:
                screenManager.nextState(new InGame());
                break;
            default:
                break;
        }
    }
}
