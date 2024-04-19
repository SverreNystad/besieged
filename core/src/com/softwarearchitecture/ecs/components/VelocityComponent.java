package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.math.Vector2;

public class VelocityComponent implements Serializable {
    public float velocity;

    public float baseVelocity;

    public VelocityComponent(float velocity) {
        this.velocity = velocity;

        this.baseVelocity = velocity;
    }
}
