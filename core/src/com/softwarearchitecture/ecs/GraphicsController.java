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
}
