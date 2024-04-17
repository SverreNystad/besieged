package com.softwarearchitecture.game_server;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.math.Vector2;

public class CardFactory {

    public enum CardType {
        BOW,
        FIRE,
        LIGHTNING,
        MAGIC,
        TECHNOLOGY,
    }

    public static Entity createCard(CardType type, Vector2 position, boolean placed) throws NullPointerException {
        if (type == null) {
            throw new IllegalArgumentException("Card type cannot be null");
        }
        String texturePath = "texturePath";
        Vector2 size = new Vector2(0.02f, 0.02f);
        int cost = 0;
        String sound = "soundPath";

        switch (type) {

            case FIRE:
                if (placed) {
                    texturePath = TexturePack.CARD_FIRE_PLACED;
                } else {
                    texturePath = TexturePack.CARD_FIRE;
                }
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case TECHNOLOGY:
                if (placed) {
                    texturePath = TexturePack.CARD_TECHNOLOGY_PLACED;
                } else {
                    texturePath = TexturePack.CARD_TECHNOLOGY;
                }
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case LIGHTNING:
                if (placed) {
                    texturePath = TexturePack.CARD_LIGHTNING_PLACED;
                } else {
                    texturePath = TexturePack.CARD_LIGHTNING;
                }
                cost = 500;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case BOW:
                if (placed) {
                    texturePath = TexturePack.CARD_BOW_PLACED;
                } else {
                    texturePath = TexturePack.CARD_BOW;
                }
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            case MAGIC:
                if (placed) {
                    texturePath = TexturePack.CARD_MAGIC_PLACED;
                } else {
                    texturePath = TexturePack.CARD_MAGIC;
                }
                cost = 100;
                sound = AudioPack.JENS; // TODO: Add the correct sound path
                break;

            default:
                throw new NullPointerException("Invalid card type");
        }

        PlacedCardComponent placedCardComponent = new PlacedCardComponent(type);
        PositionComponent positionComponent = new PositionComponent(position, 20);
        SpriteComponent spriteComponent = new SpriteComponent(texturePath, size);
        SoundComponent soundComponent = new SoundComponent(sound);
        CostComponent costComponent = new CostComponent(cost);

        Entity cardEntity = new Entity();
        cardEntity.addComponent(PlacedCardComponent.class, placedCardComponent);
        cardEntity.addComponent(PositionComponent.class, positionComponent);
        cardEntity.addComponent(SpriteComponent.class, spriteComponent);
        cardEntity.addComponent(SoundComponent.class, soundComponent);
        cardEntity.addComponent(CostComponent.class, costComponent);

        return cardEntity;
    }

}
