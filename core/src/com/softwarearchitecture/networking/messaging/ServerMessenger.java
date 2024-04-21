package com.softwarearchitecture.networking.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.game_server.ServerMessagingController;
import com.softwarearchitecture.networking.persistence.DAO;
import com.softwarearchitecture.networking.persistence.DAOFactory;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.game_client.Score;
import com.softwarearchitecture.game_server.GameState;

public class ServerMessenger implements ServerMessagingController {

    private DAO<String, byte[]> gameDao;
    private DAO<String, UUID> pendingPlayerDao;
    private DAO<UUID, PlayerInput> actionDao;
    private DAO<String, Score> highscoreDao;
    
    private final String JOIN_PREFIX = "JOIN";
    private static final String GAME_PREFIX = "GAME";
    private final String HIGHSCORE_PREFIX = "HIGHSCORE";

    public ServerMessenger(boolean isMultiplayer) {
        gameDao = new DAOFactory<String, byte[]>(!isMultiplayer).build(String.class, byte[].class);
        pendingPlayerDao = new DAOFactory<String, UUID>(!isMultiplayer).build(String.class, UUID.class);
        actionDao = new DAOFactory<UUID, PlayerInput>(!isMultiplayer).build(UUID.class, PlayerInput.class);
        highscoreDao = new DAOFactory<String, Score>(!isMultiplayer).build(String.class, Score.class);
    }

    @Override
    public UUID createGame(String mapName) {
        UUID gameId = UUID.randomUUID();
        UUID playerID = UUID.randomUUID();
        // Create a player entity for player one
        Entity playerOne = new Entity();
        playerOne.addComponent(PlayerComponent.class, new PlayerComponent(playerID));
        GameState gameState = new GameState();
        gameState.playerOne = playerOne;
        gameState.gameID = gameId;
        gameState.mapName = mapName;
        

        System.out.println("The gameStates player one" + gameState.playerOne);
        try {
            byte[] gameOutput = GameState.serializeToByteArray(gameState);
            gameDao.add(createGameId(gameId), gameOutput);
            
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Error creating game");
        }
        return gameState.gameID;
    }

    private String createGameId(UUID gameId) {
        return GAME_PREFIX + gameId.toString();
    }
    
    private String createJoinGameId(UUID gameId) {
        return JOIN_PREFIX + gameId.toString();
    }
    
    private String createHighScoreId(UUID gameId) {
        return HIGHSCORE_PREFIX + gameId.toString();
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
            System.out.println(e);
            System.out.println("Error getting game state with ID: " + gameId);
            return null;
        }
    }

    @Override
    public void setNewGameState(UUID gameId, GameState gameState) {
        try {
            byte[] gameOutput = GameState.serializeToByteArray(gameState);
            gameDao.update(createGameId(gameId), gameOutput);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Error updating game state with ID: " + gameId);
        }
    }

    @Override
    public List<PlayerInput> lookForPendingActions(UUID playerId) {
        List<PlayerInput> actions = new ArrayList<>();
        Optional<PlayerInput> action = actionDao.get(playerId);
        if (action.isPresent()) {
            actions.add(action.get());
            actionDao.delete(playerId);
        }
        return actions;
    }
    

    @Override
    public Optional<UUID> lookForPendingPlayer(UUID gameId) {
        String lookingId = createJoinGameId(gameId);
        return pendingPlayerDao.get(lookingId);
    }

    @Override
    public void removeGame(UUID gameId) {
        gameDao.delete(createGameId(gameId));
    }

    @Override
    public void setHighScore(UUID gameId, int wavesSurvived) {
        Score score = new Score(gameId, wavesSurvived);
        highscoreDao.add(createHighScoreId(gameId), score);
    }
}
