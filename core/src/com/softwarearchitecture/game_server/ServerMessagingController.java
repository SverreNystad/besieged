package com.softwarearchitecture.game_server;

import java.util.List;
import java.util.UUID;

public interface ServerMessagingController {
    /**
     * Create a new game and return the game ID.
     * 
     * @return the game ID of the new game
     */
    public UUID createGame();

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
}
