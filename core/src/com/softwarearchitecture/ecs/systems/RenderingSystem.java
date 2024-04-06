package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.PositionComponent;
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
    private ComponentManager<PositionComponent> positionManager;

    /** Graphics controller - Not optional and will be needed to check against null. Optional<T> is a option here, but since this is performance critical, we have considered not to use it and instead check for null. */
    private GraphicsController graphicsController;

    public RenderingSystem(ComponentManager<SpriteComponent> drawableManager, ComponentManager<PositionComponent> positionManager, GraphicsController graphicsController) {
        // TODO: Validate drawableManager
        this.drawableManager = drawableManager;
        this.graphicsController = graphicsController;
        this.positionManager = positionManager;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) throws IllegalStateException {
        if (graphicsController == null) {
            throw new IllegalStateException("Graphics controller is not set.");
        }

        ArrayList<SpriteComponent> sprites = new ArrayList<>();
        ArrayList<PositionComponent> positions = new ArrayList<>();

        for (Entity entity : entities) {
            Optional<SpriteComponent> sprite = drawableManager.getComponent(entity);
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            if (sprite.isPresent() && position.isPresent()) {
                sprites.add(sprite.get());
                positions.add(position.get());
            } else if (sprite.isPresent() && !position.isPresent()) {
                throw new IllegalStateException("Entity " + entity + " has a SpriteComponent but no PositionComponent.");
            } else if (!sprite.isPresent() && position.isPresent()) {
                throw new IllegalStateException("Entity " + entity + " has a PositionComponent but no SpriteComponent.");
            }
        }

        sprites.sort(new Comparator<SpriteComponent>() {
            @Override
            public int compare(SpriteComponent s1, SpriteComponent s2) {
                return Integer.compare(s2.z_index, s1.z_index);
            }
        });
        
        for (int i = 0; i < sprites.size(); i++) {
            graphicsController.draw(sprites.get(i), positions.get(i));
        }
        graphicsController.clearScreen();
    }

}