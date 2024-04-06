package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_client.State;

public class MainMenu implements State {

    private ArrayList<Entity> entities;

    @Override
    public void init(GraphicsController graphicsController) {
        Entity background = new Entity();
        SpriteComponent sprite = new SpriteComponent("BlackBackground.png", 0, 0, 1, 1);
        background.addComponent(SpriteComponent.class, sprite);
        ECSManager.getInstance().addEntity(background);

        Entity logo = new Entity();
        SpriteComponent logoSprite = new SpriteComponent("BesiegedLogoPartlyTransparent.png", 0.375f, 0.f, 0.25f, 0.25f);
        logo.addComponent(SpriteComponent.class, logoSprite);
        ECSManager.getInstance().addEntity(logo);

        ECSManager.getInstance().addSystem(new RenderingSystem(ECSManager.getInstance().getOrDefaultComponentManager(SpriteComponent.class), graphicsController));
    }

    @Override
    public void update(float deltaTime) {
        ECSManager.getInstance().update(1.f);
    }

    @Override
    public void dispose() {
        ECSManager.getInstance().clearAll();
    }

}
