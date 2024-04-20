package com.softwarearchitecture.game_client.states;

import java.util.List;
import java.util.UUID;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.ecs.systems.RenderingSystem;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;
import com.softwarearchitecture.game_client.Controllers;
import com.softwarearchitecture.game_client.Score;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.math.Vector2;

public class Highscores extends State implements Observer {
    
    private final int MAX_HIGHSCORES = 5;

    public Highscores(Controllers defaultControllers, UUID yourId) {
        super(defaultControllers, yourId);
    }
    
    @Override
    public void onAction(ButtonEnum type) {
        // TODO Auto-generated method stub
        switch (type) {
            case BACK:
            screenManager.nextState(new Menu(defaultControllers, yourId));
            break;
            default:
            break;
        }
    }
    
    @Override
    protected void activate() {
        
        // set background image
        String backgroundPath = TexturePack.BACKGROUND_TOR;
        SpriteComponent backgroundSprite = new SpriteComponent(backgroundPath, new Vector2(1, 1));
        PositionComponent backgroundPosition = new PositionComponent(new Vector2(0f, 0f), -1);
        Entity background = new Entity();
        background.addComponent(SpriteComponent.class, backgroundSprite);
        background.addComponent(PositionComponent.class, backgroundPosition);
        ECSManager.getInstance().addLocalEntity(background);
        


        // Add the sign
        Entity board = new Entity();
        SpriteComponent logoSprite = new SpriteComponent(TexturePack.BIG_EMPTY_SING, new Vector2(0.5f, 1f));
        PositionComponent logoPosition = new PositionComponent(new Vector2(0.5f - 0.25f, 0f), 1);
        board.addComponent(SpriteComponent.class, logoSprite);
        board.addComponent(PositionComponent.class, logoPosition);
        ECSManager.getInstance().addLocalEntity(board);

        Entity logoTextEntity = new Entity();
        TextComponent logoText = new TextComponent("Highscores", new Vector2(0.05f * 1.5f, 0.05f * 1.5f));
        PositionComponent logoTextPosition = new PositionComponent(new Vector2(0.5f - 0.13f, 0.6f), 1);
        logoTextEntity.addComponent(TextComponent.class, logoText);
        logoTextEntity.addComponent(PositionComponent.class, logoTextPosition);
        ECSManager.getInstance().addLocalEntity(logoTextEntity);
        
        // add buttons
        ButtonFactory.createAndAddButtonEntity(ButtonEnum.BACK, new Vector2(0.5f - 0.30f / 2f, 0.10f),
        new Vector2(0.30f, 0.10f), this, 0);
        
        
        // Add highscores

        List<Score> allScores = defaultControllers.onlineClientMessagingController.getAllHighScores();
        
        // Find the top scores
        List<Score> highScores = allScores.subList(0, Math.min(MAX_HIGHSCORES, allScores.size()));
                for (int i = 0; i < highScores.size(); i++) {
            
            Entity scoreEntity = new Entity();
            String idText = highScores.get(i).getGameId().toString();
            Vector2 scoreSize = new Vector2(0.05f * 0.8f, 0.05f * 0.8f);
            TextComponent scoreText = new TextComponent(idText.substring(0, 12) + " Survived " + highScores.get(i).getWavesSurvived() + " waves", scoreSize);
            PositionComponent scorePosition = new PositionComponent(new Vector2(0.5f - 0.2f, 0.5f - 0.10f * i), 1);

            scoreEntity.addComponent(TextComponent.class, scoreText); 
            scoreEntity.addComponent(PositionComponent.class, scorePosition);
            ECSManager.getInstance().addLocalEntity(scoreEntity);
        }

        // Add systems to the ECSManager
        RenderingSystem renderingSystem = new RenderingSystem(defaultControllers.graphicsController);
        InputSystem inputSystem = new InputSystem(defaultControllers.inputController);
        ECSManager.getInstance().addSystem(renderingSystem);
        ECSManager.getInstance().addSystem(inputSystem);
    }

}
