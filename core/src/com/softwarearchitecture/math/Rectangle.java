package com.softwarearchitecture.math;

public class Rectangle {

    public float x;
    public float y;
    public float width;
    public float height;

    public Rectangle() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    // TODO: add functionality to wrap texture to the rectangle

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rectangle rectangle) {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.width = rectangle.width;
        this.height = rectangle.height;
    }

    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void set(Rectangle rectangle) {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.width = rectangle.width;
        this.height = rectangle.height;
    }

    public boolean contains(float x, float y) {
        return this.x < x && this.x + this.width > x && this.y < y && this.y + this.height > y;
    }

    public boolean contains(Vector2 vector) {
        return contains(vector.x, vector.y);
    }

    public boolean contains(Rectangle rectangle) {
        return this.x < rectangle.x && this.x + this.width > rectangle.x + rectangle.width && this.y < rectangle.y
                && this.y + this.height > rectangle.y + rectangle.height;
    }

    public boolean overlaps(Rectangle rectangle) {
        return this.x < rectangle.x + rectangle.width && this.x + this.width > rectangle.x
                && this.y < rectangle.y + rectangle.height && this.y + this.height > rectangle.y;
    }

    public Rectangle setCenter(float x, float y) {
        this.x = x - this.width / 2;
        this.y = y - this.height / 2;
        return this;
    }

    public Rectangle setCenter(Vector2 vector) {
        return setCenter(vector.x, vector.y);
    }

    public Vector2 getCenter(Vector2 vector) {
        vector.x = this.x + this.width / 2;
        vector.y = this.y + this.height / 2;
        return vector;
    }

    public Rectangle setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;

    }

    public Rectangle setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Rectangle setPosition(Vector2 vector) {
        return setPosition(vector.x, vector.y);
    }

    public Rectangle setX(float x) {
        this.x = x;
        return this;
    }

    public Rectangle setY(float y) {
        this.y = y;
        return this;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object == null)
            return false;
        if (object.getClass() != this.getClass())
            return false;
        Rectangle rectangle = (Rectangle) object;
        return this.x == rectangle.x && this.y == rectangle.y && this.width == rectangle.width
                && this.height == rectangle.height;
    }

    public String toString() {
        return x + "," + y + "," + width + "," + height;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(width);
        result = prime * result + Float.floatToIntBits(height);
        return result;
    }

    public Rectangle merge(Rectangle rectangle) {
        float x = Math.min(this.x, rectangle.x);
        float y = Math.min(this.y, rectangle.y);
        float width = Math.max(this.x + this.width, rectangle.x + rectangle.width) - x;
        float height = Math.max(this.y + this.height, rectangle.y + rectangle.height) - y;
        return new Rectangle(x, y, width, height);
    }

    public Rectangle merge(float x, float y) {
        float minX = Math.min(this.x, x);
        float maxX = Math.max(this.x + this.width, x);
        float minY = Math.min(this.y, y);
        float maxY = Math.max(this.y + this.height, y);
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public Rectangle merge(Vector2 vector) {
        return merge(vector.x, vector.y);
    }

    public Rectangle merge(float x, float y, float width, float height) {
        float minX = Math.min(this.x, x);
        float maxX = Math.max(this.x + this.width, x + width);
        float minY = Math.min(this.y, y);
        float maxY = Math.max(this.y + this.height, y + height);
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public Rectangle merge(Vector2 vector, Vector2 vector2) {
        return merge(vector.x, vector.y, vector2.x, vector2.y);
    }

    public Rectangle merge(Rectangle rectangle, Rectangle rectangle2) {
        float x = Math.min(this.x, rectangle.x);
        float y = Math.min(this.y, rectangle.y);
        float width = Math.max(this.x + this.width, rectangle.x + rectangle.width) - x;
        float height = Math.max(this.y + this.height, rectangle.y + rectangle.height) - y;
        return new Rectangle(x, y, width, height);
    }

    public Rectangle merge(Rectangle rectangle, Vector2 vector) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Vector2 vector, Rectangle rectangle) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Rectangle rectangle, Vector2 vector, Vector2 vector2) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Vector2 vector, Rectangle rectangle, Vector2 vector2) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Vector2 vector, Vector2 vector2, Rectangle rectangle) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Rectangle rectangle, Vector2 vector, Vector2 vector2, Rectangle rectangle2) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Vector2 vector, Rectangle rectangle, Vector2 vector2, Rectangle rectangle2) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Rectangle rectangle, Vector2 vector, Vector2 vector2, Rectangle rectangle2,
            Vector2 vector3) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Vector2 vector, Rectangle rectangle, Vector2 vector2, Rectangle rectangle2,
            Vector2 vector3) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Rectangle rectangle, Vector2 vector, Vector2 vector2, Rectangle rectangle2, Vector2 vector3,
            Vector2 vector4) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Vector2 vector, Rectangle rectangle, Vector2 vector2, Rectangle rectangle2, Vector2 vector3,
            Vector2 vector4) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Rectangle rectangle, Vector2 vector, Vector2 vector2, Rectangle rectangle2, Vector2 vector3,
            Vector2 vector4, Vector2 vector5) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Vector2 vector, Rectangle rectangle, Vector2 vector2, Rectangle rectangle2, Vector2 vector3,
            Vector2 vector4, Vector2 vector5) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle merge(Rectangle rectangle, Vector2 vector, Vector2 vector2, Rectangle rectangle2, Vector2 vector3,
            Vector2 vector4, Vector2 vector5, Vector2 vector6) {
        return merge(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

}
