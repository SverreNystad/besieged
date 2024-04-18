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

    public AudioSystem(ComponentManager<SoundComponent> audioManager, SoundController soundController) {
        this.audioManager = audioManager;
        this.soundController = soundController;
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        
        for (Entity entity : entities) {
            // Play the sound component.
            Optional<SoundComponent> soundComponent = audioManager.getComponent(entity);
            if (soundComponent.isPresent()) {
                if (soundComponent.get().isPlaying) {
                    continue;
                }
                soundComponent.get().isPlaying = true;
                if (soundComponent.get().isBackgroundMusic) {
                    java.lang.System.out.println("Spiller bakgrunnsmusikk");
                    soundController.playBackgroundMusic(soundComponent.get());
                } else {
                    java.lang.System.out.println("Spiller lyd");
                    soundController.playSound(soundComponent.get());
                }
            }
        }
    }
    

    public void playSound(SoundComponent soundComponent) {
        soundController.playSound(soundComponent);
    }
    
}
