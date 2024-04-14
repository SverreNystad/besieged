package com.softwarearchitecture.game_server;

import java.util.HashMap;
import java.util.Set;

import com.softwarearchitecture.game_client.TexturePack;

public class MapFactory {

    /**
     * Creates a map based on the name
     * @param mapName
     * @return The map with the given name
     * @throws IllegalArgumentException 
     */
    public static Map createMap(String mapName) {
        HashMap<String, Map> maps = getMaps();
        Map map = maps.getOrDefault(mapName, null);
        if (map == null) {
            throw new IllegalArgumentException("[ERROR] Map type not found");
        }
        return map;
    }

    public static Set<String> getMapNames() {
        return MapFactory.getMaps().keySet();
    }

    private static HashMap<String, Map> getMaps() {
        HashMap<String, Map> maps = new HashMap<>();
        String abyssMapString = "BLOCKED_WATER, BLOCKED_WATER, PLACEABLE, PLACEABLE, PLACEABLE, START, PLACEABLE, BLOCKED_TREE;"
                        +
                        "BLOCKED_WATER, BLOCKED_WATER, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, BLOCKED_TREE;"
                        +
                        "BLOCKED_WATER, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "BLOCKED_WATER, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "BLOCKED_WATER, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, BLOCKED_ROCK, BLOCKED_ROCK, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, BLOCKED_ROCK, BLOCKED_ROCK, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, BLOCKED_ROCK, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PLACEABLE, PATH, PATH, PLACEABLE, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PATH, PATH, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, BLOCKED_WATER;" +
                        "PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE, PLACEABLE, BLOCKED_WATER, BLOCKED_WATER;" +
                        "PLACEABLE, PLACEABLE, END, PLACEABLE, BLOCKED_WATER, BLOCKED_WATER, BLOCKED_WATER, BLOCKED_WATER;";
        String backgroundImage = TexturePack.BACKGROUND_ABYSS;
        maps.put("abyss", new Map(abyssMapString, backgroundImage));

        String mapString2 = "BLOCKED_WATER, START, PATH, PLACEABLE, BLOCKED_TREE;" +
                        "PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "BLOCKED_TREE, PLACEABLE, PATH, END, BLOCKED_ROCK;";
        String backString = TexturePack.BACKGROUND_GRIFFIN;
        maps.put("test", new Map(mapString2, backString));
        return maps;
    }
}
