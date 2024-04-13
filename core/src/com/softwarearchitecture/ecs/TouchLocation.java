package com.softwarearchitecture.ecs;

/**
 * @param u The relative X-coordinate (U) of the entity on the screen, ranging from 0 (left edge) to 1 (right edge).
 * @param v The relative Y-coordinate (V) of the entity on the screen, ranging from 0 (bottom edge) to 1 (top edge).
*/
public class TouchLocation {
    public float u;
    public float v;

    public TouchLocation(float u, float v) {
        this.u = u;
        this.v = v;
    }
}
