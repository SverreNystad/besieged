package com.softwarearchitecture.ECS;

import java.util.Set;

public interface System {
    void update(Set<Entity> entities, float deltaTime);
}
