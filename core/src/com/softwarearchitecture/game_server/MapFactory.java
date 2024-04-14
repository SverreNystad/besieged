package com.softwarearchitecture.game_server;

import com.softwarearchitecture.game_client.TexturePack;

public class MapFactory {
    public static Map createMap(String mapType) {
        switch (mapType) {
            case "Abyss":
                String mapString = "BLOCKED_WATER, BLOCKED_WATER, PLACEABLE, PLACEABLE, PLACEABLE, START, PLACEABLE, BLOCKED_TREE;"
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
                        "PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, PATH, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PLACEABLE, PATH, PATH, PLACEABLE, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PATH, PATH, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE, PLACEABLE, PLACEABLE, BLOCKED_WATER;" +
                        "PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE, PLACEABLE, BLOCKED_WATER, BLOCKED_WATER;" +
                        "PLACEABLE, PLACEABLE, END, PLACEABLE, BLOCKED_WATER, BLOCKED_WATER, BLOCKED_WATER, BLOCKED_WATER;";
                String backgroundImage = TexturePack.BACKGROUND_ABYSS;
                return new Map(mapString, backgroundImage);
            case "TestMap":
                String mapString2 = "BLOCKED_WATER, START, PATH, PLACEABLE, BLOCKED_TREE;" +
                        "PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "PLACEABLE, PLACEABLE, PATH, PLACEABLE, PLACEABLE;" +
                        "BLOCKED_TREE, PLACEABLE, PATH, END, BLOCKED_ROCK;";
                String backString = TexturePack.BACKGROUND_GRIFFIN;
                return new Map(mapString2, backString);
            default:
                throw new IllegalArgumentException("Invalid map type: " + mapType);
        }
    }
}
