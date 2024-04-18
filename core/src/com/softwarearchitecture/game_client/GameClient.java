package com.softwarearchitecture.game_client;

import java.util.Optional;
import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TileComponent;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.ScreenManager;
import com.softwarearchitecture.game_server.GameState;
import com.softwarearchitecture.game_server.Tile;

public class GameClient {
    private ScreenManager screenManager;
    private Controllers defaultControllers;

    public GameClient(Controllers defaultControllers, UUID yourId) throws IllegalArgumentException {
        this.defaultControllers = defaultControllers;
        screenManager = ScreenManager.getInstance();
        screenManager.nextState(new Menu(defaultControllers, yourId));

    }

    public void update() {
        screenManager.activateCurrentStateIfChanged();

        float deltaTime = 1f; // TODO: Implement deltatime

        ECSManager.getInstance().update(deltaTime);

        // Check if the player is in a multiplayer game
        UUID gameId = null;
        if (screenManager.getGameId() != null) {
            gameId = screenManager.getGameId();
            Optional<GameState> game = defaultControllers.clientMessagingController.requestGameState(gameId);
            if (game.isPresent()) {
                removePlacedCardsFromScreen();
                game.get();
                // System.out.println("[CLIENT] Requested game gotten: " + game.get());
            }
        }
        if (defaultControllers.gameServer.getGameId() != null) {
            gameId = defaultControllers.gameServer.getGameId();
            Optional<GameState> game = defaultControllers.clientMessagingController.requestGameState(gameId);
            if (game.isPresent()) {
                removePlacedCardsFromScreen();
                game.get();
                // System.out.println("[CLIENT] Requested game gotten: " + game.get());
            }
        }
        // System.out.println("[CLIENT] Entities " + ECSManager.getInstance().getEntities().size());
    }

    private void removePlacedCardsFromScreen() {
        // ComponentManager<PlacedCardComponent> placedCardManager = ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class);
        ComponentManager<TileComponent> tileManager = ECSManager.getInstance().getOrDefaultComponentManager(TileComponent.class);
        ComponentManager<SpriteComponent> spriteManager = ECSManager.getInstance().getOrDefaultComponentManager(SpriteComponent.class);
        ComponentManager<PlacedCardComponent> placedCardManager = ECSManager.getInstance().getOrDefaultComponentManager(PlacedCardComponent.class);
        for (Entity entity : ECSManager.getInstance().getEntities()) {
            // if (placedCardManager.getComponent(entity).isPresent()) {
            //     placedCardManager.removeComponent(entity);
            // }
            Optional<PlacedCardComponent> placedCardComponent = placedCardManager.getComponent(entity);
            Optional<TileComponent> tileComponent = tileManager.getComponent(entity);
            Optional<SpriteComponent> spriteComponent = spriteManager.getComponent(entity);
            if (placedCardComponent.isPresent() && !tileComponent.isPresent() && spriteComponent.isPresent()) {
                spriteManager.removeComponent(entity);
                ECSManager.getInstance().removeEntity(entity);
            } 
        }
    }
}