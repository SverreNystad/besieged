package com.softwarearchitecture.game_server;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.PathfindingComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;
import com.softwarearchitecture.game_client.TexturePack;
import com.softwarearchitecture.math.Vector2;

public class EnemyFactory {

    public enum EnemyType {
        NORDIC_ANT,
        WOLF

    }

    public static Entity createEnemy(EnemyType enemyType, List<Tile> enemyPath, Vector2 tileSize) throws IllegalArgumentException {
        List<String> textures = new ArrayList<String>();
        float tileHeight = tileSize.y;
        float tileWidth = tileSize.x;
        Vector2 size = new Vector2(1, 1);
        Vector2 position = new Vector2((float) enemyPath.get(0).getX()*tileWidth, (float) enemyPath.get(0).getY()*tileHeight);
        Vector2 velocity = new Vector2(0, 0);
        int damage = 1;
        int health = 0;
        int maxHealth = 0;
        String sound = "soundPath";
        int money = 0;

        switch (enemyType) {
            case NORDIC_ANT:
                textures.add(TexturePack.ENEMY_ANT_FRAME1);
                textures.add(TexturePack.ENEMY_ANT_FRAME2);
                textures.add(TexturePack.ENEMY_ANT_FRAME3);
                textures.add(TexturePack.ENEMY_ANT_FRAME4);

                damage = 1;
                size.set(0.025f, 0.025f);
                velocity.set(0.01f, 0.01f);
                health = 10;
                maxHealth = 10;
                sound = "soundPath"; // TODO: Add the correct sound path
                money = 1;

                break;
            case WOLF:
                textures.add(TexturePack.ENEMY_FENRIR1);
                textures.add(TexturePack.ENEMY_FENRIR2);
                damage = 2;
                size.set(0.1f, 0.1f);
                velocity.set(0.02f, 0.02f);
                health = 100;
                maxHealth = 100;
                sound = "soundPath"; // TODO: Add the correct sound path
                money = 5;

                break;

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }
        EnemyComponent enemyComponent = new EnemyComponent(damage);
        PositionComponent positionComponent = new PositionComponent(position, 5);
        AnimationComponent animationComponent = new AnimationComponent(textures);
        SpriteComponent spriteComponent = new SpriteComponent(textures.get(0), size);
        HealthComponent healthComponent = new HealthComponent(health, maxHealth);
        SoundComponent soundComponent = new SoundComponent(sound);
        VelocityComponent velocityComponent = new VelocityComponent(velocity.x, velocity.y);
        CostComponent costComponent = new CostComponent(money);
        PathfindingComponent PathfindingComponent = new PathfindingComponent(enemyPath);
        // TODO: Add target component if necessary

        Entity enemyEntity = new Entity();
        enemyEntity.addComponent(EnemyComponent.class, enemyComponent);
        enemyEntity.addComponent(PositionComponent.class, positionComponent);
        enemyEntity.addComponent(AnimationComponent.class, animationComponent);
        enemyEntity.addComponent(SpriteComponent.class, spriteComponent);
        enemyEntity.addComponent(HealthComponent.class, healthComponent);
        enemyEntity.addComponent(SoundComponent.class, soundComponent);
        enemyEntity.addComponent(VelocityComponent.class, velocityComponent);
        enemyEntity.addComponent(CostComponent.class, costComponent);
        enemyEntity.addComponent(PathfindingComponent.class, PathfindingComponent);

        return enemyEntity;

    }

}
