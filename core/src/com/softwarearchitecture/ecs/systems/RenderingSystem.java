package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.SpriteComponent;

import java.util.ArrayList;
import java.util.Comparator;
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

    /** Graphics controller - Not optional and will be needed to check against null. Optional<T> is a option here, but since this is performance critical, we have considered not to use it and instead check for null. */
    private GraphicsController graphicsController;

    public RenderingSystem(ComponentManager<SpriteComponent> drawableManager, GraphicsController graphicsController) {
        // TODO: Validate drawableManager
        this.drawableManager = drawableManager;
        this.graphicsController = graphicsController;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) throws IllegalStateException {
        if (graphicsController == null) {
            throw new IllegalStateException("Graphics controller is not set.");
        }

        ArrayList<SpriteComponent> sprites = new ArrayList<>();

        for (Entity entity : entities) {
            Optional<SpriteComponent> sprite = drawableManager.getComponent(entity);
            if (sprite.isPresent()) {
                sprites.add(sprite.get());
            }
        }

        sprites.sort(new Comparator<SpriteComponent>() {
            @Override
            public int compare(SpriteComponent o1, SpriteComponent o2) {
                return Integer.compare(o1.z_index, o2.z_index);
            }
        });
        
        for (SpriteComponent sprite : sprites) {
            graphicsController.draw(sprite);
        }
        graphicsController.clearScreen();
    }

}