package com.softwarearchitecture.game_client.states;

import java.util.Optional;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.Controllers;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.game_server.TexturePack;
import com.softwarearchitecture.math.Vector2;

public class Options extends State implements Observer {

    Entity volumeText;

    public Options(Controllers defaultControllers) {
        super(defaultControllers);
    }
    
    @Override
    protected void activate() {
        // Background
        String backgroundPath = TexturePack.BACKGROUND_ABYSS;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0, 0), -1);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        TextComponent textComponent = new TextComponent("Options!", new Vector2(0.05f, 0.05f));
        background.addComponent(TextComponent.class, textComponent);
        ECSManager.getInstance().addEntity(background);
        
        // Buttons
        Entity backButton = ButtonFactory.createAndAddButtonEntity(TypeEnum.BACK, new Vector2(0.5f-0.25f/2f, 0f), new Vector2(0.25f, 0.25f), this, 0);
        
        // Volume options
        Entity plussButton = ButtonFactory.createAndAddButtonEntity(TypeEnum.PLUSS, new Vector2(0.75f, 0.75f), new Vector2(0.125f, 0.125f), this, 0);
        Entity minusButton = ButtonFactory.createAndAddButtonEntity(TypeEnum.MINUS, new Vector2(0.25f-0.25f/2f, 0.75f), new Vector2(0.125f, 0.125f), this, 0);
        Entity muteButton = ButtonFactory.createAndAddButtonEntity(TypeEnum.MUTE, new Vector2(0.75f+0.25f/2f, 0.75f), new Vector2(0.125f, 0.125f), this, 0);
        volumeText = new Entity();
        TextComponent volumeTextComponent = new TextComponent("Volume: " + defaultControllers.soundController.getVolume(), new Vector2(0.05f, 0.05f));
        PositionComponent volumeTextPosition = new PositionComponent(new Vector2(0.5f-0.25f/2f, 0.75f), 0);
        volumeText.addComponent(TextComponent.class, volumeTextComponent);
        volumeText.addComponent(PositionComponent.class, volumeTextPosition);
        ECSManager.getInstance().addEntity(volumeText);

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
    }

    @Override
    public void onAction(TypeEnum type) {
        switch (type) {
            case BACK:
                ScreenManager.getInstance().previousState();
                break;
            case PLUSS:
                System.out.println("Pressed pluss!");
                defaultControllers.soundController.setVolume(defaultControllers.soundController.getVolume() + 1);
                break;
            case MINUS:
                System.out.println("Pressed minus!");
                defaultControllers.soundController.setVolume(defaultControllers.soundController.getVolume() - 1);
                break;
            case MUTE:
                System.out.println("Pressed mute!");
                if (defaultControllers.soundController.getVolume() > 0) {
                    defaultControllers.soundController.setVolume(0);
                } else if (defaultControllers.soundController.getVolume() == 0) {
                    defaultControllers.soundController.setVolume(100);
                }
                break;
            default:
                break;
        }
        ComponentManager<TextComponent> textComponentManager = ECSManager.getInstance().getOrDefaultComponentManager(TextComponent.class);
        Optional<TextComponent> volumeTextComponent = textComponentManager.getComponent(volumeText);
        if (volumeTextComponent.isPresent()) {
            volumeTextComponent.get().text = "Volume: " + defaultControllers.soundController.getVolume();
        }
    }
}
