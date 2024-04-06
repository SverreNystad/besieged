package com.softwarearchitecture.ecs;

import com.softwarearchitecture.ecs.components.SpriteComponent;

public interface GraphicsController {
    /**
     * Draw the sprite component on the screen.
     * @param component
     */
    void draw(SpriteComponent component);

    /**
     * Clear the screen.
     */
    void clearScreen();

    /**
     * Get the width of the screen.
     * @return the width of the screen
     */
    int getScreenWidth();

    /**
     * Get the height of the screen.
     * @return the height of the screen
     */
    int getScreenHeight();
}
