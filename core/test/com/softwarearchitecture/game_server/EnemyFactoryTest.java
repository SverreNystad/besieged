package com.softwarearchitecture.game_server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.AnimationComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.EnemyComponent;
import com.softwarearchitecture.ecs.components.HealthComponent;

import com.softwarearchitecture.math.Vector2;

public class EnemyFactoryTest {

    private List<Tile> tiles;
    private Vector2 tileSize;

    @Before
    public void setUp() {
        // Reset the entity ID counter before each test
        ECSManager.getInstance().clearLocalEntities();
        tiles = new ArrayList<Tile>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tiles.add(new Tile(i, j, TileType.PATH));
            }
        }
        
        tileSize = new Vector2(0, 0);

    }

    @Test
    public void testCreateEnemyNotNull() {
        assertNotNull("Entity should not be null", EnemyFactory.createEnemy(EnemyFactory.EnemyType.NORDIC_ANT, tiles, tileSize));
        assertNotNull("Entity should not be null", EnemyFactory.createEnemy(EnemyFactory.EnemyType.WOLF, tiles, tileSize));
    }

    @Test
    public void testEnemyAttributes() {
        Entity nordicAnt = EnemyFactory.createEnemy(EnemyFactory.EnemyType.NORDIC_ANT, tiles, tileSize);
        Optional<EnemyComponent> nordicAntComponentOpt = nordicAnt.getComponent(EnemyComponent.class);
        Optional<HealthComponent> nordicAntHealthOpt = nordicAnt.getComponent(HealthComponent.class);

        assertTrue("EnemyComponent should be present for NORDIC_ANT", nordicAntComponentOpt.isPresent());
        assertTrue("HealthComponent should be present for NORDIC_ANT", nordicAntHealthOpt.isPresent());

        assertEquals("Health should be 10 for NORDIC_ANT", 10, nordicAntHealthOpt.get().getHealth());
        assertEquals("Damage should be 1 for NORDIC_ANT", 1, nordicAntComponentOpt.get().getDamage());

        Entity wolf = EnemyFactory.createEnemy(EnemyFactory.EnemyType.WOLF, tiles, tileSize);
        Optional<EnemyComponent> wolfComponentOpt = wolf.getComponent(EnemyComponent.class);
        Optional<HealthComponent> wolfHealthOpt = wolf.getComponent(HealthComponent.class);

        assertTrue("EnemyComponent should be present for WOLF", wolfComponentOpt.isPresent());
        assertTrue("HealthComponent should be present for WOLF", wolfHealthOpt.isPresent());

        assertEquals("Health should be 100 for WOLF", 100, wolfHealthOpt.get().getHealth());
        assertEquals("Damage should be 2 for WOLF", 2, wolfComponentOpt.get().getDamage());
    }

    @Test
    public void testComponentAssignment() {
        Entity wolf = EnemyFactory.createEnemy(EnemyFactory.EnemyType.WOLF, tiles, tileSize);
        assertNotNull("PositionComponent should be assigned", wolf.getComponent(PositionComponent.class));
        assertNotNull("AnimationComponent should be assigned", wolf.getComponent(AnimationComponent.class));
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidEnemyType() {
        EnemyFactory.createEnemy(null, tiles, tileSize); // Should throw IllegalArgumentException due to invalid enemy type
    }
}