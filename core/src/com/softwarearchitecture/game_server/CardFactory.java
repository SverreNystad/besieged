package com.softwarearchitecture.game_server;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.math.Vector2;

public class CardFactory {

    public enum CardType {
        ICE,
        FIRE,
        TECHNOLOGY,
        LIGHTNING,
        BOW,
        MAGIC
    }

    public static Entity createCard(CardType type, Vector2 position) throws NullPointerException {
        if (type == null) {
            throw new IllegalArgumentException("Card type cannot be null");
        }
        String texturePath = "texturePath";
        Vector2 size = new Vector2(0.02f, 0.02f);
        int cost = 0;
        String sound = "soundPath";

        switch (type) {
            case ICE:
                texturePath = TexturePack.CARD_ICE;
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case FIRE:
                texturePath = TexturePack.CARD_FIRE;
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case TECHNOLOGY:
                texturePath = TexturePack.CARD_TECHNOLOGY;
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case LIGHTNING:
                texturePath = TexturePack.CARD_LIGHTNING;
                cost = 500;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case BOW:
                texturePath = TexturePack.CARD_BOW;
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case MAGIC:
                texturePath = TexturePack.CARD_MAGIC;
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            default:
                throw new NullPointerException("Invalid card type");
        }

        PlacedCardComponent placedCardComponent = new PlacedCardComponent((int) position.x, (int) position.y, type);
        PositionComponent positionComponent = new PositionComponent(position, 0);
        SpriteComponent spriteComponent = new SpriteComponent(texturePath, size);
        SoundComponent soundComponent = new SoundComponent(sound);
        MoneyComponent moneyComponent = new MoneyComponent(cost);

        Entity cardEntity = new Entity();
        cardEntity.addComponent(PlacedCardComponent.class, placedCardComponent);
        cardEntity.addComponent(PositionComponent.class, positionComponent);
        cardEntity.addComponent(SpriteComponent.class, spriteComponent);
        cardEntity.addComponent(SoundComponent.class, soundComponent);
        cardEntity.addComponent(MoneyComponent.class, moneyComponent);

        return cardEntity;
    }

}
