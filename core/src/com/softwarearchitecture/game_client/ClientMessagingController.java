package com.softwarearchitecture.game_client;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.game_server.states.InGame;

public interface ClientMessagingController {
    public Optional<InGame> joinGame(UUID gameID, UUID playerID);

    public Optional<InGame> requestGameState(UUID gameID);

    public void addAction(UUID gameID, PlayerInput action);
}
