package com.softwarearchitecture.game_client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.TouchLocation;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.game_server.MapFactory;
import com.softwarearchitecture.game_server.Tile;
import com.softwarearchitecture.game_server.TowerFactory;
import com.softwarearchitecture.math.Vector2;

public class GameClient {
    // private State currentState;
    private GraphicsController graphicsController;
    private InputSystem inputSystem;
    private Map currentMap;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    public GameClient(GraphicsController graphicsController, InputSystem inputSystem) throws IllegalArgumentException {
        this.graphicsController = graphicsController;
        this.inputSystem = inputSystem;
        this.spriteBatch = new SpriteBatch();
        this.currentMap = MapFactory.createMap("Abyss");
        this.shapeRenderer = new ShapeRenderer();
        // currentState = new MainMenu();
    }

    public void handleInput() {
        TouchLocation touch = inputSystem.getLastTouched();
        if (touch != null) {
            // Convert touch location to map tile coordinates
            int numberOfXTiles = currentMap.getMapLayout().length;
            int numberOfYTiles = currentMap.getMapLayout()[0].length;
    
            int tileX = (int) (touch.u * numberOfXTiles);
            // Invert the y-axis to match the map layout, -1 for 0-based index
            int tileY = numberOfYTiles - (int) ((1 - touch.v) * numberOfYTiles) - 1; 
    
            // Check if within bounds and tile is buildable
            if (tileX >= 0 && tileX < numberOfXTiles && tileY >= 0 && tileY < numberOfYTiles) {
                Tile tile = currentMap.getMapLayout()[tileX][tileY];
                if (tile.isBuildable() && !tile.hasTower()) {
                    // Create and place a FIRE_MAGIC tower
                    Entity tower = TowerFactory.createTower(CardType.MAGIC, CardType.FIRE, new Vector2(tileX, tileY)); // Adjust parameters as needed
                    tile.setTower(tower);

                    // Assuming the Entity has a SpriteComponent with the texture path
                    String towerTexture = tower.getComponent(SpriteComponent.class).texture_path; // Simplified; actual method to access components may differ
                    tile.setCardOrTowerTexturePath(towerTexture); // New method in Tile class to store tower texture

                    System.out.println("Tower placed at (" + tileX + ", " + tileY + ")");
                }
            }
            inputSystem.clearLastTouched(); // Reset the last touch location after handling
        }
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        spriteBatch.begin();
    
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int numberOfXTiles = currentMap.getMapLayout().length;
        int numberOfYTiles = currentMap.getMapLayout()[0].length;
    
        // Calculate tile size
        float tileSizeW = screenWidth / (float) numberOfXTiles;
        float tileSizeH = screenHeight / (float) numberOfYTiles;
        
        for (int i = 0; i < currentMap.getMapLayout().length; i++) {
            for (int j = 0; j < currentMap.getMapLayout()[i].length; j++) {
                Tile tile = currentMap.getMapLayout()[i][j];
                Texture texture;
                if (tile.hasTower()) {
                    texture = new Texture(tile.getCardOrTowerTexturePath()); // Assumes getTowerTexturePath() returns the path
                } else {
                    texture = currentMap.getTextureForTile(tile);
                }
                spriteBatch.draw(texture, i * tileSizeW, j * tileSizeH, tileSizeW, tileSizeH);
            }
        }
        spriteBatch.end();

        // Set up shape renderer for drawing borders
        Gdx.gl.glLineWidth(1); // Set the line width for the border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK); // Set the color to black

        // Draw borders around each tile
        for (int i = 0; i < currentMap.getMapLayout().length; i++) {
            for (int j = 0; j < currentMap.getMapLayout()[i].length; j++) {
                shapeRenderer.rect(i * tileSizeW, j * tileSizeH, tileSizeW, tileSizeH);
            }
        }
        shapeRenderer.end();
    }
    

    public void init() {
        // currentState.init(graphicsController);
    }

    public void update() {
        float deltaTime = 1f; // TODO: Implement deltatime
        handleInput();
        render();
        // currentState.update(deltaTime);
    }

    public void dispose() {
        // currentState.dispose();
    }
}
