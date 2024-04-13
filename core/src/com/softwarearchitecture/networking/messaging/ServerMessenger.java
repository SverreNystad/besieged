package com.softwarearchitecture.networking.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.game_server.ServerMessagingController;
import com.softwarearchitecture.networking.persistence.DAO;
import com.softwarearchitecture.networking.persistence.DAOBuilder;
import com.badlogic.gdx.Game;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.game_server.GameState;

public class ServerMessenger implements ServerMessagingController {

    private DAO<UUID, byte[]> gameDao;
    private DAO<String, UUID> pendingPlayerDao;

    public ServerMessenger() {
        gameDao = new DAOBuilder<UUID, byte[]>().withCreate().withRead().withUpdate().build(UUID.class, byte[].class);
        pendingPlayerDao = new DAOBuilder<String, UUID>().withCreate().build(String.class, UUID.class);
    }

    @Override
    public UUID createGame() {
        UUID gameID = UUID.randomUUID();
        UUID playerID = UUID.randomUUID();
        GameState gameState = new GameState();
        // Create a player entity for player one
        gameState.playerOne = new Entity();
        gameState.playerOne.addComponent(PlayerComponent.class, new PlayerComponent(playerID));

        try {
            byte[] gameOutput = GameState.serializeToByteArray(gameState);
            gameDao.add(gameID, gameOutput);
            
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Error creating game");
        }
        return gameID;
    }

    @Override
    public GameState getGameState(UUID gameID) {
        try {
            Optional<byte[]> gameOutput = gameDao.get(gameID);
            if (!gameOutput.isPresent()) {
                return null;
            }
            return GameState.deserializeFromByteArray(gameOutput.get());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error getting game state with ID: " + gameID);
            return null;
        }
    }

    @Override
    public void setNewGameState(UUID id, GameState gameState) {
        try {
            byte[] gameOutput = GameState.serializeToByteArray(gameState);
            gameDao.update(id, gameOutput);
        } catch (IOException e) {
            System.out.println("Error updating game state with ID: " + id);
        }
    }

    @Override
    public List<PlayerInput> getActions(UUID gameID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActions'");
    }

    @Override
    public Optional<UUID> lookForPendingPlayer(UUID gameID) {
        String lookingId = gameID.toString() + "JOIN";
        return pendingPlayerDao.get(lookingId);
    }

}
