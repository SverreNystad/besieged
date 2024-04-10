package com.softwarearchitecture.ecs.components;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

public class TargetComponent implements Serializable {
    // ID of target-entity
    public Optional<UUID> targetID;

    public TargetComponent(UUID targetID) {
        this.targetID = Optional.of(targetID);
    }

    public TargetComponent() {
        this.targetID = null;
    }
}
