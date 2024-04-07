package com.softwarearchitecture.game_server;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class MapTest {

    private Map abyssMap;

    @Before
    public void setUp() {
        abyssMap = MapFactory.createMap("Abyss");
    }

    @Test
    public void testAbyssMapCreation() {

        assertNotNull(abyssMap);

        // Check if there is more than one tile in the map
        assertTrue(abyssMap.getMapLayout().length > 1);
        assertTrue(abyssMap.getMapLayout()[0].length > 1);

        for (int x = 0; x < abyssMap.getMapLayout().length; x++) {
            for (int y = 0; y < abyssMap.getMapLayout()[x].length; y++) {
                Tile tile = abyssMap.getMapLayout()[x][y];
                assertEquals(x, tile.getX());
                assertEquals(y, tile.getY());
            }
        }
    }
}