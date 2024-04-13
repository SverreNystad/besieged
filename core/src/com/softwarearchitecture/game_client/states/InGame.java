package com.softwarearchitecture.game_client.states;

import com.badlogic.gdx.graphics.Texture;
import com.softwarearchitecture.ecs.Controllers;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.game_server.MapFactory;
import com.softwarearchitecture.game_server.TexturePack;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TowerFactory;
import com.softwarearchitecture.math.Vector2;

public class InGame extends State implements Observer {

    private Map gameMap;

    protected InGame(Controllers defaultControllers) {
        super(defaultControllers);
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
        Map gameMap = MapFactory.createMap("Abyss");
        initializeMapEntities(gameMap);
        this.gameMap = gameMap;
        
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

        for (int i = 0; i < numOfColumns; i++) {
            for (int j = numOfRows - 1; j >= 0; j--) {
                final int finalI = i; // Create a final copy of i
                final int finalJ = j; // Create a final copy of j
    
                Entity tileEntity = new Entity();
                Texture tileTexture = gameMap.getTextureForTile(tiles[i][j]);

                Vector2 position = new Vector2(i * tileWidth, j* tileHeight);
                Vector2 size = new Vector2(tileWidth, tileHeight);
                SpriteComponent spriteComponent = new SpriteComponent(tileTexture.toString(), size);
                PositionComponent positionComponent = new PositionComponent(position, 0);
                
                //Create the callback-function for the button
                Runnable callback = () -> {
                    handleTileClick(finalI, finalJ);
                };

                ButtonComponent buttonComponent = new ButtonComponent(position, size, TypeEnum.TILE, 0, callback);
    
                tileEntity.addComponent(SpriteComponent.class, spriteComponent);
                tileEntity.addComponent(PositionComponent.class, positionComponent);
                tileEntity.addComponent(ButtonComponent.class, buttonComponent);
                ECSManager.getInstance().addEntity(tileEntity);
    
                // Store the entity reference in the Tile for easy access later
                tiles[i][j].setEntity(tileEntity);
            }
        }
    }
    
    // Callback-function for when a tile is clicked
    private void handleTileClick(int x, int y) {
        Tile tile = gameMap.getMapLayout()[x][y];
        if (tile.isBuildable() && !tile.hasTower()) {
            Entity tower = TowerFactory.createTower(CardType.MAGIC, CardType.FIRE, new Vector2(x, y));
            tile.setTower(tower);
    
            // Update the tile entity with the new tower texture by replacing the old texture
            Vector2 size = new Vector2(gameMap.getTileWidth(), gameMap.getTileHeight());
            SpriteComponent spriteComponent = new SpriteComponent(TexturePack.FIRE_MAGIC, size);
            // Remove the old sprite component and add the new one
            tile.getEntity().removeComponent(SpriteComponent.class);
            tile.getEntity().addComponent(SpriteComponent.class, spriteComponent);
            }
    }
    
    

}
