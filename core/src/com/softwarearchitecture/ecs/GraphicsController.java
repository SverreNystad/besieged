package com.softwarearchitecture.ecs;

import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;

/**
 * The {@code GraphicsController} interface defines the essential operations for rendering graphics
 * within the game. This includes drawing sprites and text, clearing the screen, and other graphics
 * utilities such as drawing shapes or querying screen properties.
 */
public interface GraphicsController {
    /**
     * Clears the screen of all current graphics. This method should typically be called at the
     * start of a new rendering cycle to reset the screen state before new drawing commands are issued.
     */
    void clearScreen();

    /**
     * Draws a sprite on the screen at the specified position.
     *
     * @param spriteComponent the {@link SpriteComponent} containing the sprite's texture and other rendering properties.
     * @param positionComponent the {@link PositionComponent} determining where to draw the sprite on the screen.
     */
    void draw(SpriteComponent spriteComponent, PositionComponent positionComponent);


    /**
     * Draws text on the screen at the specified position.
     *
     * @param textComponent the {@link TextComponent} containing the text string and font styling details.
     * @param positionComponent the {@link PositionComponent} determining where to draw the text on the screen.
     */
    void drawText(TextComponent textComponent, PositionComponent positionComponent);

    /**
     * Returns the aspect ratio of the screen, which is defined as the ratio of the screen's width to its height.
     *
     * @return the aspect ratio of the screen.
     */
    float getAspectRatio();

    /**
     * Draws a filled square on the screen at the specified position, using the specified color and transparency.
     *
     * @param positionComponent the {@link PositionComponent} determining where to draw the square on the screen.
     * @param width the width of the square.
     * @param height the height of the square.
     * @param r the red component of the color (0-1).
     * @param g the green component of the color (0-1).
     * @param b the blue component of the color (0-1).
     * @param a the alpha component for transparency (0-1).
     */
    void drawSquare(PositionComponent positionComponent, float width, float height, float r, float g, float b, float a);
}
