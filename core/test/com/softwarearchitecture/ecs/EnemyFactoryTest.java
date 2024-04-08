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
        Optional<EnemyComponent> nordicAntComponentOpt = nordicAnt.getComponent(EnemyComponent.class);
        assertTrue("EnemyComponent should be present", nordicAntComponentOpt.isPresent());
        EnemyComponent nordicAntComponent = nordicAntComponentOpt
                .orElseThrow(() -> new AssertionError("EnemyComponent not found"));

        Optional<HealthComponent> nordicAntHealthOpt = nordicAnt.getComponent(HealthComponent.class);
        assertTrue("HealthComponent should be present", nordicAntHealthOpt.isPresent());
        HealthComponent nordicAntHealth = nordicAntHealthOpt
                .orElseThrow(() -> new AssertionError("HealthComponent not found"));

        assertEquals("Health should be 10 for NORDIC_ANT", 10, nordicAntHealth.getHealth());
        assertEquals("Damage should be 1 for NORDIC_ANT", 1, nordicAntComponent.getDamage());

        Entity wolf = EnemyFactory.createEnemy(EnemyFactory.EnemyType.WOLF);
        Optional<EnemyComponent> wolfComponentOpt = wolf.getComponent(EnemyComponent.class);
        assertTrue("EnemyComponent should be present", wolfComponentOpt.isPresent());
        EnemyComponent wolfComponent = wolfComponentOpt
                .orElseThrow(() -> new AssertionError("EnemyComponent not found"));

        Optional<HealthComponent> wolfHealthOpt = wolf.getComponent(HealthComponent.class);
        assertTrue("HealthComponent should be present", wolfHealthOpt.isPresent());
        HealthComponent wolfHealth = wolfHealthOpt.orElseThrow(() -> new AssertionError("HealthComponent not found"));

        assertEquals("Health should be 100 for WOLF", 100, wolfHealth.getHealth());
        assertEquals("Damage should be 2 for WOLF", 2, wolfComponent.getDamage());
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