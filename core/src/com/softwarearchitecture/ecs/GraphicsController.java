package com.softwarearchitecture.ecs;

import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;

public interface GraphicsController {
    /**
     * Draw the sprite component on the screen.
     * @param spriteComponent
     */
    void draw(SpriteComponent spriteComponent, PositionComponent positionComponent);

    /**
     * Clear the screen.
     */
    void clearScreen();
}
