package com.softwarearchitecture.networking.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.softwarearchitecture.game_client.ClientMessagingController;
import com.softwarearchitecture.game_client.Score;
import com.softwarearchitecture.game_server.GameState;
import com.softwarearchitecture.game_server.PlayerInput;
import com.softwarearchitecture.networking.persistence.DAO;
import com.softwarearchitecture.networking.persistence.DAOBuilder;

public class ClientMessenger implements ClientMessagingController {

    private DAO<String, byte[]> gameDAO;
    private DAO<UUID, PlayerInput> actionDAO;
    private DAO<String, UUID> joinPlayerDAO;
    private DAO<String, String> gamesDAO;

    private DAO<String, Score> highscoreDAO;
    
    private final String JOIN_PREFIX = "JOIN";
    private final String GAME_PREFIX = "GAME";
    private final String HIGHSCORE_PREFIX = "HIGHSCORE";
        

    public ClientMessenger(boolean isMultiplayer) {
        this.gameDAO = new DAOBuilder(!isMultiplayer).build(String.class, byte[].class);
        this.actionDAO = new DAOBuilder(!isMultiplayer).build(UUID.class, PlayerInput.class);
        this.joinPlayerDAO = new DAOBuilder(!isMultiplayer).build(String.class, UUID.class);
        this.gamesDAO = new DAOBuilder(!isMultiplayer).build(String.class, String.class);
        this.highscoreDAO = new DAOBuilder(!isMultiplayer).build(String.class, Score.class);
    }
    
    @Override
    public boolean joinGame(UUID gameID, UUID playerID) {
       synchronized (this) {
            joinPlayerDAO.add(createJoinGameId(gameID), playerID);
            System.out.println("Joining game: " + gameID + " with player: " + playerID);
            
            // Give server time before checking server 
            try {
                wait(1000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                e.printStackTrace();
            }
            // Check with server
            Optional<byte[]> data = gameDAO.get(createGameId(gameID));
            if (data.isPresent()) {
                try {
                    GameState gameState = GameState.deserializeFromByteArray(data.get());
                    System.out.println("Player one: " + gameState.playerOne);
                    System.out.println("Player two: " + gameState.playerTwo);
                    System.out.println("Player ID: " + playerID);
                    
                    boolean correctGameId =  gameState.gameID.equals(gameID);
                    boolean hasJoined = gameState.playerTwo != null;
                    return correctGameId && hasJoined;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return false; 
    }

    private class GameStateWrapper implements Runnable {

        private volatile byte[] data;
        private volatile GameState gameState = null;

        public GameStateWrapper(byte[] data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                gameState = GameState.deserializeFromByteArray(data);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public List<GameState> getAllAvailableGames() {
        List<String> indexes = gamesDAO.loadAllIndices();
        List<GameState> games = new ArrayList<>();
        for (String index : indexes) {
            if (index.contains(GAME_PREFIX)) {
                try {
                    
                    Optional<byte[]> data = gameDAO.get(index);
                    
                    if (data.isPresent()) {
                        GameStateWrapper getter = new GameStateWrapper(data.get());
                        Thread thread = new Thread(getter);
                        thread.start();
                        thread.join();
                        GameState gameState = getter.gameState;
                        if (gameState != null && gameState.playerTwo == null) {
                            games.add(gameState);
                        }
                    }
                } catch (InterruptedException i) {
                    i.printStackTrace();
                }
            }
        }
        return games;
    }

    @Override
    public Optional<GameState> requestGameState(UUID gameID) {
        Optional<byte[]> data = gameDAO.get(createGameId(gameID));
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

    private String createGameId(UUID gameId) {
        return GAME_PREFIX + gameId.toString();
    }
    
    private String createJoinGameId(UUID gameId) {
        return JOIN_PREFIX + gameId.toString();
    }


    @Override
    public void addAction(PlayerInput action) {
        actionDAO.add(action.getPlayerId(), action);
    }


    @Override
    public List<Score> getAllHighScores() {
        List<Score> scores = new ArrayList<>();
        for (String index : highscoreDAO.loadAllIndices()) {
            if (!index.contains(HIGHSCORE_PREFIX)) {
                continue;
            }
            Optional<Score> possibleScore = highscoreDAO.get(index);
            if (possibleScore.isPresent()) {
                Score score = possibleScore.get();
                scores.add(score);
            }
        }
        return scores;
    }
}
