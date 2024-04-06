package com.softwarearchitecture.game_server.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenManager {
    /**
     * Keeps track of the current state of the game
     */
    private State currentState;
    private State savedState; // TODO: might remove later
    private static ScreenManager instance = new ScreenManager();

    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        return instance;
    }

    public void nextState(State state) {
        this.currentState = state;
    }

    public void saveState(State state) {
        this.savedState = state;
    }

    public void previousState() {
        if (this.savedState != null) {
            this.currentState = this.savedState;
        }

    }

    public void handleInput() {
        this.currentState.handleInput();
    }

    public void update(float deltaTime) {
        this.currentState.update(deltaTime);
    }

    public void render(SpriteBatch spriteBatch) {
        currentState.render(spriteBatch);
    }

}
