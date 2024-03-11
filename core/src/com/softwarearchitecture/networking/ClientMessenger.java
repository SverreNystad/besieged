package com.softwarearchitecture.networking;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_client.ClientMessagingController;
import com.softwarearchitecture.game_server.GameState;
import com.softwarearchitecture.game_server.PlayerInput;

public class ClientMessenger implements ClientMessagingController {

    @Override
    public Optional<GameState> joinGame(UUID gameID, UUID playerID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'joinGame'");
    }

    @Override
    public Optional<GameState> requestGameState(UUID gameID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestGameState'");
    }

    @Override
    public void addAction(UUID gameID, PlayerInput action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAction'");
    }

}
