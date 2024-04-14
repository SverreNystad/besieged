package com.softwarearchitecture.ecs.components;

import java.io.Serializable;
import java.util.List;

import com.softwarearchitecture.game_server.Tile;

public class PathfindingComponent implements Serializable {
    public List<Tile> path;
    public Tile targetTile;

    public PathfindingComponent(List<Tile> path) {
        this.path = path;
        this.targetTile = path.get(0);
    }
}
