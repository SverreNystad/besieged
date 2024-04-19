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

public class GameOverSystem implements System {

    private ComponentManager<HealthComponent> healthManager;
    private ComponentManager<VillageComponent> villageManger;
    private GameOverObserver observer;

    public GameOverSystem(GameOverObserver observer) {
        this.observer = observer;
        this.healthManager = ECSManager.getInstance().getOrDefaultComponentManager(HealthComponent.class);
        this.villageManger = ECSManager.getInstance().getOrDefaultComponentManager(VillageComponent.class);
    }

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
            java.lang.System.out.println("Game Over");
            if (this.observer != null) {
                this.observer.handleGameOver();
            }
        }
    }
    
}
