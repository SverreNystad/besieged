package com.softwarearchitecture.ecs;

import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;

public interface GraphicsController {
    /**
     * Draw the sprite component on the screen.
     * 
     * @param spriteComponent
     */
    void draw(SpriteComponent spriteComponent, PositionComponent positionComponent);

    /**
     * Clear the screen.
     */
    void clearScreen();

    /**
     * Draw text.
     */
    void drawText(TextComponent textComponent, PositionComponent positionComponent);

    /**
     * Gets the aspect ratio of the screen.
     */
    float getAspectRatio();

    /**
     * Draw a square.
     */
    void drawSquare(PositionComponent positionComponent, float width, float height, float r, float g, float b, float a);
}
