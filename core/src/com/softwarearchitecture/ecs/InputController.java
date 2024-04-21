package com.softwarearchitecture.ecs;

import java.util.function.Consumer;

/**
 * The {@code InputController} interface defines methods for handling touch inputs within the game. 
 * It provides mechanisms to register callback functions for touch events and to query the last release location.
 * This interface is intended to abstract the complexities of input handling, allowing the game systems to 
 * respond to player actions in a straightforward and predictable manner.
 */
public interface InputController {
    /**
     * Registers a callback function to be invoked when a touch event occurs. This method is designed 
     * to capture touch interactions, such as tapping or pressing on the screen, allowing the game to 
     * react accordingly.
     *
     * @param onTouch the {@link Consumer<TouchLocation>} callback that processes the touch location data.
     */
    void onTouch(Consumer<TouchLocation> onTouch);
    
    /**
     * Registers a callback function to be invoked when a touch release event occurs. This method captures
     * the end of a touch interaction, such as lifting a finger off the screen, which can be crucial for 
     * handling drag-and-drop actions or for triggering certain gameplay mechanics upon release.
     *
     * @param onRelease the {@link Consumer<TouchLocation>} callback that processes the touch release location data.
     */
    void onRelease(Consumer<TouchLocation> onRelease);
    
    /**
     * Retrieves the last known location where a touch was released. This can be useful for determining 
     * where actions should take place in the game, such as dropping an item or confirming the selection
     * of a game element.
     *
     * @return the last release location as a {@link TouchLocation}, providing the x and y coordinates of the touch point.
     */
    TouchLocation getLastReleaseLocation();
}
