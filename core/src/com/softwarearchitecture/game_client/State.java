package com.softwarearchitecture.game_client;

import com.softwarearchitecture.ecs.GraphicsController;

public interface State {
    void init(GraphicsController graphicsController);
    void update(float deltaTime);
    void dispose();
}
