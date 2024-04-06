package com.softwarearchitecture.game_server;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class MapTest {

    @Test
    public void testAbyssMapCreation() {
        Map abyssMap = MapFactory.createMap("Abyss", 10, 10); 

        assertNotNull(abyssMap);

        // Check if there is more than one tile in the map
        assertTrue(abyssMap.getMapLayout().length > 1);
        assertTrue(abyssMap.getMapLayout()[0].length > 1);

        // Check if the first tile has coordinates (0, 0) 
        assertEquals(0, abyssMap.getMapLayout()[0][0].getX());
        assertEquals(0, abyssMap.getMapLayout()[0][0].getY());

        // Check if the last tile has coordinates (9, 9)
        assertEquals(9, abyssMap.getMapLayout()[9][9].getX());
        assertEquals(9, abyssMap.getMapLayout()[9][9].getY());

        
        
    }
}
