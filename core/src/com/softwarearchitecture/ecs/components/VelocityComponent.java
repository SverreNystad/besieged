package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.math.Vector2;

public class VelocityComponent implements Serializable {
    public Vector2 velocity = new Vector2();

    public Vector2 baseVelocity = new Vector2();

    public VelocityComponent(float x, float y) {
        velocity.x = x;
        velocity.y = y;

        baseVelocity = velocity;
    }
}
