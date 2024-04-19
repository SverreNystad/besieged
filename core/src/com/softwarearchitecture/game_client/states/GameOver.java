package com.softwarearchitecture.game_client.states;

import java.util.UUID;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.math.Vector3;

public class GameOver extends State implements Observer {

    private String title = "GAME OVER";

    public GameOver(Controllers defaultControllers, UUID yourId) {
        super(defaultControllers, yourId);
    }

    public GameOver(Controllers defaultControllers, UUID yourId, String title) {
        super(defaultControllers, yourId);
        this.title = title;
    }

    @Override
    protected void activate() {
        // Clear current game UI components
        ECSManager.getInstance().clearLocalEntities();

        // Display Game Over text
        Entity gameOverTextEntity = new Entity();
        TextComponent gameOverText = new TextComponent(title, new Vector2(0.1f, 0.1f));
        gameOverText.setColor(new Vector3(1f, 0f, 0f)); // Red text
        PositionComponent textPosition = new PositionComponent(new Vector2(0.34f, 0.56f), 10); // Position near the top and centered
        gameOverTextEntity.addComponent(TextComponent.class, gameOverText);
        gameOverTextEntity.addComponent(PositionComponent.class, textPosition);
        ECSManager.getInstance().addLocalEntity(gameOverTextEntity);

        // Create button to return to main menu using ButtonFactory
        Vector2 mainMenuButtonSize = new Vector2(0.2f, 0.1f);
        Entity mainMenuButton = ButtonFactory.createAndAddButtonEntity(ButtonEnum.BACK_MENU, new Vector2(0.4f, 0.4f), mainMenuButtonSize, this, 5);
        ECSManager.getInstance().addLocalEntity(mainMenuButton);

        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        ECSManager.getInstance().addSystem(inputSystem);
        ECSManager.getInstance().addSystem(renderingSystem);
    }

    @Override
    public void onAction(ButtonEnum type) {
        switch (type) {
            case BACK_MENU:
                // Return to main menu
                ScreenManager.getInstance().nextState(new Menu(defaultControllers, yourId));
                break;
            case QUIT:
                // Quit the game
                System.exit(0);
                break;
            default:
                break;
        }
    }
 }
