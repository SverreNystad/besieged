package com.softwarearchitecture.game_server;

import com.badlogic.gdx.math.Vector2;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.PairableCards.TowerType;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;

import java.util.Optional;

public class TowerFactory {
    public static Entity createTower(CardType cardType1, CardType cardType2, Vector2 position) {
        String texture = "defaultTexturePath";
        Vector2 size = new Vector2(1, 1);
        int damage = 0;
        int range = 0;
        
        Optional<TowerType> towerType = PairableCards.getTower(cardType1, cardType2);

        if (!towerType.isPresent()) {
            throw new IllegalArgumentException("Invalid tower type combination");
        }

        switch (towerType.get()) {
            case FIRE_MAGIC:
                texture = TexturePack.FIRE_MAGIC;
                damage = 5;
                range = 3;
                break;
            case ICE_MAGIC:
                texture = TexturePack.ICE_MAGIC;
                damage = 6;
                range = 4;
                break;
            case TOR:
                texture = TexturePack.TOR;
                damage = 7;
                range = 5;
                break;
            case MAGIC:
                texture = TexturePack.MAGIC;
                damage = 8;
                range = 3;
                break;
            case FIRE_BOW:
                texture = TexturePack.FIRE_BOW;
                damage = 6;
                range = 2;
                break;
            case SHARP_SHOOTER:
                texture = TexturePack.SHARP_SHOOTER;
                damage = 9;
                range = 4;
                break;
            case BOW:
                texture = TexturePack.BOW;
                damage = 4;
                range = 3;
                break;
        }

        // Create the components for the tower
        TowerComponent towerComponent = new TowerComponent(damage, range);
        
        SpriteComponent spriteComponent = new SpriteComponent(texture, size, 1);

        // Create the tower entity and add the components
        Entity tower = new Entity();
        tower.addComponent(TowerComponent.class, towerComponent);
        tower.addComponent(SpriteComponent.class, spriteComponent);

        return tower;
    }
}
