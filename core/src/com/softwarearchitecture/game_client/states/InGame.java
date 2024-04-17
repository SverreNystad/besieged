package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.systems.EnemySystem;
import com.softwarearchitecture.ecs.systems.AnimationSystem;
import com.softwarearchitecture.ecs.systems.AttackSystem;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.MovementSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.game_server.CardFactory;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.game_server.MapFactory;
import com.softwarearchitecture.game_server.PairableCards;
import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.game_server.PairableCards.TowerType;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TowerFactory;
import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.math.Vector3;

public class InGame extends State implements Observer {
    
    private Map gameMap;
    private Entity village;
    private String mapName;
    private CardType selectedCardType = null;
    private boolean isMultiplayer;
    private List<Entity> cardButtonEntities = new ArrayList<>();
    
    protected InGame(Controllers defaultControllers, UUID yourId, String mapName, boolean isMultiplayer) {
        super(defaultControllers, yourId);
        this.mapName = mapName;
        this.isMultiplayer = isMultiplayer;
    }

    @Override
    protected void activate() {
        if (!isMultiplayer) {
            // Background
            String backgroundPath = TexturePack.BACKGROUND_TOR;
            Entity background = new Entity();
            SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
            PositionComponent backgroundPosition = new PositionComponent(new Vector2(0, 0), -1);
            background.addComponent(SpriteComponent.class, backgroundSprite);
            background.addComponent(PositionComponent.class, backgroundPosition);
            ECSManager.getInstance().addEntity(background);

            // Map and tiles
            Map gameMap = MapFactory.createMap(mapName);
            initializeMapEntities(gameMap);
            this.gameMap = gameMap;
            
            EnemySystem EnemySystem = new EnemySystem();
            AttackSystem attackSystem = new AttackSystem(gameMap);
            ECSManager.getInstance().addSystem(EnemySystem);
            ECSManager.getInstance().addSystem(attackSystem);
            
            // Initialize the Village-entity
            initializeVillage();
        } else {
            Entity screenTouch = new Entity();
            ButtonComponent button = new ButtonComponent(new Vector2(0,0), new Vector2(1,1), ButtonEnum.TILE, 0, () -> {
                System.out.println("Screen touched at: " + defaultControllers.inputController.getLastReleaseLocation().u + ", " + defaultControllers.inputController.getLastReleaseLocation().v);
            });
            screenTouch.addComponent(ButtonComponent.class, button);
            ECSManager.getInstance().addEntity(screenTouch);
            System.out.println("Added screen touch button");
        }


        // Buttons
        Entity backButton = ButtonFactory.createAndAddButtonEntity(ButtonEnum.BACK, new Vector2(0, 1),
                new Vector2(0.1f, 0.2f), this, 0);
        ECSManager.getInstance().addEntity(backButton);


        // TODO: Check if multiplayer and add multiplayer-specific button functionality
        // Card selection menu
        createCardSelectionMenu();


        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        MovementSystem MovementSystem = new MovementSystem();
        EnemySystem EnemySystem = new EnemySystem();
        AttackSystem attackSystem = new AttackSystem(gameMap);
        AnimationSystem animationSystem = new AnimationSystem();


        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
        ECSManager.getInstance().addSystem(MovementSystem);
        ECSManager.getInstance().addSystem(EnemySystem);
        ECSManager.getInstance().addSystem(attackSystem);
        ECSManager.getInstance().addSystem(animationSystem);

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

    public void initializeVillage() {
        Entity village = new Entity();
        HealthComponent healthComponent = new HealthComponent(1000);
        PlayerComponent playerComponent = new PlayerComponent(village.getId());
        MoneyComponent moneyComponent = new MoneyComponent(1000);
        PositionComponent villagePosition = new PositionComponent(new Vector2(0.80f, 0.90f), 1000);
        String textToDisplay = "Health: " + healthComponent.getHealth() + "\n Money: " + moneyComponent.getAmount();
        TextComponent villageHealthText = new TextComponent(textToDisplay, new Vector2(0.05f, 0.05f));

        villageHealthText.setColor(new Vector3(0f, 0f, 0f));
        village.addComponent(HealthComponent.class, healthComponent);
        village.addComponent(PlayerComponent.class, playerComponent);
        village.addComponent(MoneyComponent.class, moneyComponent);
        village.addComponent(PositionComponent.class, villagePosition);
        village.addComponent(TextComponent.class, villageHealthText);

        ECSManager.getInstance().addEntity(village);
        this.village = village;
    }

    private void initializeMapEntities(Map gameMap) {
        Tile[][] tiles = gameMap.getMapLayout();
        int numOfColumns = tiles.length;
        int numOfRows = tiles[0].length;

        float tileWidth = 1.0f / numOfColumns;
        float tileHeight = 1.0f / numOfRows;

        // Set tileWidth and tileHeight in the gameMap
        gameMap.setTileWidth(tileWidth);
        gameMap.setTileHeight(tileHeight);

        // Create Path entitiy
        List<Tile> enemyPath = gameMap.getPath();
        PathfindingComponent pathfindingComponent = new PathfindingComponent(enemyPath);
        Entity path = new Entity();
        path.addComponent(PathfindingComponent.class, pathfindingComponent);
        ECSManager.getInstance().addEntity(path);

        for (int i = 0; i < numOfColumns; i++) {
            for (int j = numOfRows - 1; j >= 0; j--) {
                final int finalI = i; // Create a final copy of i
                final int finalJ = j; // Create a final copy of j

                Entity tileEntity = new Entity();
                String tileTexture = gameMap.getTextureForTile(tiles[i][j]);

                Vector2 position = new Vector2(i * tileWidth, j * tileHeight);
                Vector2 size = new Vector2(tileWidth, tileHeight);
                SpriteComponent spriteComponent = new SpriteComponent(tileTexture.toString(), size);
                PositionComponent positionComponent = new PositionComponent(position, 1);
                TileComponent tileComponent = new TileComponent(tiles[i][j]); // Added

                // Create the callback-function for the button
                Runnable callback = () -> {
                    // Do action client side
                    handleTileClick(finalI, finalJ);
                };

                ButtonComponent buttonComponent = new ButtonComponent(position, size, ButtonEnum.TILE, 0, callback);

                tileEntity.addComponent(SpriteComponent.class, spriteComponent);
                tileEntity.addComponent(PositionComponent.class, positionComponent);
                tileEntity.addComponent(ButtonComponent.class, buttonComponent);
                tileEntity.addComponent(TileComponent.class, tileComponent); // Added
                ECSManager.getInstance().addEntity(tileEntity);

            }
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
        ECSManager.getInstance().addEntity(menuBackground);

        // Create buttons for each card type
        float buttonWidth = 0.065f;
        float buttonHeight = 0.185f;
        float gap = 0.005f;
        for (CardType type : CardType.values()) {
            Entity card = CardFactory.createCard(type, new Vector2(0, 0), false);
            Vector2 position2 = new Vector2(0.61f + type.ordinal() * (buttonWidth + gap), menuYPosition - 0.065f);
            Vector2 size = new Vector2(buttonWidth, buttonHeight);
            Entity button = createCardTypeButton(card, position2, size);
            ECSManager.getInstance().addEntity(button);
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
        cardSprite.setSizeUV(largerSize);

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

    // Callback-function for when a tile is clicked. Responsible for placing either
    // a card or a tower on the tile
    private void handleTileClick(int x, int y) {
        System.out.println("Clicked tile at position: (" + x + ", " + y + ")");
        Tile tile = gameMap.getMapLayout()[x][y];
        Entity tileEntity = getTileEntityByPosition(new Vector2(x, y));
        if (tileEntity == null)
            return; // Exit if there is no entity for this tile
        if (selectedCardType == null || !tile.isBuildable() || tile.hasTower()) {
            return;
        }
        

        // Card already placed, place tower
        if (tile.hasCard()) {
            System.out.println("Placing tower on tile at position (" + x + ", " + y + ")");
            PlacedCardComponent existingCardComponent = ECSManager.getInstance()
                    .getOrDefaultComponentManager(PlacedCardComponent.class).getComponent(tileEntity).get();
            CardType existingCardType = existingCardComponent.cardType;
            Optional<TowerType> towerType = PairableCards.getTower(selectedCardType, existingCardType);

            if (towerType.isPresent()) {
                // Remove the card thats already there
                ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class)
                        .removeComponent(tileEntity);
                Entity card = tile.getCard();
                ECSManager.getInstance().removeEntity(card);
                tile.removeCard();

                // Create the tower entity
                Entity towerEntity = TowerFactory.createTower(selectedCardType, existingCardType, new Vector2(x, y));

                // Update the tile with the new tower
                updateTileWithTower(tile, tileEntity, towerEntity);
            }
        }
        // No card on tile, place card
        else {
            System.out.println("Placing card on tile at position (" + x + ", " + y + ")");
            // Add a PlacedCardComponent the Tile-entity (to keep track of the cards' type)
            PlacedCardComponent placedCardComponent = new PlacedCardComponent(selectedCardType);
            ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class).addComponent(tileEntity,
                    placedCardComponent);

            // Create the card entity
            Entity cardEntity = CardFactory.createCard(selectedCardType, new Vector2(x, y), true);

            // Update the tile with the new card
            updateTileWithCard(tile, tileEntity, cardEntity);

        }

        // Reset the position of all other cards
        for (Entity entity : cardButtonEntities) {
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class).get();
            positionComponent.position.y = -0.085f;

        }
        // Decrement the players' MoneyComponent with the cost of the card
        MoneyComponent playerBalance = village.getComponent(MoneyComponent.class).get();
        // TODO: Add the correct cost for each card type

        // Reset the selected card type after placing a card
        selectedCardType = null;
    }

    private void updateTileWithCard(Tile tile, Entity tileEntity, Entity cardEntity) {
        if (tileEntity != null) {
            centerAndResizeEntity(cardEntity, tileEntity, gameMap);
            tile.setCard(cardEntity);

            ECSManager.getInstance().addEntity(cardEntity);
        }
    }

    private void updateTileWithTower(Tile tile, Entity tileEntity, Entity towerEntity) {
        if (tileEntity != null) {
            centerAndResizeEntity(towerEntity, tileEntity, gameMap);
            tile.setTower(towerEntity);

            ECSManager.getInstance().addEntity(towerEntity);
        }
    }

    private void centerAndResizeEntity(Entity entityToPlace, Entity tileEntity, Map gameMap) {
        float padding = 0.05f; // 5% padding on each side

        float entityWidth = gameMap.getTileWidth();
        float entityHeight = gameMap.getTileHeight();
        if (entityToPlace.getComponent(PlacedCardComponent.class).isPresent()) {
            // Cards should be slightly smaller than towers
            entityWidth = gameMap.getTileWidth() * (1 - 2 * padding) * 0.9f;
            entityHeight = gameMap.getTileHeight() * (1 - 2 * padding) * 0.9f;
        }

        // Get the position of the tile (in UV-coordinates)
        PositionComponent tilePositionComponent = tileEntity.getComponent(PositionComponent.class).get();

        // Calculate the centered position for the card/tower within the tile
        Vector2 centeredPosition = new Vector2(
                tilePositionComponent.getPosition().x + padding * gameMap.getTileWidth(),
                tilePositionComponent.getPosition().y + padding * gameMap.getTileHeight() + entityHeight / 4);

        // Update the PositionComponent of the entity to place
        PositionComponent entityPositionComponent = entityToPlace.getComponent(PositionComponent.class).get();
        entityPositionComponent.position = centeredPosition;
        entityToPlace.addComponent(PositionComponent.class, entityPositionComponent);

        // Update the SpriteComponent of the entity to place
        SpriteComponent entitySpriteComponent = entityToPlace.getComponent(SpriteComponent.class).get();
        entitySpriteComponent.size_uv = new Vector2(entityWidth, entityHeight);
        entityToPlace.addComponent(SpriteComponent.class, entitySpriteComponent);
    }

    private Entity getTileEntityByPosition(Vector2 tilePosition) {
        float tileWidth = gameMap.getTileWidth();
        float tileHeight = gameMap.getTileHeight();

        for (Entity entity : ECSManager.getInstance().getEntities()) {
            if (entity.getComponent(TileComponent.class).isPresent()
                    && entity.getComponent(PositionComponent.class).isPresent()) {
                PositionComponent positionComponent = entity.getComponent(PositionComponent.class).get();
                // Convert the UV coordinates back to XY coordinates
                int xCoord = (int) (positionComponent.getPosition().x / tileWidth);
                int yCoord = (int) (positionComponent.getPosition().y / tileHeight);

                if (xCoord == (int) tilePosition.x && yCoord == tilePosition.y) {
                    return entity;

                }
            }
        }
        return null;
    }

}
