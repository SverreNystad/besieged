package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;

import java.util.Set;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;

/**
 * AnimationSystem is responsible for updating the path of spriteComponent.
 */
public class AnimationSystem implements System {

    public SpriteComponent spriteComponent;
    public AnimationComponent animationComponent;

    /**
     * Constructor for AnimationSystem.
     * 
     * @param spriteComponent    SpriteComponent
     * @param animationComponent AnimationComponent
     */
    public AnimationSystem(SpriteComponent spriteComponent, AnimationComponent animationComponent) {
        this.spriteComponent = spriteComponent;
        this.animationComponent = animationComponent;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        animationComponent.nextFrame();
        spriteComponent.setSprite(animationComponent.getAnimationPath());
    }

}
