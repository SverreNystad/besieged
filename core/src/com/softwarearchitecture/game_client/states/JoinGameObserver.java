package com.softwarearchitecture.game_client.states;

import com.softwarearchitecture.game_server.GameState;

public interface JoinGameObserver {
    
    public void onJoinGame(GameState gameName);
}
