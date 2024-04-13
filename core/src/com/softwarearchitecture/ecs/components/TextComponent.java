package com.softwarearchitecture.ecs.components;

import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.math.Vector3;

public class TextComponent {
    public String text;
    public Vector2 fontScale;
    public Vector3 color; 

    public TextComponent(String text, Vector2 fontScale) {
        this.text = text;
        this.fontScale = fontScale;
        this.color = new Vector3(1, 1, 1); // Default color
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }

    public Vector3 getColor() {
        return color;
    }
}
