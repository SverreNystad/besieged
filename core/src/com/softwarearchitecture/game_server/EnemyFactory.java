package com.softwarearchitecture.game_server;

import java.util.ArrayList;
import java.util.List;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SoundComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;
import com.softwarearchitecture.math.Vector2;

public class EnemyFactory {

    public enum EnemyType {
        NORDIC_ANT,
        WOLF

    }

    public static Entity createEnemy(EnemyType enemyType) throws IllegalArgumentException {
        List<String> textures = new ArrayList<String>();
        Vector2 size = new Vector2(1, 1);
        Vector2 position = new Vector2(0, 0); // TODO: Set position to the first path tile
        Vector2 velocity = new Vector2(0, 0);
        int damage = 1;
        int health = 0;
        String sound = "soundPath";
        int money = 0;

        switch (enemyType) {
            case NORDIC_ANT:
                textures.add(TexturePack.BUTTON_PLACEHOLDER);// TODO: Add the correct textures to texturePack
                textures.add(TexturePack.BUTTON_PLACEHOLDER);
                damage = 1;
                size.set(0.02f, 0.02f);
                velocity.set(0.01f, 0.01f);
                health = 10;
                sound = "soundPath"; // TODO: Add the correct sound path
                money = 1;

                break;
            case WOLF:
                textures.add(TexturePack.BUTTON_PLACEHOLDER);
                textures.add(TexturePack.BUTTON_PLACEHOLDER);
                damage = 2;
                size.set(0.03f, 0.03f);
                velocity.set(0.02f, 0.02f);
                health = 100;
                sound = "soundPath"; // TODO: Add the correct sound path
                money = 5;

                break;

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }
        EnemyComponent enemyComponent = new EnemyComponent(damage);
        PositionComponent positionComponent = new PositionComponent(position);
        AnimationComponent animationComponent = new AnimationComponent(textures);
        SpriteComponent spriteComponent = new SpriteComponent(textures.get(0), size, 0);
        HealthComponent healthComponent = new HealthComponent(health);
        SoundComponent soundComponent = new SoundComponent(sound);
        VelocityComponent velocityComponent = new VelocityComponent(velocity.x, velocity.y);
        CostComponent costComponent = new CostComponent(money);
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

        return enemyEntity;

    }

}
