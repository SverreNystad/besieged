package com.softwarearchitecture.game_server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MapTest {

    private Map abyssMap;
    private Map TestMap;

    @Before
    public void setUp() {
        abyssMap = MapFactory.createMap("abyss");
        TestMap = MapFactory.createMap("test");
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

    @Test
    public void testGetPath() {
        List<Tile> abyssPath = abyssMap.getPath();
        List<Tile> testPath = TestMap.getPath();
        assertTrue(abyssPath.size() == 22);
        assertTrue(testPath.size() == 6);

    }
}