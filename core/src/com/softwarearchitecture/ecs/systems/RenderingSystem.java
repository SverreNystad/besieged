package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.SpriteComponent;

import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.GraphicsController;

/**
 * RenderingSystem is responsible for rendering entities that have a SpriteComponent.
 * It implements the System interface and defines logic in the update method to render entities.
 */
public class RenderingSystem implements System {
    private ComponentManager<SpriteComponent> drawableManager;

    private float screen_width;
    private float screen_height;

    /** Graphics controller - Not optional and will be needed to check against null. Optional<T> is a option here, but since this is performance critical, we have considered not to use it and instead check for null. */
    private GraphicsController graphicsController;

    public RenderingSystem(ComponentManager<SpriteComponent> drawableManager, GraphicsController graphicsController, float screen_width, float screen_height) {
        // TODO: Validate drawableManager
        this.drawableManager = drawableManager;
        this.screen_width = screen_width;
        this.screen_height = screen_height;
        this.graphicsController = graphicsController;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            Optional<SpriteComponent> sprite = drawableManager.getComponent(entity);

            if (sprite.isPresent()) {
                graphicsController.draw(sprite.get());
            }
        }
    }

}