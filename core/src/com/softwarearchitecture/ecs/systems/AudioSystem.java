package com.softwarearchitecture.ecs.systems;

import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;

public class AudioSystem implements System {
    private ComponentManager<SoundComponent> audioManager;
    private SoundController soundController;
    private ComponentManager<TowerComponent> towerManager;
    private ComponentManager<PlacedCardComponent> cardManager;
    private ComponentManager<EnemyComponent> enemyManager;

    public AudioSystem(SoundController soundController) {
        this.audioManager = ECSManager.getInstance().getOrDefaultComponentManager(SoundComponent.class);
        this.soundController = soundController;
        this.towerManager = ECSManager.getInstance().getOrDefaultComponentManager(TowerComponent.class);
        this.cardManager = ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class);
        this.enemyManager = ECSManager.getInstance().getOrDefaultComponentManager(EnemyComponent.class);
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        
        // Check for newly added entities that has sound components.
        for (Entity entity : ECSManager.getInstance().getAndClearNewlyRemoteAddedEntities()) {
            Optional<SoundComponent> soundComponent = audioManager.getComponent(entity);
            Optional<PlacedCardComponent> cardComponent = cardManager.getComponent(entity);
            if (soundComponent.isPresent() && cardComponent.isPresent()) {
                java.lang.System.out.println("Entity card: " + entity);
                cardComponent.get().playSound = true;
            }

            Optional<TowerComponent> towerComponent = towerManager.getComponent(entity);
            if (soundComponent.isPresent() && towerComponent.isPresent()) {

                towerComponent.get().playSound = true;
            }

        }
        
        for (Entity entity : entities) {
            // If the entity does not have a sound component, skip it.
            Optional<SoundComponent> soundComponent = audioManager.getComponent(entity);
            if (!soundComponent.isPresent())
                continue;

            SoundComponent sound = soundComponent.get();
            
            // Check background music
            if (sound.isBackgroundMusic) {
                soundController.playBackgroundMusic(sound);
                continue;
            }

            // Check if the sound is a sound effect
            // Sound for cards
            Optional<PlacedCardComponent> cardComponent = cardManager.getComponent(entity);
            if (cardComponent.isPresent() && cardComponent.get().playSound == true) {
                java.lang.System.out.println("PLAYING CARD SOUND");
                soundController.playSound(soundComponent.get());
                cardComponent.get().playSound = false;
            }
            
            // Sound for towers
            Optional<TowerComponent> towerComponent = towerManager.getComponent(entity);
            if (towerComponent.isPresent() && towerComponent.get().playSound == true) {
                java.lang.System.out.println("AUDIO system: " + towerComponent.isPresent());
                java.lang.System.out.println("PLAYING TOWER SOUND");
                soundController.playSound(soundComponent.get());
                towerComponent.get().playSound = false;
            }
        }
    }
}