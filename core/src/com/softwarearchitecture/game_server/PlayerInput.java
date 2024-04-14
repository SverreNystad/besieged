package com.softwarearchitecture.game_server;

import java.util.UUID;

public class PlayerInput {
    private UUID playerID;
    private String action;
    
    // Maybe it is better with a player input builder? Or just playerID, action type as enum, and serialized data?
    public PlayerInput(UUID playerID, String action) {
        this.playerID = playerID;
        this.action = action;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public String getAction() {
        return action;
    }
}

