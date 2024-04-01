package com.softwarearchitecture.testecs.systems;

import java.util.Optional;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;


public class MovementSystem implements System {
    private ComponentManager<PositionComponent> positionManager;
    private ComponentManager<VelocityComponent> velocityManager;
    private ComponentManager<SpriteComponent> drawableManager;
    private Viewport viewport;

    public MovementSystem(ComponentManager<PositionComponent> positionManager,
        ComponentManager<VelocityComponent> velocityManager, ComponentManager<SpriteComponent> drawableManager, Viewport viewport) {
        if (positionManager != null && velocityManager != null && drawableManager != null) {
            this.positionManager = positionManager;
            this.velocityManager = velocityManager;
            this.drawableManager = drawableManager;
            this.viewport = viewport;
        }
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            Optional<VelocityComponent> velocity = velocityManager.getComponent(entity);
            Optional<SpriteComponent> drawable = drawableManager.getComponent(entity);

            if (position.isEmpty() || velocity.isEmpty()) {
                continue;
            }

            PositionComponent pos = position.get();
            VelocityComponent vel = velocity.get();


            pos.position.x += vel.velocity.x * deltaTime;
            pos.position.y += vel.velocity.y * deltaTime;

            if (drawable.isPresent()) {
                updateDrawable(drawable.get(), pos);
            }
        }
    }
    
    private void updateDrawable(SpriteComponent drawable, PositionComponent pos) {
        float screen_width = viewport.getWorldWidth();
        float screen_height = viewport.getWorldHeight();
    
        // Example conversion - assume game world dimensions match screen dimensions 
        float drawableWidthInWorld = 1.0f; // Fixed max-width of the entity in the world
        float drawableHeightInWorld = 1.0f; // Fixed max-height of the entity in the world
        
        // Conversion from world position to screen position
        drawable.screen_u = convertWorldToScreenX(pos.position.x, screen_width);
        drawable.screen_v = convertWorldToScreenY(pos.position.y, screen_height);
    
        // Conversion from world size to screen size
        drawable.u_size = convertWorldToScreenSize(drawableWidthInWorld, screen_width);
        drawable.v_size = convertWorldToScreenSize(drawableHeightInWorld, screen_height);
    }

    // Convert world X-coordinate to screen U coordinate
    private float convertWorldToScreenX(float worldX, float screenWidth) {
        return worldX / screenWidth;
    }

    // Convert world Y-coordinate to screen V coordinate
    private float convertWorldToScreenY(float worldY, float screenHeight) {
        return worldY / screenHeight;
    }

    // Convert world size to screen size (assuming uniform scaling)
    private float convertWorldToScreenSize(float worldSize, float screenSize) {
        return worldSize / screenSize;
    }
}


