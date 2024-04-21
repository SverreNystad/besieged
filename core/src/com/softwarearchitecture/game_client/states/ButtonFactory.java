package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.game_server.GameState;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

/**
 * This is a button factory
 */
public class ButtonFactory {

    /**
     * Creates a button based on the button type and adds it to the ECS system
     * 
     * @param button:   ButtonEnum
     * @param size:     Vector2
     * @param observer: Observer
     * @throws IllegalArgumentException if the button type is invalid
     */
    public static Entity createAndAddButtonEntity(ButtonEnum button, Vector2 position, Vector2 size, Observer observer,
            int z_index) throws IllegalArgumentException {
        // factory that makes buttons based on the state enum
        String texture = chooseTexture(button);
        Runnable callback = () -> {
            observer.onAction(button);
        };

        ButtonComponent buttonComponent = new ButtonComponent(position, size, button, z_index, callback);
        PositionComponent positionComponent = new PositionComponent(position, z_index);
        SpriteComponent spriteComponent = new SpriteComponent(texture, size);

        Entity buttonEntity = new Entity();
        buttonEntity.addComponent(ButtonComponent.class, buttonComponent);
        buttonEntity.addComponent(PositionComponent.class, positionComponent);
        buttonEntity.addComponent(SpriteComponent.class, spriteComponent);

        ECSManager.getInstance().addLocalEntity(buttonEntity);
        return buttonEntity;
    }

    public static Entity createAndAddButtonEntity(ButtonEnum button, Vector2 position, Vector2 size,
            JoinGameObserver observer, GameState game, int z_index) {
        String texture = chooseTexture(button);

        // Here, we pass the game to the joinGame method directly in the lambda
        Runnable callback = () -> observer.onJoinGame(game);

        ButtonComponent buttonComponent = new ButtonComponent(position, size, button, z_index, callback);
        PositionComponent positionComponent = new PositionComponent(position, z_index);
        SpriteComponent spriteComponent = new SpriteComponent(texture, size);

        Entity buttonEntity = new Entity();
        buttonEntity.addComponent(ButtonComponent.class, buttonComponent);
        buttonEntity.addComponent(PositionComponent.class, positionComponent);
        buttonEntity.addComponent(SpriteComponent.class, spriteComponent);

        ECSManager.getInstance().addLocalEntity(buttonEntity);
        return buttonEntity;
    }

    private static String chooseTexture(ButtonEnum button) {
        String texture = TexturePack.BUTTON_PLACEHOLDER;
        switch (button) {
            case OPTIONS:
                texture = TexturePack.BUTTON_OPTION;
                break;
            case GAME_MENU:
                texture = TexturePack.BUTTON_GAME_MENU;
                break;
            case QUIT:
                texture = TexturePack.BUTTON_QUIT;
                break;
            case JOIN:
                texture = TexturePack.BUTTON_JOIN;
                break;
            case HOST:
                texture = TexturePack.BUTTON_HOST;
                break;
            case PAUSE:
                texture = TexturePack.BUTTON_PAUSE;
                break;
            case MULTI_PLAYER:
                texture = TexturePack.BUTTON_MULTI_PLAYER;
                break;
            case SINGLE_PLAYER:
                texture = TexturePack.BUTTON_SINGLE_PLAYER;
                break;
            case PLAY:
                texture = TexturePack.BUTTON_PLAY;
                break;
            case BACK:
                texture = TexturePack.BUTTON_BACK;
                break;
            case BACK_MENU:
                texture = TexturePack.BUTTON_BACK_MENU;
                break;
            case PLUSS:
                texture = TexturePack.BUTTON_PLUSS;
                break;
            case MINUS:
                texture = TexturePack.BUTTON_MINUS;
                break;
            case MUTE:
                texture = TexturePack.BUTTON_MUTE;
                break;
            case TILE:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case CARD:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case ABYSS:
                texture = TexturePack.BUTTON_ABYSS;
                break;
            case TEST:
                texture = TexturePack.BUTTON_TEST;
                break;
            case AVAILABLE_LOBBY:
                texture = TexturePack.BUTTON_AVAILABLE_LOBBY;
                break;
            case TUTORIAL:
                texture = TexturePack.BUTTON_TUTORIAL;
                break;
            case HIGHSCORES:
                texture = TexturePack.BUTTON_HIGHSCORES;
                break;
            case CLEARING:
                texture = TexturePack.BUTTON_CLEARING;
                break;
            default:
                throw new IllegalArgumentException("Invalid button type");
        }
        return texture;
    }

    public static List<Rectangle> FindUVButtonPositions(int numberOfButtons, Vector2 containerUVPosition,
            float containerUVWidth, float containerUVHeight) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        // Calculate each button's height as a fraction of the container's height
        float buttonUVHeight = containerUVHeight / numberOfButtons;

        // Calculate the starting y position for the first button, considering the
        // bottom of the container plus half the height of a button to center it
        // vertically.
        float startY = containerUVPosition.y + buttonUVHeight / 2;

        // Calculate each button's position within the container
        for (int i = 0; i < numberOfButtons; i++) {
            float xPos = containerUVPosition.x + containerUVWidth / 2; // Centering button horizontally within the
                                                                       // container
            float yPos = startY + i * buttonUVHeight; // Positioning button vertically, moving upwards

            // Calculate the rectangle for each button, with width being the container's
            // width and height being the calculated button height
            Rectangle rectangle = new Rectangle(xPos - containerUVWidth / 2, yPos - buttonUVHeight / 2,
                    containerUVWidth, buttonUVHeight);
            // Add the rectangle to the list
            rectangles.add(rectangle);
        }

        return rectangles;
    }
}
