package com.softwarearchitecture.networking.messaging;

import java.io.IOException;
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
        DAOBuilder<UUID, byte[]> gameDaoBuilder = new DAOBuilder<>();
        DAOBuilder<UUID, String> actionDaoBuilder = new DAOBuilder<>();
        DAOBuilder<String, UUID> joinPlayerDaoBuilder = new DAOBuilder<>();
        this.gameDAO = gameDaoBuilder.withRead().withUpdate().build(UUID.class, byte[].class);
        this.actionDAO = actionDaoBuilder.withCreate().build(UUID.class, String.class);
        this.joinPlayerDAO = joinPlayerDaoBuilder.withCreate().build(String.class, UUID.class);
    }
    
    @Override
    public void joinGame(UUID gameID, UUID playerID) {
        joinPlayerDAO.add(gameID.toString() + "JOIN", playerID);
    }

    @Override
    public Optional<GameState> requestGameState(UUID gameID) {
        Optional<byte[]> data = gameDAO.get(gameID);
        if (data.isPresent()) {
            try {
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
