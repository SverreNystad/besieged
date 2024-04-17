package com.softwarearchitecture.math;

import java.io.Serializable;

public class Vector3 implements Serializable {
    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
