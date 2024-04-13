package com.softwarearchitecture.networking.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_client.ClientMessagingController;
import com.softwarearchitecture.game_server.GameState;
import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.networking.persistence.DAO;
import com.softwarearchitecture.networking.persistence.DAOBuilder;

public class ClientMessenger implements ClientMessagingController {

    private DAO<UUID, byte[]> gameDAO;
    private DAO<UUID, String> actionDAO;
    private DAO<String, UUID> joinPlayerDAO;

    public ClientMessenger() {
        this.gameDAO = new DAOBuilder().build(UUID.class, byte[].class);
        this.actionDAO = new DAOBuilder().build(UUID.class, String.class);
        this.joinPlayerDAO = new DAOBuilder().build(String.class, UUID.class);
    }
    
    @Override
    public void joinGame(UUID gameID, UUID playerID) {
        joinPlayerDAO.add(gameID.toString() + "JOIN", playerID);
    }

    public List<GameState> getAllAvailableGames() {
        List<byte[]> rawGames = gameDAO.loadAll();
        List<GameState> games = new ArrayList<>();
        for (byte[] rawGame : rawGames) {
            try {
                games.add(GameState.deserializeFromByteArray(rawGame));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return games;
    }

    @Override
    public Optional<GameState> requestGameState(UUID gameID) {
        Optional<byte[]> data = gameDAO.get(gameID);
        if (data.isPresent()) {
            try {
                // TODO: ADD LOGIC FOR NOT ADDING GAMES THAT ARE FULL
                GameState gameState = GameState.deserializeFromByteArray(data.get());
                return Optional.of(gameState);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
    

    @Override
    public void addAction(PlayerInput action) {
        actionDAO.add(action.getPlayerID(), action.getAction());
    }
}
