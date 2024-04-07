package com.softwarearchitecture.ecs.systems;

import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
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

    public InputSystem(ComponentManager<ButtonComponent> buttonManager, InputController inputController) {
        this.buttonManager = buttonManager;
        this.inputController = inputController;
    }

    public void onTouch(TouchLocation touchLocation) {
        this.lastTouched = touchLocation;
        java.lang.System.out.println("Touch at: " + touchLocation.u + ", " + touchLocation.v);
        // Process touch input here
    }

    public void onRelease(TouchLocation touchLocation) {
        this.lastReleased = touchLocation;
        java.lang.System.out.println("Release at: " + touchLocation.u + ", " + touchLocation.v);
        // Process release input here
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        for (Entity entity : entities) {
            Optional<ButtonComponent> buttonComponent = buttonManager.getComponent(entity);
            java.lang.System.out.println("ButtonComponent: " + buttonComponent);
            if (buttonComponent.isPresent()) {
                ButtonComponent button = buttonComponent.get();

                if (isButtonPressed(button)) {
                    button.triggerAction();
                    java.lang.System.out.println("Button pressed: " + button.type);
                }
            }
        }
    }

    private boolean isButtonPressed(ButtonComponent buttonComponent) {
        return lastReleased != null && lastReleased.u >= buttonComponent.uv_offset.x
                && lastReleased.u <= buttonComponent.uv_offset.x + buttonComponent.uv_size.x
                && lastReleased.v >= buttonComponent.uv_offset.y
                && lastReleased.v <= buttonComponent.uv_offset.y + buttonComponent.uv_size.y;
    }
}
