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

    public AudioSystem(ComponentManager<SoundComponent> audioManager, SoundController soundController) {
        this.audioManager = audioManager;
        this.soundController = soundController;
        this.towerManager = ECSManager.getInstance().getOrDefaultComponentManager(TowerComponent.class);
        this.cardManager = ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class);
        this.enemyManager = ECSManager.getInstance().getOrDefaultComponentManager(EnemyComponent.class);
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
                    // TODO: Case for cards
                    Optional<PlacedCardComponent> cardComponent = cardManager.getComponent(entity);
                    if (cardComponent.isPresent() && cardComponent.get().playSound == true) {
                        java.lang.System.out.println("Spiller kortlyd");
                        soundController.playSound(soundComponent.get());
                        cardComponent.get().playSound = false;
                    }

                    // TODO: Case for towers
                    Optional<TowerComponent> towerComponent = towerManager.getComponent(entity);
                    if (towerComponent.isPresent() && towerComponent.get().playSound == true) {
                        java.lang.System.out.println("Spiller tårnlyd");
                        soundController.playSound(soundComponent.get());
                        towerComponent.get().playSound = false;
                    }


                    // TODO: Case for enemies
                    Optional<EnemyComponent> enemyComponent = enemyManager.getComponent(entity);


                    java.lang.System.out.println("Spiller lyd");
                    // soundController.playSound(soundComponent.get());
                }
            }
        }
    }
    

    // public void playSound(SoundComponent soundComponent) {
    //     soundController.playSound(soundComponent);
    // }
    
}
