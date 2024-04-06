package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.math.Vector2;

public class PositionComponent implements Serializable {
    public Vector2 position = new Vector2();

    public PositionComponent(Vector2 position) {
        this.position = position;
    }
}
