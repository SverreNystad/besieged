package com.softwarearchitecture.game_client.states;

import java.util.UUID;

import java.util.List;
import java.util.Optional;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.TileComponentManager;
import com.softwarearchitecture.ecs.components.ButtonComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_server.CardFactory;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.game_server.MapFactory;
import com.softwarearchitecture.game_server.PairableCards;
import com.softwarearchitecture.game_server.TexturePack;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TowerFactory;
import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.math.Vector3;

public class InGame extends State implements Observer {

    private Map gameMap;
    private CardType selectedCardType = null;

    protected InGame(Controllers defaultControllers, UUID yourId) {
        super(defaultControllers, yourId);
    }
    
    @Override
    protected void activate() {
        // Background
        String backgroundPath = TexturePack.BACKGROUND_TOR;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0, 0), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        TextComponent textComponent = new TextComponent("In Game!", new Vector2(0.05f, 0.05f));
        background.addComponent(TextComponent.class, textComponent);
        ECSManager.getInstance().addEntity(background);
        
        // Buttons
        Entity backButton = ButtonFactory.createAndAddButtonEntity(TypeEnum.BACK, new Vector2(0, 1), new Vector2(0.1f, 0.2f), this, 0);
        ECSManager.getInstance().addEntity(backButton);

        // Map and tiles
        Map gameMap = MapFactory.createMap("TestMap");
        initializeMapEntities(gameMap);
        this.gameMap = gameMap;

