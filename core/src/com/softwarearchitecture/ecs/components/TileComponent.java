package com.softwarearchitecture.ecs.components;

import com.softwarearchitecture.game_server.Tile;

public class TileComponent {
    private Tile tile;

    public TileComponent(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }
}
