package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.systems.GameOverSystem;
import com.softwarearchitecture.ecs.systems.AudioSystem;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.MovementSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.game_server.CardFactory;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.math.Vector2;

public class InGame extends State implements Observer, GameOverObserver {

    private CardType selectedCardType = null;
    private List<Entity> cardButtonEntities = new ArrayList<>();
    
    protected InGame(Controllers defaultControllers, UUID yourId, String mapName)  {
        super(defaultControllers, yourId);
    }


    @Override
    protected void activate() {
        // Make a button that covers the whole screen and sends a message to the server when clicked
        Entity screenTouch = new Entity();
        Runnable callback = () -> {
            System.out.println("Screen touched at: " + defaultControllers.inputController.getLastReleaseLocation().u + ", " + defaultControllers.inputController.getLastReleaseLocation().v);
            ComponentManager<SpriteComponent> spriteManager = ECSManager.getInstance().getOrDefaultComponentManager(SpriteComponent.class);  
            ComponentManager<TileComponent> tileManager = ECSManager.getInstance().getOrDefaultComponentManager(TileComponent.class);
            ComponentManager<PositionComponent> positionManager = ECSManager.getInstance().getOrDefaultComponentManager(PositionComponent.class);

            Set<Entity> entities = ECSManager.getInstance().getLocalEntities();
            entities.addAll(ECSManager.getInstance().getRemoteEntities());
            for (Entity entity : entities) {
                if (spriteManager.getComponent(entity).isPresent() && tileManager.getComponent(entity).isPresent() && positionManager.getComponent(entity).isPresent()) {
                    SpriteComponent sprite = spriteManager.getComponent(entity).get();
                    PositionComponent position = positionManager.getComponent(entity).get();
                    TileComponent tile = tileManager.getComponent(entity).get();
                    float u = defaultControllers.inputController.getLastReleaseLocation().u;
                    float v = defaultControllers.inputController.getLastReleaseLocation().v;

                    if (position.position.x <= u && u <= position.position.x + sprite.size_uv.x && position.position.y <= v && v <= position.position.y + sprite.size_uv.y) {
                        PlayerInput action = new PlayerInput(yourId, selectedCardType, tile.getTile().getX(), tile.getTile().getY());
                        if (screenManager.isLocalServer()) defaultControllers.localClientMessagingController.addAction(action);
                        else defaultControllers.onlineClientMessagingController.addAction(action);
                    }
                }
            }
        };

        ButtonComponent button = new ButtonComponent(new Vector2(0,0), new Vector2(1,1), ButtonEnum.TILE, 0, callback);
        screenTouch.addComponent(ButtonComponent.class, button);
        ECSManager.getInstance().addLocalEntity(screenTouch);
        System.out.println("Added screen touch button");


        // Buttons
        Entity backButton = ButtonFactory.createAndAddButtonEntity(ButtonEnum.BACK, new Vector2(0, 1),
                new Vector2(0.1f, 0.2f), this, 0);
        ECSManager.getInstance().addLocalEntity(backButton);


        // TODO: Check if multiplayer and add multiplayer-specific button functionality
        // Card selection menu
        createCardSelectionMenu();


        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        MovementSystem movementSystem = new MovementSystem();
        GameOverSystem gameOverSystem = new GameOverSystem(this);
        AudioSystem audioSystem = new AudioSystem(defaultControllers.soundController);

        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
        ECSManager.getInstance().addSystem(movementSystem);
        ECSManager.getInstance().addSystem(gameOverSystem);
        ECSManager.getInstance().addSystem(audioSystem);
    }

    @Override
    public void onAction(ButtonEnum type) {
        switch (type) {
            case BACK:
                ScreenManager.getInstance().previousState();
                break;
            default:
                break;
        }
    }

    private void createCardSelectionMenu() {
        float menuYPosition = -0.02f; // Bottom of the screen
        float menuHeight = 0.2f;
        String menuBackgroundTexture = TexturePack.BOARD;

        // Create menu background entity
        Entity menuBackground = new Entity();
        SpriteComponent menuBackgroundSprite = new SpriteComponent(menuBackgroundTexture,
                new Vector2(0.4f, menuHeight));
        Vector2 position1 = new Vector2(0.58f, menuYPosition);
        PositionComponent menuBackgroundPosition = new PositionComponent(position1, 2);
        menuBackground.addComponent(SpriteComponent.class, menuBackgroundSprite);
        menuBackground.addComponent(PositionComponent.class, menuBackgroundPosition);
        ECSManager.getInstance().addLocalEntity(menuBackground);

        // Create buttons for each card type
        float buttonWidth = 0.065f;
        float buttonHeight = 0.185f;
        float gap = 0.005f;
        for (CardType type : CardType.values()) {
            Entity card = CardFactory.createCard(type, new Vector2(0, 0), false);
            Vector2 position2 = new Vector2(0.61f + type.ordinal() * (buttonWidth + gap), menuYPosition - 0.065f);
            Vector2 size = new Vector2(buttonWidth, buttonHeight);
            Entity button = createCardTypeButton(card, position2, size);
            ECSManager.getInstance().addLocalEntity(button);
        }
    }

    // Method to create a button for a card type
    private Entity createCardTypeButton(Entity cardEntity, Vector2 position, Vector2 size) {
        Entity buttonEntity = new Entity();
        PositionComponent cardPosition = cardEntity.getComponent(PositionComponent.class).get();
        cardPosition.position = position;
        // Define the SpriteComponent for the button

        // Get the texture from the card entity
        SpriteComponent cardSprite = cardEntity.getComponent(SpriteComponent.class).get();

        // Increase the size of the sprite
        Vector2 largerSize = new Vector2(size.x, size.y); // Increase the size by 50%
        cardSprite.size_uv  = largerSize;

        // Button component also has a callback now
        Runnable onButtonClick = () -> {
            System.out.println("Selected card: " + cardEntity.toString());
            selectedCardType = cardEntity.getComponent(PlacedCardComponent.class).get().cardType;

            // Reset the position of all other cards
            for (Entity entity : cardButtonEntities) {
                PositionComponent positionComponent = entity.getComponent(PositionComponent.class).get();
                positionComponent.position.y = -0.085f;

            }

            // Move the selected card up
            cardPosition.position.y = -0.015f;

        };

        ButtonComponent buttonComponent = new ButtonComponent(position, largerSize, ButtonEnum.CARD, 10, onButtonClick);

        // Add the PositionComponent and SpriteComponent to the button entity
        buttonEntity.addComponent(PositionComponent.class, cardPosition);
        buttonEntity.addComponent(SpriteComponent.class, cardSprite);
        buttonEntity.addComponent(ButtonComponent.class, buttonComponent);

        cardButtonEntities.add(buttonEntity);

        return buttonEntity;
    }


    @Override
    public void handleGameOver() {
        ScreenManager.getInstance().nextState(new GameOver(defaultControllers, yourId));
    }
}
