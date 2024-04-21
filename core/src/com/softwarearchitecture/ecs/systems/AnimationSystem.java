package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;

import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;

/**
 * The {@code AnimationSystem} class updates animation frames of entities based on the elapsed time.
 * This system handles the interaction between {@link SpriteComponent} and {@link AnimationComponent}
 * to update and animate sprites accordingly during each frame of the game loop.
 *
 * It uses component managers to fetch relevant components from entities and applies frame updates
 * based on the delta time provided by the game loop.
 */
public class AnimationSystem implements System {

    public ComponentManager<SpriteComponent> spriteManager;
    public ComponentManager<AnimationComponent> animationManager;

    /**
     * Constructor for AnimationSystem.
     */
    public AnimationSystem() {
        this.spriteManager = ECSManager.getInstance().getOrDefaultComponentManager(SpriteComponent.class);
        this.animationManager = ECSManager.getInstance().getOrDefaultComponentManager(AnimationComponent.class);
    }

    /**
     * Updates the sprite and animation components of all entities provided.
     * This method is called every game loop iteration with the set of entities
     * that might contain {@link SpriteComponent} and {@link AnimationComponent} to update
     * their animation state based on the provided delta time.
     *
     * @param entities  The set of entities to be updated.
     * @param deltaTime The time elapsed since the last update, used to determine the current frame of the animation.
     */
    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            Optional<SpriteComponent> spriteComponent = spriteManager.getComponent(entity);
            Optional<AnimationComponent> animationComponent = animationManager.getComponent(entity);

            if (spriteComponent.isPresent() && animationComponent.isPresent()) {
                SpriteComponent sprite = spriteComponent.get();
                AnimationComponent animation = animationComponent.get();
                sprite.texture_path = animation.getFramePath(deltaTime);
            }
        }
    }

}
