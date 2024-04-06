package com.softwarearchitecture.networking.messaging;

import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_client.ClientMessagingController;
import com.softwarearchitecture.game_server.GameState;
import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.networking.persistence.DAO;
import com.softwarearchitecture.networking.persistence.DAOBuilder;

public class ClientMessenger implements ClientMessagingController {

    private DAO<UUID, GameState> dao;

    public ClientMessenger() {
        DAOBuilder<UUID, GameState> daoBuilder = new DAOBuilder<>();
        this.dao = daoBuilder.withRead().withUpdate().build();
    }
    
    @Override
    public Optional<GameState> joinGame(UUID gameID, UUID playerID) {
        // Check if the game exists.
        Optional<GameState> gameResponse = dao.get(gameID);
        if (gameResponse.isPresent()) {
            // If the game exists, add the player to the game.
            GameState game = gameResponse.get();

            boolean playerAdded = game.addPlayer(playerID);
            if (playerAdded) {
                // If the player was added, update the game in the database.
                dao.update(gameID, game);
                return Optional.of(game);
            }
        }
        // If the game does not exist, return an empty optional.
        return Optional.empty();
    }

    @Override
    public Optional<GameState> requestGameState(UUID gameID) {
        return dao.get(gameID);
    }

    @Override
    public void addAction(UUID gameID, PlayerInput action) {
        // Get the game state from the database.
        Optional<GameState> gameResponse = dao.get(gameID);
        if (gameResponse.isPresent()) {
            // If the game exists, add the action to the game.
            GameState game = gameResponse.get();
            game.addAction(action);
            // Update the game in the database.
            dao.update(gameID, game);
        }
    }
}
