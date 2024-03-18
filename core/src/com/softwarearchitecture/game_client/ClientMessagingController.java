package com.softwarearchitecture.game_client;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.game_server.states.GameState;

public interface ClientMessagingController {
    public Optional<GameState> joinGame(UUID gameID, UUID playerID);

    public Optional<GameState> requestGameState(UUID gameID);

    public void addAction(UUID gameID, PlayerInput action);
}
