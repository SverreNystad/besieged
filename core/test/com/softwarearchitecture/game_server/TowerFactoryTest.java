package com.softwarearchitecture.game_server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.TowerComponent;
import com.softwarearchitecture.game_server.CardFactory.CardType;
import com.softwarearchitecture.math.Vector2;

public class TowerFactoryTest {
    @Test
    public void testValidTowerCreation() {
        // Assuming Vector2 is a class that holds coordinates or dimensions and has a
        // constructor
        Vector2 position = new Vector2(0, 0);
        Entity tower = TowerFactory.createTower(CardType.MAGIC, CardType.FIRE, position);

        assertNotNull("Tower should not be null", tower);
        TowerComponent tc = tower.getComponent(TowerComponent.class)
                .orElseThrow(() -> new AssertionError("TowerComponent should be present"));
        assertEquals("Damage should match expected for FIRE_MAGIC tower", 5, tc.getDamage());
        assertEquals("Range should match expected for FIRE_MAGIC tower", 3, tc.getRange());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCardCombination() {
        Vector2 position = new Vector2(0, 0);

        TowerFactory.createTower(CardType.MAGIC, CardType.TECHNOLOGY, position);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPairableCards() {
        Vector2 position = new Vector2(0, 0);
        TowerFactory.createTower(CardType.BOW, CardType.MAGIC, position); // Assuming this combination is invalid
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputHandling() {
        Vector2 position = new Vector2(0, 0);
        TowerFactory.createTower(null, CardType.FIRE, position);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSecondNullInputHandling() {
        Vector2 position = new Vector2(0, 0);
        TowerFactory.createTower(CardType.FIRE, null, position);
    }

}
