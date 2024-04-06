package com.softwarearchitecture.ecs.components;

import java.io.Serializable;
import java.util.UUID;

public class PlayerComponent implements Serializable {
    public UUID playerID;
    
    public PlayerComponent(UUID playerID) {
        this.playerID = playerID;
    }
}
