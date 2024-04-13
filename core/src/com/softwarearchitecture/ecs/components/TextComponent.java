package com.softwarearchitecture.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.softwarearchitecture.math.Vector2;

public class TextComponent {
    public String text;
    public Vector2 fontScale;
    private Color color; 

    public TextComponent(String text, Vector2 fontScale) {
        this.text = text;
        this.fontScale = fontScale;
        this.color = Color.BLACK; // Default color
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
