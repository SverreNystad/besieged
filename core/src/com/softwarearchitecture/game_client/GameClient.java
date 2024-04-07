package com.softwarearchitecture.game_client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.game_server.MapFactory;

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
    
        // Iterate through each tile in the map and draw it
        for (int i = 0; i < currentMap.getMapLayout().length; i++) {
            for (int j = 0; j < currentMap.getMapLayout()[i].length; j++) {
                Texture texture = currentMap.getTextureForTile(currentMap.getMapLayout()[i][j]);
                // Adjust the draw call to use the dynamically calculated tile size
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
        render();
        // currentState.update(deltaTime);
    }

    public void dispose() {
        // currentState.dispose();
    }
}
