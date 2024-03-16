package com.softwarearchitecture.ecs.components;

import com.badlogic.gdx.math.Vector2;

public class VelocityComponent {
    public Vector2 velocity = new Vector2();

    public VelocityComponent(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }
}
