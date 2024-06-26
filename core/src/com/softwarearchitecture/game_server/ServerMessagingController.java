package com.softwarearchitecture.game_server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerMessagingController {

    /**
     * Create a new game and return the game ID.
     * 
     * @return the game ID of the new game
     */
    public UUID createGame(String mapName);

    /**
     * Get the game state of the game with the given game ID.
     * 
     * @param gameId of the game to get the game state of
     * @return the game state of the game with the given game ID
     */
    public GameState getGameState(UUID gameId);

    /**
     * Make the server update the game state to the new game state for all clients.
     * 
     * @param gameId the game ID of the game to update
     * @param gameState the new game state
     */
    public void setNewGameState(UUID gameId, GameState gameState);

    /**
     * Add all the actions pending to be performed in the game for the given playerId.
     * @param gameId of the game to add the actions to
     */
    public List<PlayerInput> lookForPendingActions(UUID playerId);

    /**
     * Look for a pending player to join the game with the given game ID.
     * @param gameId of the game to look for a pending player in
     * @return the player ID of the pending player if there is one, empty otherwise
     */
    public Optional<UUID> lookForPendingPlayer(UUID gameId);

    /**
     * Removes a game from the server.
     * 
     * @param gameId the UUID of the game to remove
     */
    public void removeGame(UUID gameId);

    /**
     * Sets the high score for a game based on the number of waves survived.
     * 
     * @param gameId the UUID of the game
     * @param wavesSurvived the number of waves survived
     */
    public void setHighScore(UUID gameId, int wavesSurvived);
}
