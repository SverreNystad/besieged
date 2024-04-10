package com.softwarearchitecture.game_server;

import com.softwarearchitecture.ecs.Entity;

public class Tile {
    private int x;
    private int y;
    private TileType type;
    private Entity tower;
    private String cardOrTowerTexturePath;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.tower = null;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileType getType() {
        return type;
    }

    public Entity getTower() {
        return tower;
    }

    // Setters
    public void setTower(Entity tower) {
        this.tower = tower;
    }

    public boolean hasTower() {
        return tower != null;
    }

    public boolean isBuildable() {
        return type == TileType.PLACEABLE;
    }

    public String getCardOrTowerTexturePath() {
        return cardOrTowerTexturePath;
    }

    public void setCardOrTowerTexturePath(String cardOrTowerTexturePath) {
        this.cardOrTowerTexturePath = cardOrTowerTexturePath;
    }
}
