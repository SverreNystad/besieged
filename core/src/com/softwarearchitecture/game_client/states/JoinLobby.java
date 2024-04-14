package com.softwarearchitecture.game_client.states;

import java.util.List;
import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.game_server.GameState;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.math.Vector2;

public class JoinLobby extends State implements Observer {

    public JoinLobby(Controllers defaultControllers, UUID yourId) {
        super(defaultControllers, yourId);
    }

    @Override
    protected void activate() {
        System.out.println("Join lobby activated");
        String backgroundPath = TexturePack.BACKGROUND_VIKING_BATTLE_ICE;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0, 0), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        ECSManager.getInstance().addEntity(background);

        // Add the logo at the top
        TextComponent textComponent = new TextComponent("Find A Game", new Vector2(0.05f, 0.05f));
        PositionComponent textPosition = new PositionComponent(new Vector2(0.45f, 0.9f), 1);
        Entity logo = new Entity();
        logo.addComponent(TextComponent.class, textComponent);
        logo.addComponent(PositionComponent.class, textPosition);
        ECSManager.getInstance().addEntity(logo);


        
        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
        
        // Create buttons for joining a game based on the available games
        List<GameState> games = defaultControllers.clientMessagingController.getAllAvailableGames();
        System.out.println("Games: " + games);
        for (GameState game : games) {
            System.out.println("Game: " + game.playerOne + " " + game.playerTwo);
            ButtonEnum buttonType = ButtonEnum.JOIN;
            ButtonFactory.createAndAddButtonEntity(buttonType, new Vector2(0.5f, 0.5f), new Vector2(0.1f, 0.1f), this, 3);
        }
    }


    /**
     * Handles button actions based on the type of the button.
     * This state is only the intermediary menus that traverses to other states.
     * 
     * @param type: ButtonType.
     */
    @Override
    public void onAction(ButtonEnum type) {
        switch (type) {
            case GAME_MENU:
                screenManager.nextState(new Menu(defaultControllers, yourId));
                break;
            case JOIN:
                System.out.println("Join button pressed");
                screenManager.nextState(new InGame(defaultControllers, yourId));
                break;

            default:
                break;
        }
    }

    private void joinGame(UUID gameID) {
        // Send a message to the server to join the game
        boolean didJoin = defaultControllers.clientMessagingController.joinGame(gameID, yourId);

        // Wait for the server to respond
        if (didJoin) {
            // Change the state to the game
            screenManager.nextState(new InGame(defaultControllers, yourId));
        } else {
            // Notify the user that the game is full
            System.out.println("Game is full");
        }
    }
}
