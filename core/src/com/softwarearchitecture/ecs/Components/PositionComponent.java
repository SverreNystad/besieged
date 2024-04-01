package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Serializable {
    public Vector2 position = new Vector2();

    public PositionComponent(float x, float y) {
        position.x = x;
        position.y = y;
    }
}
