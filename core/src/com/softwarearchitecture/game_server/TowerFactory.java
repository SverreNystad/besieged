package com.softwarearchitecture.game_server;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.game_server.PairableCards.TowerType;
import com.softwarearchitecture.math.Vector2;
import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.AreaOfEffectComponent;
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
        Optional<TowerType> towerType = PairableCards.getTower(cardType1, cardType2);

        if (!towerType.isPresent()) {
            throw new IllegalArgumentException("Invalid tower type combination");
        }
        return createTower(towerType.get(), position);
    }

    public static Entity createTower(TowerType towerType, Vector2 position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        List<String> textures = new ArrayList<String>();
        Vector2 size = new Vector2(1, 1);
        int damage = 0;
        float range = 0;
        float attackCooldown = 0;
        String sound = AudioPack.PLACING_CARD;
        float timeFactor = 1f;

        // Optional<TowerType> towerType = PairableCards.getTower(cardType1, cardType2);

        // if (!towerType.isPresent()) {
        // throw new IllegalArgumentException("Invalid tower type combination");
        // }

        AreaOfEffectComponent areaOfEffectComponent = null;

        switch (towerType) {
            case FIRE_MAGIC:
                textures.add(TexturePack.TOWER_FIRE_FRAME1);
                textures.add(TexturePack.TOWER_FIRE_FRAME2);
                textures.add(TexturePack.TOWER_FIRE_FRAME3);
                textures.add(TexturePack.TOWER_FIRE_ATTACK_FRAME1);
                textures.add(TexturePack.TOWER_FIRE_ATTACK_FRAME2);
                textures.add(TexturePack.TOWER_FIRE_ATTACK_FRAME3);

                areaOfEffectComponent = new AreaOfEffectComponent();

                damage = 10;
                range = 2;
                attackCooldown = 1f;
                sound = AudioPack.TOWER_FIRE_MAGIC; // TODO: Add the correct sound path

                break;
            case TOR:
                textures.add(TexturePack.TOWER_TOR_FRAME1);
                textures.add(TexturePack.TOWER_TOR_FRAME2);
                textures.add(TexturePack.TOWER_TOR_FRAME3);
                textures.add(TexturePack.TOWER_TOR_FRAME2);
                textures.add(TexturePack.TOWER_TOR_FRAME1);
                damage = 40;
                range = 3;
                attackCooldown = 5f * timeFactor;
                sound = AudioPack.TOWER_TOR;
                break;
            case MAGIC:
                textures.add(TexturePack.TOWER_MAGIC_FRAME1);
                textures.add(TexturePack.TOWER_MAGIC_FRAME2);
                textures.add(TexturePack.TOWER_MAGIC_FRAME3);
                textures.add(TexturePack.TOWER_MAGIC_FRAME2);
                textures.add(TexturePack.TOWER_MAGIC_FRAME1);
                damage = 30;
                range = 10;
                attackCooldown = 3f * timeFactor;
                sound = AudioPack.TOWER_MAGIC;
                break;
            case FIRE_BOW:
                textures.add(TexturePack.TOWER_BOW_FIRE);
                damage = 40;
                range = 2;
                attackCooldown = 1f * timeFactor;
                sound = AudioPack.TOWER_FIRE_BOW;
                break;
            case SHARP_SHOOTER:
                textures.add(TexturePack.TOWER_SHARPSHOOTER);
                damage = 2000;
                range = 20f;
                attackCooldown = 15f * timeFactor;
                sound = AudioPack.TOWER_SHARP_SHOOTER;
                break;
            case BOW:
                textures.add(TexturePack.TOWER_BOW);
                damage = 30;
                range = 3;
                attackCooldown = 1f * timeFactor;
                sound = AudioPack.TOWER_BOW;
                break;

            case BOW_LIGHTING:
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME1);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME1);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME1);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME2);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME3);
                textures.add(TexturePack.TOWER_BOW_LIGHTING_FRAME4);
                areaOfEffectComponent = new AreaOfEffectComponent();
                damage = 60;
                range = 1.5f;
                attackCooldown = 4f * timeFactor;
                sound = AudioPack.TOWER_BOW_LIGHTNING;
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
                areaOfEffectComponent = new AreaOfEffectComponent();

                damage = 10000;
                range = 1f;
                attackCooldown = 15f * timeFactor;
                sound = AudioPack.TOWER_MAGIC_TECH;
                break;

            case MORTAR:
                textures.add(TexturePack.MORTAR_FRAME1);
                textures.add(TexturePack.MORTAR_FRAME2);
                textures.add(TexturePack.MORTAR_FRAME3);
                textures.add(TexturePack.MORTAR_FRAME4);
                textures.add(TexturePack.MORTAR_FRAME5);
                textures.add(TexturePack.MORTAR_FRAME6);
                textures.add(TexturePack.MORTAR_FRAME1);
                damage = 300;
                range = 4.5f;
                attackCooldown = 7f * timeFactor;
                sound = AudioPack.TOWER_MORTAR;
                break;

            case INFERNO:
                textures.add(TexturePack.TOWER_INFERNO_FRAME1);
                textures.add(TexturePack.TOWER_INFERNO_FRAME2);
                textures.add(TexturePack.TOWER_INFERNO_FRAME3);
                textures.add(TexturePack.TOWER_INFERNO_FRAME4);
                textures.add(TexturePack.TOWER_INFERNO_FRAME5);
                textures.add(TexturePack.TOWER_INFERNO_FRAME6);
                areaOfEffectComponent = new AreaOfEffectComponent();

                damage = 100;
                range = 1;
                attackCooldown = 0.1f * timeFactor;
                sound = AudioPack.TOWER_INFERNO;
                break;

            case FURNACE:
                textures.add(TexturePack.TOWER_FURNACE_FRAME1);
                textures.add(TexturePack.TOWER_FURNACE_FRAME2);
                textures.add(TexturePack.TOWER_FURNACE_FRAME3);
                textures.add(TexturePack.TOWER_FURNACE_FRAME4);
                textures.add(TexturePack.TOWER_FURNACE_FRAME5);
                textures.add(TexturePack.TOWER_FURNACE_FRAME6);

                areaOfEffectComponent = new AreaOfEffectComponent();

                damage = 500;
                range = 2;
                attackCooldown = 4f * timeFactor;

                sound = AudioPack.TOWER_FURNACE;
                break;

            case TESLA:
                textures.add(TexturePack.TOWER_TESLA_FRAME1);
                textures.add(TexturePack.TOWER_TESLA_FRAME2);
                textures.add(TexturePack.TOWER_TESLA_FRAME3);
                textures.add(TexturePack.TOWER_TESLA_FRAME4);
                textures.add(TexturePack.TOWER_TESLA_FRAME5);

                areaOfEffectComponent = new AreaOfEffectComponent();

                damage = 1000;
                range = 1;
                attackCooldown = 6f * timeFactor;

                sound = AudioPack.TOWER_TESLA;

                break;

            case THUNDERBOLT:
                textures.add(TexturePack.TOWER_THUNDERBOLT_FRAME1);
                textures.add(TexturePack.TOWER_THUNDERBOLT_FRAME2);
                textures.add(TexturePack.TOWER_THUNDERBOLT_FRAME3);

                areaOfEffectComponent = new AreaOfEffectComponent();

                damage = 300;
                range = 2;
                attackCooldown = 6f * timeFactor;
                sound = AudioPack.TOWER_THUNDERBOLT;

                break;

            case BOW_MAGIC:
                textures.add(TexturePack.TOWER_BOW_MAGIC_FRAME1);
                textures.add(TexturePack.TOWER_BOW_MAGIC_FRAME2);
                textures.add(TexturePack.TOWER_BOW_MAGIC_FRAME3);
                textures.add(TexturePack.TOWER_BOW_MAGIC_FRAME4);

                damage = 20;
                range = 20;
                attackCooldown = 5f * timeFactor;
                sound = AudioPack.TOWER_BOW_MAGIC;

                areaOfEffectComponent = new AreaOfEffectComponent();

                break;

            case FIRE_LIGHTNING:
                textures.add(TexturePack.TOWER_FIRE_LIGHTNING_FRAME1);
                textures.add(TexturePack.TOWER_FIRE_LIGHTNING_FRAME2);
                textures.add(TexturePack.TOWER_FIRE_LIGHTNING_FRAME3);
                textures.add(TexturePack.TOWER_FIRE_LIGHTNING_FRAME5);
                textures.add(TexturePack.TOWER_FIRE_LIGHTNING_FRAME2);
                textures.add(TexturePack.TOWER_FIRE_LIGHTNING_FRAME4);
                textures.add(TexturePack.TOWER_FIRE_LIGHTNING_FRAME1);

                areaOfEffectComponent = new AreaOfEffectComponent();

                damage = 50;
                range = 3;
                attackCooldown = 3f * timeFactor;
                sound = AudioPack.TOWER_FIRE_LIGHTNING;

                break;

        }

        // Create the components for the tower
        TowerComponent towerComponent = new TowerComponent(damage, range, attackCooldown, towerType);
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
        if (areaOfEffectComponent != null) {
            tower.addComponent(AreaOfEffectComponent.class, areaOfEffectComponent);
        }

        return tower;
    }
}
