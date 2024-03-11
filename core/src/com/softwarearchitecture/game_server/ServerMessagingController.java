package com.softwarearchitecture.game_server;

import java.util.List;
import java.util.UUID;

public interface ServerMessagingController {
    public UUID createGame();
    public void setNewGameState(GameState gameState);
    public List<PlayerInput> getActions(UUID gameID);
}
