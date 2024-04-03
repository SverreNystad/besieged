package com.softwarearchitecture.game_client;

import com.softwarearchitecture.ecs.GraphicsController;

public interface State {
    /**
     * Initialize the state.
     * @param graphicsController
     */
    void init(GraphicsController graphicsController);
    
    /**
     * Update the state.
     * @param deltaTime
     */
    void update(float deltaTime);
    
    /**
     * Dispose the state.
     */
    void dispose();
}
