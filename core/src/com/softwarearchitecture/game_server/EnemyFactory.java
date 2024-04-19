package com.softwarearchitecture.game_server;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.MoneyComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.math.Vector2;

public class EnemyFactory {

    public enum EnemyType {
        NORDIC_ANT, WOLF, VIKING_SWORD_SHIELD, VIKING_SWORD, VIKING_AXE, VIKING_SPEAR, TROLL

    }

    public static Entity createEnemy(EnemyType enemyType, List<Tile> enemyPath, Vector2 tileSize)
            throws IllegalArgumentException {
        List<String> textures = new ArrayList<String>();
        float tileHeight = tileSize.y;
        float tileWidth = tileSize.x;
        Vector2 size = new Vector2(1, 1);
        Vector2 position = new Vector2((float) enemyPath.get(0).getX() * tileWidth,
                (float) enemyPath.get(0).getY() * tileHeight + tileHeight / 4);
        float velocity = 0f;
        int damage = 1;
        int maxHealth = 0;
        String sound = AudioPack.PLACING_CARD; //TIDO: Add default sound
        int money = 0;

        switch (enemyType) {
            case NORDIC_ANT:
                textures.add(TexturePack.ENEMY_ANT_FRAME1);
                textures.add(TexturePack.ENEMY_ANT_FRAME2);
                textures.add(TexturePack.ENEMY_ANT_FRAME3);
                textures.add(TexturePack.ENEMY_ANT_FRAME4);

                damage = 1;
                size.set(0.03f, 0.03f);
                velocity = 0.02f;
                maxHealth = 10;
                sound = AudioPack.PLACING_CARD; // TODO: Add the correct sound path
                money = 1;

                break;
            case WOLF:
                textures.add(TexturePack.ENEMY_WOLF_FRAME1);
                textures.add(TexturePack.ENEMY_WOLF_FRAME2);
                textures.add(TexturePack.ENEMY_WOLF_FRAME3);
                textures.add(TexturePack.ENEMY_WOLF_FRAME4);
                textures.add(TexturePack.ENEMY_WOLF_FRAME5);

                damage = 2;
                size.set(0.075f, 0.075f);
                velocity = 0.05f;
                maxHealth = 100;
                sound = AudioPack.PLACING_CARD; // TODO: Add the correct sound path
                money = 30;

                break;

            case VIKING_SPEAR:
                textures.add(TexturePack.ENEMY_VIKING_SPEAR_FRAME1);
                textures.add(TexturePack.ENEMY_VIKING_SPEAR_FRAME2);
                textures.add(TexturePack.ENEMY_VIKING_SPEAR_FRAME3);
                textures.add(TexturePack.ENEMY_VIKING_SPEAR_FRAME4);
                damage = 3;
                size.set(0.065f, 0.093f);
                velocity = 0.04f;
                maxHealth = 100;
                sound = AudioPack.PLACING_CARD; // TODO: Add the correct sound path
                money = 100;

                break;

            case VIKING_SWORD:
                textures.add(TexturePack.ENEMY_VIKING_SWORD_FRAME1);
                textures.add(TexturePack.ENEMY_VIKING_SWORD_FRAME2);
                textures.add(TexturePack.ENEMY_VIKING_SWORD_FRAME3);
                textures.add(TexturePack.ENEMY_VIKING_SWORD_FRAME4);
                damage = 4;
                size.set(0.05f, 0.08f);
                velocity = 0.03f;
                maxHealth = 100;
                sound = AudioPack.PLACING_CARD; // TODO: Add the correct sound path
                money = 150;

                break;

            case VIKING_SWORD_SHIELD:
                textures.add(TexturePack.ENEMY_VIKING_SWORD_SHIELD_FRAME1);
                textures.add(TexturePack.ENEMY_VIKING_SWORD_SHIELD_FRAME2);
                textures.add(TexturePack.ENEMY_VIKING_SWORD_SHIELD_FRAME3);
                textures.add(TexturePack.ENEMY_VIKING_SWORD_SHIELD_FRAME4);
                damage = 5;
                size.set(0.05f, 0.08f);
                velocity = 0.03f;
                maxHealth = 200;
                sound = AudioPack.PLACING_CARD; // TODO: Add the correct sound path
                money = 200;

                break;

            case VIKING_AXE:
                textures.add(TexturePack.ENEMY_VIKING_AXE_FRAME1);
                textures.add(TexturePack.ENEMY_VIKING_AXE_FRAME2);
                textures.add(TexturePack.ENEMY_VIKING_AXE_FRAME3);
                textures.add(TexturePack.ENEMY_VIKING_AXE_FRAME4);
                damage = 4;
                size.set(0.05f, 0.08f);
                velocity = 0.023f;
                maxHealth = 300;
                sound = AudioPack.PLACING_CARD; // TODO: Add the correct sound path
                money = 100;

                break;

            case TROLL:
                textures.add(TexturePack.ENEMY_TROLL_FRAME1);
                textures.add(TexturePack.ENEMY_TROLL_FRAME1);
                textures.add(TexturePack.ENEMY_TROLL_FRAME2);
                textures.add(TexturePack.ENEMY_TROLL_FRAME2);
                textures.add(TexturePack.ENEMY_TROLL_FRAME3);
                textures.add(TexturePack.ENEMY_TROLL_FRAME3);

                damage = 5;
                size.set(0.05f, 0.15f);
                velocity = 0.003f;
                maxHealth = 5000;
                sound = AudioPack.PLACING_CARD; // TODO: Add the correct sound path
                money = 1000;

                break;

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }
        EnemyComponent enemyComponent = new EnemyComponent(damage);
        PositionComponent positionComponent = new PositionComponent(position, 5);
        AnimationComponent animationComponent = new AnimationComponent(textures);
        SpriteComponent spriteComponent = new SpriteComponent(textures.get(0), size);
        HealthComponent healthComponent = new HealthComponent(maxHealth);
        SoundComponent soundComponent = new SoundComponent(sound, false, false);
        VelocityComponent velocityComponent = new VelocityComponent(velocity);
        PathfindingComponent PathfindingComponent = new PathfindingComponent(enemyPath);
        MoneyComponent moneyComponent = new MoneyComponent(money);
        // TODO: Add target component if necessary

        Entity enemyEntity = new Entity();
        enemyEntity.addComponent(EnemyComponent.class, enemyComponent);
        enemyEntity.addComponent(PositionComponent.class, positionComponent);
        enemyEntity.addComponent(AnimationComponent.class, animationComponent);
        enemyEntity.addComponent(SpriteComponent.class, spriteComponent);
        enemyEntity.addComponent(HealthComponent.class, healthComponent);
        enemyEntity.addComponent(SoundComponent.class, soundComponent);
        enemyEntity.addComponent(VelocityComponent.class, velocityComponent);
        enemyEntity.addComponent(PathfindingComponent.class, PathfindingComponent);
        enemyEntity.addComponent(MoneyComponent.class, moneyComponent);

        return enemyEntity;

    }

}
