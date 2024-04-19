package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.game_server.Tile;

public class TileComponent implements Serializable {

    private Tile tile;

    public TileComponent(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
