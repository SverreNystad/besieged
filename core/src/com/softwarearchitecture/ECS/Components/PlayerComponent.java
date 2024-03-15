package com.softwarearchitecture.ecs.components;

import java.util.UUID;

public class PlayerComponent {
    public UUID playerID;
    
    public PlayerComponent(UUID playerID) {
        this.playerID = playerID;
    }
}