        // Card selection menu
        createCardSelectionMenu();
        
        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
    }

    @Override
    public void onAction(TypeEnum type) {
        switch (type) {
            case BACK:
                ScreenManager.getInstance().previousState();
                break;
            default:
                break;
        }
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

                Vector2 position = new Vector2(i * tileWidth, j* tileHeight);
                Vector2 size = new Vector2(tileWidth, tileHeight);
                SpriteComponent spriteComponent = new SpriteComponent(tileTexture.toString(), size);
                PositionComponent positionComponent = new PositionComponent(position, 1);
                TileComponent tileComponent = new TileComponent(tiles[i][j]); // Added
                
                //Create the callback-function for the button
                Runnable callback = () -> {
                    handleTileClick(finalI, finalJ);
                };

                ButtonComponent buttonComponent = new ButtonComponent(position, size, TypeEnum.TILE, 0, callback);
    
                tileEntity.addComponent(SpriteComponent.class, spriteComponent);
                tileEntity.addComponent(PositionComponent.class, positionComponent);
                tileEntity.addComponent(ButtonComponent.class, buttonComponent);
                tileEntity.addComponent(TileComponent.class, tileComponent); // Added
                ECSManager.getInstance().addEntity(tileEntity);

                // Register the TileComponent with the TileComponentManager
                TileComponentManager tileManager = ECSManager.getInstance().getTileComponentManager();
                tileManager.addComponent(tileEntity, tileComponent);

            }
        }
    }

    private void createCardSelectionMenu() {
        float menuYPosition = 0; // Position the menu at the bottom of the screen
        float menuHeight = 0.2f; // Height of the menu
        String menuBackgroundTexture = TexturePack.COLOR_WHITE;
    
        // Create menu background entity
        Entity menuBackground = new Entity();
        SpriteComponent menuBackgroundSprite = new SpriteComponent(menuBackgroundTexture, new Vector2(1, menuHeight));
        Vector2 position1 = new Vector2(0, menuYPosition);
        PositionComponent menuBackgroundPosition = new PositionComponent(position1, 0);
        menuBackground.addComponent(SpriteComponent.class, menuBackgroundSprite);
        menuBackground.addComponent(PositionComponent.class, menuBackgroundPosition);
        ECSManager.getInstance().addEntity(menuBackground);
    
        // Create buttons for each card type
        float buttonWidth = 1.0f / CardType.values().length - 0.05f;
        float buttonHeight = menuHeight - 0.1f;
        for (CardType type : CardType.values()) {
            Vector2 position2 = new Vector2(type.ordinal() * buttonWidth, menuYPosition);
            Vector2 size = new Vector2(buttonWidth, buttonHeight);
            Entity button = createCardTypeButton(type, position2, size);
            ECSManager.getInstance().addEntity(button);
        }
    }

    // Method to create a button for a card type
    private Entity createCardTypeButton(CardType cardType, Vector2 position, Vector2 size) {
        Entity buttonEntity = new Entity();

        // Define the PositionComponent for the button
        PositionComponent buttonPositionComponent = new PositionComponent(position, 2);

        TextComponent buttonText = new TextComponent(cardType.name(), new Vector2(size.x / 2, size.y / 2)); // Text centered within button
        buttonText.setColor(new Vector3(0f, 0f, 0f)); // Set text color to black

        // Button component also has a callback now
        Runnable onButtonClick = () -> {
            System.out.println("Selected card type: " + cardType.name());
            selectedCardType = cardType;
        };
        ButtonComponent buttonComponent = new ButtonComponent(position, size, TypeEnum.CARD, 1, onButtonClick);

        // Add the PositionComponent to the button entity
        buttonEntity.addComponent(PositionComponent.class, buttonPositionComponent);
        buttonEntity.addComponent(TextComponent.class, buttonText);
        buttonEntity.addComponent(ButtonComponent.class, buttonComponent);

        return buttonEntity;
}

    
    // Callback-function for when a tile is clicked. Responsible for placing either a card or a tower on the tile
    private void handleTileClick(int x, int y) {
        System.out.println("Clicked tile at position: (" + x + ", " + y + ")");
        Tile tile = gameMap.getMapLayout()[x][y];
        Entity tileEntity = getTileEntityByPosition(new Vector2(x, y));
        if (tileEntity == null) return; // Exit if there is no entity for this tile

        System.out.println("Tile is buildable: " + tile.isBuildable());
        System.out.println("Tile has tower: " + tile.hasTower());
    
        if (selectedCardType == null || !tile.isBuildable() || tile.hasTower()) {
            return;
        }
        
        if (tile.hasCard()) {
            System.out.println("Placing tower on tile at position (" + x + ", " + y + ")");
            // There is already one card placed, try to combine them into a tower
            PlacedCardComponent existingCardComponent = ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class).getComponent(tileEntity).get();
            CardType existingCardType = existingCardComponent.cardType;
            Optional<PairableCards.TowerType> towerType = PairableCards.getTower(selectedCardType, existingCardType);
            if (towerType.isPresent()) {
                // Remove the cards
                tile.removeCard();
                ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class).removeComponent(tileEntity);

                // Create and place the tower
                Entity tower = TowerFactory.createTower(selectedCardType, existingCardType, new Vector2(x, y));
                ECSManager.getInstance().addEntity(tower);
                tile.setTower(tower);

                // Update the tile with the new tower
                updateTileWithTower(tile, tower);
            }
        } else {
            System.out.println("Placing card on tile at position (" + x + ", " + y + ")");
            Entity card = CardFactory.createCard(selectedCardType, new Vector2(x, y));
            tile.setCard(card);
            ECSManager.getInstance().toAdd(card);
            // Assuming there's a method to get the component manager and add a component to it
            // ComponentManager<PlacedCardComponent> cardManager = ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class);
            // PlacedCardComponent placedCardComponent = new PlacedCardComponent(x, y, selectedCardType);
            // cardManager.addComponent(tileEntity, placedCardComponent);

        }

        // Reset the selected card type after placing a card
        selectedCardType = null;
        System.out.println("[DEBUG] Selected card type reset to null");
}

    private void updateTileWithTower(Tile tile, Entity tower) {
        Entity tileEntity = getTileEntityByPosition(new Vector2(tile.getX(), tile.getY()));
        if (tileEntity != null && tower.getComponent(SpriteComponent.class).isPresent()) {
            SpriteComponent spriteComponent = tower.getComponent(SpriteComponent.class).get();
            if (spriteComponent != null) {
                tileEntity.addComponent(SpriteComponent.class, spriteComponent);
            }
        }
    }

    private Entity getTileEntityByPosition(Vector2 tilePosition) {
        int mapWidthInTiles = gameMap.getMapWidth();  // Total number of tiles in X direction
        int mapHeightInTiles = gameMap.getMapHeight();  // Total number of tiles in Y direction
    
        for (Entity entity : ECSManager.getInstance().getEntities()) {
            if (entity.getComponent(TileComponent.class).isPresent() && entity.getComponent(PositionComponent.class).isPresent()) {
                PositionComponent positionComponent = entity.getComponent(PositionComponent.class).get();
                // Convert the UV coordinates back to XY coordinates
                int xCoord = (int) (positionComponent.getPosition().x * mapWidthInTiles);
                int yCoord = (int) (positionComponent.getPosition().y * mapHeightInTiles);
                
                if (xCoord == (int) tilePosition.x && yCoord == tilePosition.y) {
                    return entity;
                    
                }
            }
        }
        return null;
    }
    
    
    

}