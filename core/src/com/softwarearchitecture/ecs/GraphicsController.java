package com.softwarearchitecture.ecs;

import com.softwarearchitecture.ecs.components.SpriteComponent;

public interface GraphicsController {
    void draw(SpriteComponent component);
    void clearScreen();
}
