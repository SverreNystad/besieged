package com.softwarearchitecture.ecs.systems;

import java.util.Set;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.TouchLocation;

public class InputSystem implements System {
    public void onTouch(TouchLocation touchLocation) {
        java.lang.System.out.println("Touch at: " + touchLocation.u + ", " + touchLocation.v);
        // Process touch input here
    }

    public void onRelease(TouchLocation touchLocation) {
        java.lang.System.out.println("Release at: " + touchLocation.u + ", " + touchLocation.v);
        // Process release input here
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        // Update entities based on the input
    }
}
