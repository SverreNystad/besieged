package com.softwarearchitecture.game_server;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.PairableCards.TowerType;
import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TargetComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TowerFactory {
    public static Entity createTower(CardType cardType1, CardType cardType2, Vector2 position) {
        if (cardType1 == null || cardType2 == null) {
            throw new IllegalArgumentException("Card type cannot be null");
        }
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        List<String> textures = new ArrayList<String>();
        Vector2 size = new Vector2(1, 1);
        int damage = 0;
        int range = 0;
        String sound = "soundPath";

        Optional<TowerType> towerType = PairableCards.getTower(cardType1, cardType2);

        if (!towerType.isPresent()) {
            throw new IllegalArgumentException("Invalid tower type combination");
        }

        switch (towerType.get()) {
            case FIRE_MAGIC:
                textures.add(TexturePack.FIRE_MAGIC); // TODO: Add the correct textures to texturePack
                // TODO: add more frames to the animation
                damage = 5;
                range = 3;
                sound = AudioPack.TOWER_FIRE_MAGIC; // TODO: Add the correct sound path
                break;
            case ICE_MAGIC:
                textures.add(TexturePack.ICE_MAGIC);
                damage = 6;
                range = 4;
                sound = AudioPack.TOWER_ICE_MAGIC;
                break;
            case TOR:
                textures.add(TexturePack.TOR);
                damage = 7;
                range = 5;
                sound = AudioPack.TOWER_TOR;
                break;
            case MAGIC:
                textures.add(TexturePack.MAGIC);
                damage = 8;
                range = 3;
                sound = AudioPack.TOWER_MAGIC;
                break;
            case FIRE_BOW:
                textures.add(TexturePack.FIRE_BOW);
                damage = 6;
                range = 2;
                sound = AudioPack.TOWER_FIRE_BOW;
                break;
            case SHARP_SHOOTER:
                textures.add(TexturePack.SHARP_SHOOTER);
                damage = 9;
                range = 4;
                sound = AudioPack.TOWER_SHARP_SHOOTER;
                break;
            case BOW:
                textures.add(TexturePack.BOW);
                damage = 4;
                range = 3;
                sound = AudioPack.TOWER_BOW;
                break;
        }

        // Create the components for the tower
        TowerComponent towerComponent = new TowerComponent(damage, range);
        PositionComponent positionComponent = new PositionComponent(position);
        AnimationComponent animationComponent = new AnimationComponent(textures);
        SpriteComponent spriteComponent = new SpriteComponent(textures.get(0), size, 1);
        TargetComponent targetComponent = new TargetComponent();
        SoundComponent soundComponent = new SoundComponent(sound); // TODO: Add the correct sound path

        // Create the tower entity and add the components
        Entity tower = new Entity();
        tower.addComponent(TowerComponent.class, towerComponent);
        tower.addComponent(SpriteComponent.class, spriteComponent);
        tower.addComponent(PositionComponent.class, positionComponent);
        tower.addComponent(AnimationComponent.class, animationComponent);
        tower.addComponent(TargetComponent.class, targetComponent);
        tower.addComponent(SoundComponent.class, soundComponent);

        return tower;
    }
}
