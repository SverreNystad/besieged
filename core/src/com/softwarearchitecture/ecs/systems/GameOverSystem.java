package com.softwarearchitecture.ecs.systems;

import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.VillageComponent;
import com.softwarearchitecture.game_client.states.GameOverObserver;

/**
 * The {@code GameOverSystem} class is responsible for monitoring the health of the village entity
 * and triggering a game over condition when its health drops to zero or below. This system uses 
 * component managers to access health components associated with entities that have a village component.
 * 
 * <p>This system is crucial for the game's state management, as it connects the condition of the village's
 * health to the broader game state, deciding when the game should end. The system uses a {@link GameOverObserver}
 * to notify other parts of the game that a game over condition has occurred.</p>
 *
 * <p>Integrates closely with {@link HealthComponent} and {@link VillageComponent} to monitor and react to 
 * the state of the village's health.</p>
 */
public class GameOverSystem implements System {

    private ComponentManager<HealthComponent> healthManager;
    private ComponentManager<VillageComponent> villageManger;
    private GameOverObserver observer;

    public GameOverSystem(GameOverObserver observer) {
        this.observer = observer;
        this.healthManager = ECSManager.getInstance().getOrDefaultComponentManager(HealthComponent.class);
        this.villageManger = ECSManager.getInstance().getOrDefaultComponentManager(VillageComponent.class);
    }

    /**
     * Updates the game over system by checking the health of the village entity each frame.
     * If the village's health is zero or less, it triggers the game over process through
     * the observer.
     *
     * @param entities   the set of entities to be processed, potentially including the village
     * @param deltaTime  the time since the last update, not directly used here but necessary for system interface
     */
    @Override
    public void update(Set<Entity> entities, float deltaTime) {
        Entity village = null;

        for (Entity entity : entities) {
            Optional<HealthComponent> healthComponent = healthManager.getComponent(entity);
            Optional<VillageComponent> villageComponent = villageManger.getComponent(entity);
            if (villageComponent.isPresent() && healthComponent.isPresent()) {
                village = entity;
                break;
            }
        }

        if (village == null) {
            return;
        }

        if (healthManager.getComponent(village).get().getHealth() <= 0) {
            if (this.observer != null) {
                this.observer.handleGameOver();
            }
        }
    }

}
