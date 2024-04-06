package com.softwarearchitecture.game_server;

import java.util.List;
import java.util.UUID;

import com.softwarearchitecture.game_client.states.InGame;

public interface ServerMessagingController {
    public UUID createGame();

    public void setNewGameState(InGame gameState);

    public List<PlayerInput> getActions(UUID gameID);
}
