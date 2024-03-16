package com.softwarearchitecture.ecs.components;

import java.util.UUID;

public class TargetComponent {
    // ID of target-entity
    public UUID targetID;

    public TargetComponent(UUID targetID) {
        this.targetID = targetID;
    }
}
