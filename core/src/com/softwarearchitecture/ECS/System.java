package com.softwarearchitecture.ecs;

import java.util.Set;

/**
 * The {@code System} interface defines a contract for systems within the ECS
 * architecture of Besieged.
 * Systems are responsible for executing game logic and rendering by operating
 * on entities that possess
 * a specific set of components. This interface mandates the implementation of
 * an update method,
 * allowing systems to process and react to the state of entities over time.
 * <p>
 * Implementations of this interface should focus on a specific aspect of game
 * logic or rendering,
 * such as movement, physics, or AI, and apply operations uniformly to all
 * entities with the
 * required components. The {@code update} method is called once per
 * update-cycle allowing the system
 * to make changes or respond to the state of the game.
 * 
 * @see Entity
 */
public interface System {

    /**
     * Updates the state of entities processed by this system. This method is
     * intended to be called
     * once per update-cycle and should contain the logic to manipulate entities
     * based on the system's role.
     *
     * @param entities  A set of entities that the system will process. These
     *                  entities should possess
     *                  the components necessary for the system to perform its
     *                  logic.
     * @param deltaTime The time in seconds since the last update, useful for
     *                  time-dependent calculations
     *                  such as physics integration or animations.
     */
    void update(Set<Entity> entities, float deltaTime);
}
