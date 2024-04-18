package com.softwarearchitecture.game_server;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.PairableCards.TowerType;
import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.AreaOfAffectComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TargetComponent;
import com.softwarearchitecture.ecs.components.TowerComponent;
import com.softwarearchitecture.game_client.TexturePack;

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
        float range = 0;
        int attackCooldown = 0;
        String sound = AudioPack.PLACING_CARD; //TODO: Add default sound

        Optional<TowerType> towerType = PairableCards.getTower(cardType1, cardType2);

        if (!towerType.isPresent()) {
            throw new IllegalArgumentException("Invalid tower type combination");
        }

        AreaOfAffectComponent areaOfAffectComponent = null;

        switch (towerType.get()) {
            case FIRE_MAGIC:
                textures.add(TexturePack.TOWER_FIRE_FRAME1);
                textures.add(TexturePack.TOWER_FIRE_FRAME2);
                textures.add(TexturePack.TOWER_FIRE_FRAME3);
                textures.add(TexturePack.TOWER_FIRE_ATTACK_FRAME1);
                textures.add(TexturePack.TOWER_FIRE_ATTACK_FRAME2);
                textures.add(TexturePack.TOWER_FIRE_ATTACK_FRAME3);

                damage = 15;
                range = 2;
                attackCooldown = 40;
                sound = AudioPack.TOWER_FIRE_MAGIC; // TODO: Add the correct sound path
                break;
            case TOR:
                textures.add(TexturePack.TOWER_TOR_FRAME1);
                textures.add(TexturePack.TOWER_TOR_FRAME2);
                textures.add(TexturePack.TOWER_TOR_FRAME3);
                textures.add(TexturePack.TOWER_TOR_FRAME2);
                textures.add(TexturePack.TOWER_TOR_FRAME1);
                damage = 20;
                range = 3;
                attackCooldown = 75;
                sound = AudioPack.TOWER_TOR;
                break;
            case MAGIC:
                textures.add(TexturePack.TOWER_MAGIC_FRAME1);
                textures.add(TexturePack.TOWER_MAGIC_FRAME2);
                textures.add(TexturePack.TOWER_MAGIC_FRAME3);
                textures.add(TexturePack.TOWER_MAGIC_FRAME2);
                textures.add(TexturePack.TOWER_MAGIC_FRAME1);
                damage = 13;
                range = 3;
                attackCooldown = 80;
                sound = AudioPack.TOWER_MAGIC;
                break;
            case FIRE_BOW:
                textures.add(TexturePack.TOWER_BOW_FIRE);
                damage = 7;
                range = 2;
                attackCooldown = 35;
                sound = AudioPack.TOWER_FIRE_BOW;
                break;
            case SHARP_SHOOTER:
                textures.add(TexturePack.TOWER_SHARPSHOOTER);
                damage = 40;
                range = 10f;
                attackCooldown = 100;
                sound = AudioPack.TOWER_SHARP_SHOOTER;
                break;
            case BOW:
                textures.add(TexturePack.TOWER_BOW);
                damage = 12;
                range = 3;
                attackCooldown = 45;
                sound = AudioPack.TOWER_BOW;
                break;

            case BOW_LIGHTING:
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME1);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME1);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME1);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME2);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME3);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME4);
                areaOfAffectComponent = new AreaOfAffectComponent();
                damage = 30;
                range = 2.5f;
                attackCooldown = 100;
                sound = AudioPack.TOWER_BOW;
                break;

            case MAGIC_TECH:
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME1);
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME1);
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME1);
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME1);
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME2);
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME1);
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME3);
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME1);
                textures.add(TexturePack.TOWER_MAGIC_TECH_FRAME4);
                areaOfAffectComponent = new AreaOfAffectComponent();

                damage = 45;
                range = 2;
                attackCooldown = 50;
                sound = AudioPack.TOWER_BOW;
                break;

            case MORTAR:
                textures.add(TexturePack.MORTAR_FRAME1);
                textures.add(TexturePack.MORTAR_FRAME2);
                textures.add(TexturePack.MORTAR_FRAME3);
                textures.add(TexturePack.MORTAR_FRAME4);
                textures.add(TexturePack.MORTAR_FRAME5);
                textures.add(TexturePack.MORTAR_FRAME6);
                textures.add(TexturePack.MORTAR_FRAME1);
                damage = 100;
                range = 4.5f;
                attackCooldown = 150;
                sound = AudioPack.TOWER_BOW;
                break;

            case INFERNO:
                textures.add(TexturePack.TOWER_INFERNO_FRAME1);
                textures.add(TexturePack.TOWER_INFERNO_FRAME2);
                textures.add(TexturePack.TOWER_INFERNO_FRAME3);
                textures.add(TexturePack.TOWER_INFERNO_FRAME4);
                textures.add(TexturePack.TOWER_INFERNO_FRAME5);
                textures.add(TexturePack.TOWER_INFERNO_FRAME6);
                areaOfAffectComponent = new AreaOfAffectComponent();

                damage = 1;
                range = 1;
                attackCooldown = 2;
                sound = AudioPack.TOWER_BOW;
                break;

        }

        // Create the components for the tower
        TowerComponent towerComponent = new TowerComponent(damage, range, attackCooldown);
        PositionComponent positionComponent = new PositionComponent(position, 10);
        AnimationComponent animationComponent = new AnimationComponent(textures);
        SpriteComponent spriteComponent = new SpriteComponent(textures.get(0), size);
        TargetComponent targetComponent = new TargetComponent();
        SoundComponent soundComponent = new SoundComponent(sound, false, false); // TODO: Add the correct sound path
        // Create the tower entity and add the components
        Entity tower = new Entity();
        tower.addComponent(TowerComponent.class, towerComponent);
        tower.addComponent(SpriteComponent.class, spriteComponent);
        tower.addComponent(PositionComponent.class, positionComponent);
        tower.addComponent(AnimationComponent.class, animationComponent);
        tower.addComponent(TargetComponent.class, targetComponent);
        tower.addComponent(SoundComponent.class, soundComponent);
        if (areaOfAffectComponent != null) {
            tower.addComponent(AreaOfAffectComponent.class, areaOfAffectComponent);
        }
        
        return tower;
    }
}
