package com.softwarearchitecture.game_client.states;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;

public class ScreenManager {
    /**
     * Keeps track of the current state of the game
     */
    private State currentState;
    private Boolean stateChanged;
    private Deque<State> stateStack = new ArrayDeque<>();
    private static ScreenManager instance = new ScreenManager();
    private UUID gameId;
    private boolean isLocalServer = false;
    
    public static ScreenManager getInstance() {
        return instance;
    }

    public void activateCurrentStateIfChanged() {
        if (stateChanged) {
            currentState.init();
            stateChanged = false;
        }
    }

    public void nextState(State state) {
        ECSManager.getInstance().clearLocalEntities();
        if (this.currentState != null) saveState(this.currentState);
        this.currentState = state;
        stateChanged = true;
    }

    private void saveState(State state) {
        this.stateStack.push(state);
    }

    public void previousState() {
        if (!this.stateStack.isEmpty()) {
            this.currentState = this.stateStack.pop();
            stateChanged = true;
        }
    }

    public void flushPreviousStates() {
        this.stateStack.clear();
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getGameId() {
        return this.gameId;
    }

    public boolean isLocalServer() {
        return isLocalServer;
    }

    public void setIsLocalServer(boolean isLocal) {
        this.isLocalServer = isLocal;
    }

    public <T> boolean isCurrentStateOfType(Class<T> classType) {
        return classType.isInstance(currentState);
    }
}
