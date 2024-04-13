package com.softwarearchitecture.ecs.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.InputController;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.TouchLocation;
import com.softwarearchitecture.ecs.components.ButtonComponent;

public class InputSystem implements System {
    private ComponentManager<ButtonComponent> buttonManager;
    private InputController inputController;
    private TouchLocation lastTouched;
    private TouchLocation lastReleased;
    private List<Entity> toAdd = new ArrayList<>();

    public InputSystem(InputController inputController) {
        this.buttonManager = ECSManager.getInstance().getOrDefaultComponentManager(ButtonComponent.class);
        this.inputController = inputController;
        this.inputController.onTouch(this::onTouch);
        this.inputController.onRelease(this::onRelease);
    }

    public void onTouch(TouchLocation touchLocation) {
        this.lastTouched = touchLocation;
        // Process touch input here
    }

    public void onRelease(TouchLocation touchLocation) {
        this.lastReleased = touchLocation;
        // Process release input here
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            Optional<ButtonComponent> buttonComponent = buttonManager.getComponent(entity);
            if (buttonComponent.isPresent()) {
                ButtonComponent button = buttonComponent.get();

                if (isButtonPressed(button)) {
                    button.triggerAction();
                }
            }
        }
        lastTouched = new TouchLocation(-1f, -1f);
        lastReleased = new TouchLocation(-1f, -1f);
        entities.addAll(toAdd);
    }

    private boolean isButtonPressed(ButtonComponent buttonComponent) {
        return lastReleased != null && lastReleased.u >= buttonComponent.uv_offset.x
                && lastReleased.u <= buttonComponent.uv_offset.x + buttonComponent.uv_size.x
                && lastReleased.v >= buttonComponent.uv_offset.y
                && lastReleased.v <= buttonComponent.uv_offset.y + buttonComponent.uv_size.y;
    }
    
    // Method to get the last touch location
    public TouchLocation getLastTouched() {
        return lastTouched;
    }

    // Method to clear the last touch location after it's been processed
    public void clearLastTouched() {
        lastTouched = null;
    }
}
