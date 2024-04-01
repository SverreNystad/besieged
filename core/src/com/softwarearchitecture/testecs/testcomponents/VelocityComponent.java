package com.softwarearchitecture.testecs.testcomponents;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Serializable {
    public Vector2 velocity = new Vector2();

    public VelocityComponent(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }
}
