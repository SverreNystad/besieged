package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.SpriteComponent;

import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Entity;

/**
 * RenderingSystem is responsible for rendering entities that have a SpriteComponent.
 * It implements the System interface and defines logic in the update method to render entities.
 */
public class RenderingSystem implements System {
    private ComponentManager<SpriteComponent> drawableManager;

    private float screen_width;
    private float screen_height;

    public RenderingSystem(ComponentManager<SpriteComponent> drawableManager, float screen_width, float screen_height) {
        // TODO: Validate drawableManager
        this.drawableManager = drawableManager;
        this.screen_width = screen_width;
        this.screen_height = screen_height;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            SpriteComponent sprite = drawableManager.getComponent(entity);

            if (sprite != null) {
                render(sprite);
            }
        }
    }
    
    /**
     * Renders the entity using its SpriteComponent.
     * @param drawable The drawable component assiciated with an 
     */
    private void render(SpriteComponent drawable) {
         // Convert relative coordinates and sizes to screen dimensions.
        float x = drawable.screen_u * screen_width;
        float y = drawable.screen_v * screen_height;
        float width = drawable.u_size * screen_width;
        float height = drawable.v_size * screen_height;

    }

}