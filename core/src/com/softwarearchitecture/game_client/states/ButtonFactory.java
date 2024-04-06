package com.softwarearchitecture.game_client.states;

import java.util.List;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.ButtonComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.game_client.screen_components.Button;
import com.softwarearchitecture.game_client.states.Observer;
import com.softwarearchitecture.game_client.states.TexturePack;
import com.softwarearchitecture.math.Rectangle;
import com.softwarearchitecture.math.Vector2;

/**
 * This is a button factory
 */
public class ButtonFactory {

    /**
     * Creates a button based on the button type and adds it to the ECS system
     * 
     * @param button:   TypeEnum
     * @param size:     Vector2
     * @param observer: Observer
     * @throws IllegalArgumentException if the button type is invalid
     */
    public static Entity createAndAddButtonEntity(TypeEnum button, Vector2 position, Vector2 size, Observer observer, int z_index) throws IllegalArgumentException {
        // factory that makes buttons based on the state enum
        String texture = TexturePack.BUTTON_PLACEHOLDER;
        switch (button) {
            case OPTIONS:
                // create options buttons
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case GAME_MENU:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case QUIT:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case JOIN:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case HOST:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case PAUSE:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case MULTI_PLAYER:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case SINGLE_PLAYER:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case PLAY:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            case BACK:
                texture = TexturePack.BUTTON_PLACEHOLDER;
                break;
            default:
                throw new IllegalArgumentException("Invalid button type");
        }

        ButtonComponent buttonComponent = new ButtonComponent(new Vector2(0, 0), size, button, z_index);
        PositionComponent positionComponent = new PositionComponent(position);
        SpriteComponent spriteComponent = new SpriteComponent(texture, size, z_index);

        Entity buttonEntity = new Entity();
        buttonEntity.addComponent(ButtonComponent.class, buttonComponent);
        buttonEntity.addComponent(PositionComponent.class, positionComponent);
        buttonEntity.addComponent(SpriteComponent.class, spriteComponent);

        ECSManager.getInstance().addEntity(buttonEntity);
        return buttonEntity;
    }
}
