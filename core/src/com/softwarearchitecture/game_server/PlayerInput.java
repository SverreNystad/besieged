package com.softwarearchitecture.game_server;

import java.util.UUID;

public class PlayerInput {
    private UUID playerID;
    // TODO: Add all action types as Optionals we are not able to use tagged enums in java :( Big lost opportunity
    
    // Maybe it is better with a player input builder? Or just playerID, action type as enum, and serialized data?
    public PlayerInput(UUID playerID/* All action types here */) {
        this.playerID = playerID;
        // Check that only one of the inputs are not null
    }
}

