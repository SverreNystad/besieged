package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.math.Vector2;
/**
 * @param position The position of the entity in UV-coordinates
 * @param z_index The z-index of the entity
 */
public class PositionComponent implements Serializable {
    public Vector2 position = new Vector2();
    public int z_index;

    public PositionComponent(Vector2 position, int z_index) {
        this.position = position;
        this.z_index = z_index;
    }

    public Vector2 getPosition() {
        return this.position;
    }
}
