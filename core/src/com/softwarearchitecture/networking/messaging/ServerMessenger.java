package com.softwarearchitecture.networking.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.UUID;

import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.game_server.ServerMessagingController;
import com.softwarearchitecture.networking.persistence.DAO;
import com.softwarearchitecture.networking.persistence.DAOBuilder;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.game_server.GameState;

public class ServerMessenger implements ServerMessagingController {

    private DAO<UUID, byte[]> dao;

    public ServerMessenger() {
        dao = new DAOBuilder<UUID, byte[]>().withCreate().withRead().withUpdate().build(UUID.class, byte[].class);
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
            byte[] gameOutput = this.serializeToByteArray(gameState);
            dao.add(gameID, gameOutput);
            
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Error creating game");
        }
        return gameID;
    }

    private byte[] serializeToByteArray(GameState state) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(state);
        oos.close();
        return baos.toByteArray();
    }

    @Override
    public void setNewGameState(UUID id, GameState gameState) {
        try {
            byte[] gameOutput = this.serializeToByteArray(gameState);
            dao.update(id, gameOutput);
        } catch (IOException e) {
            System.out.println("Error updating game state with ID: " + id);
        }
    }

    @Override
    public List<PlayerInput> getActions(UUID gameID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActions'");
    }

}
