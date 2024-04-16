package com.softwarearchitecture.math;

import java.io.Serializable;

public class Vector2 implements Serializable {

    public float x;
    public float y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public String toString() {
        return "Vector2{" + "x=" + x + ", y=" + y + '}';
    }

    public Vector2 add(Vector2 vector) {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    public Vector2 sub(Vector2 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }

    public Vector2 scl(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2 nor() {
        float len = len();
        if (len != 0) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    public float dot(Vector2 vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    public float dst(Vector2 vector) {
        float x_d = vector.x - this.x;
        float y_d = vector.y - this.y;
        return (float) Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public float dst(float x, float y) {
        float x_d = x - this.x;
        float y_d = y - this.y;
        return (float) Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public Vector2 cpy() {
        return new Vector2(this);
    }

    public static float dst(Vector2 v1, Vector2 v2) {
        float x_d = v2.x - v1.x;
        float y_d = v2.y - v1.y;
        return (float) Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public static float dst(float x1, float y1, float x2, float y2) {
        float x_d = x2 - x1;
        float y_d = y2 - y1;
        return (float) Math.sqrt(x_d * x_d + y_d * y_d);
    }
}
