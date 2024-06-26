package com.softwarearchitecture.game_server;

import java.io.Serializable;

import com.softwarearchitecture.ecs.Entity;

public class Tile implements Serializable {
    private int x;
    private int y;
    private TileType type;
    private Entity card;
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

    public Entity getCard() {
        return this.card;
    }

    public String getCardOrTowerTexturePath() {
        return cardOrTowerTexturePath;
    }

    // Setters
    public void setTower(Entity tower) {
        this.tower = tower;
    }

    public void setCard(Entity card) {
        this.card = card;
    }

    public void setCardOrTowerTexturePath(String cardOrTowerTexturePath) {
        this.cardOrTowerTexturePath = cardOrTowerTexturePath;
    }

    // Helper methods
    public boolean hasTower() {
        return tower != null;
    }

    public boolean isBuildable() {
        return type == TileType.PLACEABLE;
    }

   
    public boolean hasCard() {
        return this.card != null;
    }
    
    public void removeCard() {
        this.card = null;
    }

    public TileType getTileType() {
        return type;
    }
}
