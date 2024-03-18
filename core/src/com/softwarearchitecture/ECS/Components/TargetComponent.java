package com.softwarearchitecture.ecs.components;

import java.io.Serializable;
import java.util.UUID;

public class TargetComponent implements Serializable {
    // ID of target-entity
    public UUID targetID;

    public TargetComponent(UUID targetID) {
        this.targetID = targetID;
    }
}
