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
    public UUID createGame();

    /**
     * Get the game state of the game with the given game ID.
     * 
     * @param gameID of the game to get the game state of
     * @return the game state of the game with the given game ID
     */
    public GameState getGameState(UUID gameID);

    /**
     * Make the server update the game state to the new game state for all clients.
     * 
     * @param id the game ID of the game to update
     * @param gameState the new game state
     */
    public void setNewGameState(UUID id, GameState gameState);

    /**
     * Get the actions that have been performed in the game with the given game ID.
     * 
     * @param gameID of the game to get the actions of
     * @return the list of actions that have been performed in the game
     */
    public List<PlayerInput> getActions(UUID gameID);

    /**
     * Add all the actions pending to be performed in the game with the given game ID.
     * @param gameID of the game to add the actions to
     */
    public Optional<PlayerInput> lookForPendingActions(UUID gameID);

    /**
     * Look for a pending player to join the game with the given game ID.
     * @param gameID of the game to look for a pending player in
     * @return the player ID of the pending player if there is one, empty otherwise
     */
    public Optional<UUID> lookForPendingPlayer(UUID gameID);
}
