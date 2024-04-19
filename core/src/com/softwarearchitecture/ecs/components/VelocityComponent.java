package com.softwarearchitecture.ecs.components;

import java.io.Serializable;


public class VelocityComponent implements Serializable {
    public float velocity = 0f;

    public float baseVelocity = 0f;

    public VelocityComponent(float velocity) {
        this.velocity = velocity;
        baseVelocity = velocity;
    }
}
