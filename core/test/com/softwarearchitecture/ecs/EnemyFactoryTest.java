package com.softwarearchitecture.ecs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;

import com.softwarearchitecture.game_server.EnemyFactory;

public class EnemyFactoryTest {

    @Test
    public void testCreateEnemyNotNull() {
        assertNotNull("Entity should not be null", EnemyFactory.createEnemy(EnemyFactory.EnemyType.NORDIC_ANT));
        assertNotNull("Entity should not be null", EnemyFactory.createEnemy(EnemyFactory.EnemyType.WOLF));
    }

    @Test
    public void testEnemyAttributes() {
        Entity nordicAnt = EnemyFactory.createEnemy(EnemyFactory.EnemyType.NORDIC_ANT);
        EnemyComponent nordicAntComponentOpt = nordicAnt.getComponent(EnemyComponent.class);

        HealthComponent nordicAntHealthOpt = nordicAnt.getComponent(HealthComponent.class);

        assertEquals("Health should be 10 for NORDIC_ANT", 10, nordicAntHealthOpt.getHealth());
        assertEquals("Damage should be 1 for NORDIC_ANT", 1, nordicAntComponentOpt.getDamage());

        Entity wolf = EnemyFactory.createEnemy(EnemyFactory.EnemyType.WOLF);
        EnemyComponent wolfComponentOpt = wolf.getComponent(EnemyComponent.class);

        HealthComponent wolfHealthOpt = wolf.getComponent(HealthComponent.class);

        assertEquals("Health should be 100 for WOLF", 100, wolfHealthOpt.getHealth());
        assertEquals("Damage should be 2 for WOLF", 2, wolfComponentOpt.getDamage());
    }

    @Test
    public void testComponentAssignment() {
        Entity wolf = EnemyFactory.createEnemy(EnemyFactory.EnemyType.WOLF);
        assertNotNull("PositionComponent should be assigned", wolf.getComponent(PositionComponent.class));
        assertNotNull("AnimationComponent should be assigned", wolf.getComponent(AnimationComponent.class));
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidEnemyType() {
        EnemyFactory.createEnemy(null); // Should throw IllegalArgumentException due to invalid enemy type
    }
}