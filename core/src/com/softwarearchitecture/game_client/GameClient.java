package com.softwarearchitecture.game_client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.game_server.Map;
import com.softwarearchitecture.game_server.MapFactory;

public class GameClient {
    // private State currentState;
    private GraphicsController graphicsController;
    private InputSystem inputSystem;
    private Map currentMap;
    private SpriteBatch spritebatch;

    public GameClient(GraphicsController graphicsController, InputSystem inputSystem) throws IllegalArgumentException {
        this.graphicsController = graphicsController;
        this.inputSystem = inputSystem;
        this.spritebatch = new SpriteBatch();
        this.currentMap = MapFactory.createMap("Abyss", 10, 10);
        // currentState = new MainMenu();
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        spritebatch.begin();
        // Iterate through each tile in the map and draw it
        for (int i = 0; i < currentMap.getMapLayout().length; i++) {
            for (int j = 0; j < currentMap.getMapLayout()[i].length; j++) {
                Texture texture = currentMap.getTextureForTile(currentMap.getMapLayout()[i][j]);
                spritebatch.draw(texture, i * 32, j * 32); // Example positioning, adjust as needed
            }
        }
        spritebatch.end();
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
