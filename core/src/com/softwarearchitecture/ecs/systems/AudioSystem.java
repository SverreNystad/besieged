package com.softwarearchitecture.ecs.systems;

import java.util.ArrayList;
import java.util.List;
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
import com.softwarearchitecture.game_server.PairableCards.TowerType;


/**
 * The {@code AudioSystem} class manages the playback of sound effects and background music
 * for entities within the game environment. It leverages component managers to access
 * and manipulate sound-related properties of entities, such as towers, cards, and enemies.
 * 
 * <p>This system ensures that sounds are played when certain conditions are met, like the placement
 * of a card or attack actions of a tower. It also manages continuous playback of background music.</p>
 * 
 * <p>Sound playback is contingent upon the presence of {@link SoundComponent} and is further
 * influenced by other components like {@link PlacedCardComponent}, {@link TowerComponent},
 * and {@link EnemyComponent}.</p>
 */
public class AudioSystem implements System {
    private ComponentManager<SoundComponent> audioManager;
    private SoundController soundController;
    private ComponentManager<TowerComponent> towerManager;
    private ComponentManager<PlacedCardComponent> cardManager;
    private ComponentManager<EnemyComponent> enemyManager;
    private List<TowerType> towersWithoutSounds = new ArrayList<TowerType>();

    public AudioSystem(SoundController soundController) {
        this.audioManager = ECSManager.getInstance().getOrDefaultComponentManager(SoundComponent.class);
        this.soundController = soundController;
        this.towerManager = ECSManager.getInstance().getOrDefaultComponentManager(TowerComponent.class);
        this.cardManager = ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class);
        this.enemyManager = ECSManager.getInstance().getOrDefaultComponentManager(EnemyComponent.class);

        towersWithoutSounds.add(TowerType.INFERNO);
        towersWithoutSounds.add(TowerType.FURNACE);
        towersWithoutSounds.add(TowerType.MAGIC_TECH);
        towersWithoutSounds.add(TowerType.BOW_MAGIC);
        towersWithoutSounds.add(TowerType.MAGIC);
    }

    /**
     * Processes sound actions for all active entities and newly added remote entities.
     * It plays individual sound effects and background music based on the entity's components and state.
     *
     * @param entities   the set of all entities to process for sound actions
     * @param deltaTime  the time since the last frame, used to manage sound timings
     */
    @Override
    public void update(Set<Entity> entities, float deltaTime) {

        // Check for newly added entities that has sound components.
        for (Entity entity : ECSManager.getInstance().getAndClearNewlyRemoteAddedEntities()) {
            Optional<SoundComponent> soundComponent = audioManager.getComponent(entity);
            Optional<PlacedCardComponent> cardComponent = cardManager.getComponent(entity);
            if (soundComponent.isPresent() && cardComponent.isPresent()) {
                cardComponent.get().playSound = true;
            }

            Optional<TowerComponent> towerComponent = towerManager.getComponent(entity);
            if (soundComponent.isPresent() && towerComponent.isPresent()) {

                towerComponent.get().playSound = true;
            }

            Optional<EnemyComponent> enemyComponent = enemyManager.getComponent(entity);
            if (soundComponent.isPresent() && enemyComponent.isPresent()) {
                soundController.playSound(soundComponent.get());
            }
        }

        for (Entity entity : entities) {
            // If the entity does not have a sound component, skip it.
            Optional<SoundComponent> soundComponent = audioManager.getComponent(entity);
            if (soundComponent.isPresent()) {
                if (soundComponent.get().isPlaying) {
                    continue;
                }

                if (soundComponent.get().isBackgroundMusic) {
                    soundController.playBackgroundMusic(soundComponent.get());
                    soundComponent.get().isPlaying = true;

                } else {
                    // TODO: Case for cards
                    Optional<PlacedCardComponent> cardComponent = cardManager.getComponent(entity);
                    if (cardComponent.isPresent() && cardComponent.get().playSound == true) {

                        soundController.playSound(soundComponent.get());
                        cardComponent.get().playSound = false;
                    }

                    // TODO: Case for towers
                    Optional<TowerComponent> towerComponent = towerManager.getComponent(entity);
                    if (towerComponent.isPresent() && towerComponent.get().playSound == true
                            && !towersWithoutSounds.contains(towerComponent.get().towerType)) {
                        soundController.playSound(soundComponent.get());
                        towerComponent.get().playSound = false;
                    }
                }
            }
        }
    }
}
