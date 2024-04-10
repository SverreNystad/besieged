package com.softwarearchitecture.ecs.components;

import com.softwarearchitecture.math.Vector2;

public class TextComponent {
    public String text;
    public Vector2 fontScale;

    public TextComponent(String text, Vector2 fontScale) {
        this.text = text;
        this.fontScale = fontScale;
    }
}
