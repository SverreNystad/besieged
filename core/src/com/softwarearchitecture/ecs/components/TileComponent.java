package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.game_server.Tile;

public class TileComponent implements Serializable {
    // TODO: Change all fields to public and remove getters and setters

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
