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

public class JoinLobby extends State implements Observer, JoinGameObserver {

    private final int buttonZIndex = 3;
    private final float gap = 0.2f;
    
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

        // Add menu button
        ButtonFactory.createAndAddButtonEntity(ButtonEnum.MULTI_PLAYER, new Vector2(0.45f, 0.05f), new Vector2(0.1f, 0.1f), this, buttonZIndex);
        
        
        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
        
        // Create buttons for joining a game based on the available games
        List<GameState> games = defaultControllers.clientMessagingController.getAllAvailableGames();
        System.out.println("Games: " + games);
        float translateY = 0.7f;
        for (GameState game : games) {
            System.out.println("Game: " + game.playerOne + " " + game.playerTwo);
            ButtonEnum buttonType = ButtonEnum.JOIN;
            Vector2 buttonWidth = new Vector2(0.2f, 0.2f);
            float buttonX = 0.5f - buttonWidth.x / 2;
            Vector2 position = new Vector2(buttonX, translateY);
            ButtonFactory.createAndAddButtonEntity(buttonType, position, buttonWidth, this, game, buttonZIndex);
            translateY -= gap;
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
            case MULTI_PLAYER:
                screenManager.nextState(new Multiplayer(defaultControllers, yourId));
                break;
            case JOIN:
                System.out.println("Join button pressed");
                screenManager.nextState(new InGame(defaultControllers, yourId, "abyss"));
                break;

            default:
                break;
        }
    }

    @Override
    public void onJoinGame(GameState game) {

        // Send a message to the server to join the game
        UUID gameID = game.playerOne.getId();
        boolean didJoin = defaultControllers.clientMessagingController.joinGame(gameID, yourId);
        // Wait for the server to respond
        if (didJoin) {
            // Change the state to the game
            screenManager.nextState(new InGame(defaultControllers, yourId, getGameName(game)));
        } else {
            // Notify the user that the game is full
            System.out.println("Game is full");
        }
    }

    private String getGameName(GameState game) {
        // TODO: Get the name of the game
        return "abyss";
    }
}
