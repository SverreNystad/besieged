package com.softwarearchitecture.networking.messaging;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.game_server.ServerMessagingController;
import com.softwarearchitecture.networking.persistence.DAO;
import com.softwarearchitecture.networking.persistence.DAOBuilder;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.game_server.GameState;

public class ServerMessenger implements ServerMessagingController {

    private DAO<String, byte[]> gameDao;
    private DAO<String, UUID> pendingPlayerDao;
    private static final String GAME_PREFIX = "GAME";

    public ServerMessenger() {
        gameDao = new DAOBuilder<String, byte[]>().build(String.class, byte[].class);
        pendingPlayerDao = new DAOBuilder<String, UUID>().build(String.class, UUID.class);
    }

    @Override
    public UUID createGame() {
        UUID gameId = UUID.randomUUID();
        UUID playerID = UUID.randomUUID();
        GameState gameState = new GameState();
        // Create a player entity for player one
        gameState.playerOne = new Entity();
        gameState.playerOne.addComponent(PlayerComponent.class, new PlayerComponent(playerID));

        try {
            byte[] gameOutput = GameState.serializeToByteArray(gameState);
            gameDao.add(createGameId(gameId), gameOutput);
            
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Error creating game");
        }
        return gameId;
    }

    private String createGameId(UUID gameId) {
        return GAME_PREFIX + gameId.toString();
    }

    @Override
    public GameState getGameState(UUID gameId) {
        try {
            Optional<byte[]> gameOutput = gameDao.get(createGameId(gameId));
            if (!gameOutput.isPresent()) {
                return null;
            }
            return GameState.deserializeFromByteArray(gameOutput.get());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error getting game state with ID: " + gameId);
            return null;
        }
    }

    @Override
    public void setNewGameState(UUID id, GameState gameState) {
        try {
            byte[] gameOutput = GameState.serializeToByteArray(gameState);
            gameDao.update(createGameId(id), gameOutput);
        } catch (IOException e) {
            System.out.println("Error updating game state with ID: " + id);
        }
    }

    @Override
    public List<PlayerInput> lookForPendingActions(UUID gameId) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    @Override
    public Optional<UUID> lookForPendingPlayer(UUID gameId) {
        String lookingId = gameId.toString() + "JOIN";
        return pendingPlayerDao.get(lookingId);
    }
}
