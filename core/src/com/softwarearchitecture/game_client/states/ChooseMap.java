package com.softwarearchitecture.game_client.states;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.math.Vector2;

// import systems
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;

public class ChooseMap extends State implements Observer {
    private final int PAGE_Z_INDEX = 1;
    private final int TEXT_Z_INDEX = PAGE_Z_INDEX + 2;
    private final int BUTTON_Z_INDEX = PAGE_Z_INDEX + 1;
    private final int MAP_PREVIEW_Z_INDEX = PAGE_Z_INDEX + 3;

    private boolean isMultiplayer;
    /**
     * Generic state is a state that can be used for multiple purposes
     * for different types of menus.
     * 
     * parameters: type: GenericStateType, wich is an enum that defines
     * the use of the state
     */
    public ChooseMap(Controllers defaultControllers, UUID yourId, boolean isMultiplayer) {
        super(defaultControllers, yourId);
        this.isMultiplayer = isMultiplayer;
    }

    @Override
    protected void activate() {
        // Set background image
        String backgroundPath = TexturePack.BACKGROUND_MULTIPLAYER;
        Entity background = new Entity();
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0f, 0f), PAGE_Z_INDEX);
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        ECSManager.getInstance().addLocalEntity(background);
        System.out.println("Menu activated");

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);

        // Add the choose map
        Entity logo = new Entity();
        SpriteComponent logoSprite = new SpriteComponent(TexturePack.CHOOSE_MAP, new Vector2(0.35f, 0.06f));
        PositionComponent logoPosition = new PositionComponent(new Vector2(0.5f - 0.35f / 2, 0.75f), TEXT_Z_INDEX);
        logo.addComponent(SpriteComponent.class, logoSprite);
        logo.addComponent(PositionComponent.class, logoPosition);
        ECSManager.getInstance().addLocalEntity(logo);

        

        // Set up the UI elements
        List<Entity> buttons = new ArrayList<>();
        float buttonWidth = 0.25f;
        float buttonHeight = 0.3f;
        float gap = 0.02f;
        float translateY = 0.1f;

        // TODO: Make map fetching dynamic

        // Create button rectangles
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.ABYSS,
                new Vector2(0.5f - buttonWidth - 0.01f, translateY + 0.25f),
                new Vector2(buttonWidth, buttonHeight), this, BUTTON_Z_INDEX));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.TEST,
                new Vector2(0.5f + 0.01f, translateY + 0.25f),
                new Vector2(buttonWidth, buttonHeight), this, BUTTON_Z_INDEX));
        buttons.add(ButtonFactory.createAndAddButtonEntity(ButtonEnum.BACK,
                new Vector2(0.495f - 0.35f / 2, translateY),
                new Vector2(0.35f, 0.1f), this, BUTTON_Z_INDEX));

        // Create first Preview Image
        Entity mapPreview = new Entity();
        String imagePath = TexturePack.PREVIEW_ABYSS;
        SpriteComponent mapPreviewSprite = new SpriteComponent(imagePath, new Vector2(buttonWidth, buttonHeight - 0.05f)); // Adjust the size as necessary
        PositionComponent mapPreviewPosition = new PositionComponent(new Vector2(0.5f - buttonWidth - 0.01f, translateY + 0.31f), MAP_PREVIEW_Z_INDEX); // Adjust the position as necessary
        mapPreview.addComponent(SpriteComponent.class, mapPreviewSprite);
        mapPreview.addComponent(PositionComponent.class, mapPreviewPosition);
        ECSManager.getInstance().addLocalEntity(mapPreview);

        // Create second Preview Image
        Entity mapPreview2 = new Entity();
        String imagePath2 = TexturePack.PREVIEW_CLEARING;
        SpriteComponent mapPreviewSprite2 = new SpriteComponent(imagePath2, new Vector2(buttonWidth, buttonHeight - 0.05f)); // Adjust the size as necessary
        PositionComponent mapPreviewPosition2 = new PositionComponent(new Vector2(0.77f - buttonWidth - 0.01f, translateY + 0.31f), MAP_PREVIEW_Z_INDEX); // Adjust the position as necessary
        mapPreview2.addComponent(SpriteComponent.class, mapPreviewSprite2);
        mapPreview2.addComponent(PositionComponent.class, mapPreviewPosition2);
        ECSManager.getInstance().addLocalEntity(mapPreview2);
    }

    /**
     * Handles button actions based on the type of the button.
     * This state is only the intermediary menus that traverses to other states.
     * 
     * @param type: ButtonType.
     */
    @Override
    public void onAction(ButtonEnum type) {
        // Switches the state of the game based on the button type

        // TODO: MAKE BUTTON ACTIONS DYNAMIC CHOOSING MAPS
        String map = "";
        switch (type) {
            case ABYSS:
                System.out.println("Abyss map button pressed");
                map = "abyss";
                break;

            case TEST:
                System.out.println("clearing map button pressed");
                map = "clearing";
                break;

            case BACK:
                System.out.println("Back button pressed");
                screenManager.nextState(new Menu(defaultControllers, yourId));
                return;

            default:
                throw new IllegalArgumentException("Invalid button type");
        }
            startServer(map);
        screenManager.setIsLocalServer(!isMultiplayer);
        
        screenManager.nextState(new InGame(defaultControllers, yourId, map));
    }

    private void startServer(String mapName) {
        // Start the server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Starting server on a new thread");
                    defaultControllers.gameServer.run(mapName, isMultiplayer);
                } catch (Exception e) {
                    System.out.println("Error running server: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, "ServerThread").start(); 
    }
}
