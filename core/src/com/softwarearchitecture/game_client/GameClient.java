package com.softwarearchitecture.game_client;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Controllers;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlayerComponent;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.MenuEnum;
import com.softwarearchitecture.game_client.states.ScreenManager;

public class GameClient {
    private Controllers defaultControllers;
    private ScreenManager screenManager;
    private static Process serverProcess = null;
    private UUID yourId;

    public GameClient(Controllers defaultControllers) throws IllegalArgumentException {
        this.defaultControllers = defaultControllers;
        screenManager = ScreenManager.getInstance();
        screenManager.nextState(new Menu(MenuEnum.MENU, defaultControllers));
        yourId = UUID.randomUUID();

    }
    

    public void init() {
        // currentState.init(graphicsController);
    }

    public void update() {
        screenManager.activateCurrentStateIfChanged();
        
        addSelfPlayerIfNotExists();

        float deltaTime = 1f; // TODO: Implement deltatime

        ECSManager.getInstance().update(deltaTime);
    }

    private void addSelfPlayerIfNotExists() {
        Set<Entity> entities = ECSManager.getInstance().getEntities();
        boolean hasPlayer = false;
        for (Entity entity : entities) {
            ComponentManager<PlayerComponent> playerComponentManager = ECSManager.getInstance().getOrDefaultComponentManager(PlayerComponent.class);
            Optional<PlayerComponent> playerComponent = playerComponentManager.getComponent(entity);
            if (!(playerComponent.isPresent() && playerComponent.get().playerID == yourId)) continue;
            hasPlayer = true;
            break;
        }
        if (!hasPlayer) {
            Entity player = new Entity();
            player.addComponent(PlayerComponent.class, new PlayerComponent(yourId));
            ECSManager.getInstance().addEntity(player);
        }
    }
}
