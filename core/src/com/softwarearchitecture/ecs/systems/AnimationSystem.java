package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;

import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.clock.Clock;
import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;

/**
 * AnimationSystem is responsible for updating the path of spriteComponent.
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
