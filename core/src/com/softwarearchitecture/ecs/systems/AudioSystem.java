package com.softwarearchitecture.ecs.systems;

import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.SoundComponent;

public class AudioSystem implements System {
    private ComponentManager<SoundComponent> audioManager;
    private SoundController soundController;

    public AudioSystem(ComponentManager<SoundComponent> audiManager, SoundController soundController) {
        this.audioManager = audiManager;
        this.soundController = soundController;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        
        for (Entity entity : entities) {
            // Play the sound component.
            Optional<SoundComponent> soundComponent = audioManager.getComponent(entity);
            if (soundComponent.isPresent()) {
                soundController.playSound(soundComponent.get());
            }
        }
    }
    
}
