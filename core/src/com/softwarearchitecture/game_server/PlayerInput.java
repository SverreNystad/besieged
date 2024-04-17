package com.softwarearchitecture.game_server;

import java.util.UUID;

import com.softwarearchitecture.game_server.CardFactory.CardType;

public class PlayerInput {
    private UUID playerId;
    private int x;
    private int y;
    private CardType cardType;
    private String action;
    
    // Maybe it is better with a player input builder? Or just playerID, action type as enum, and serialized data?
    public PlayerInput(UUID playerId, CardType cardType, int x, int y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.cardType = cardType;
        this.action = "";
    }

    public PlayerInput(UUID playerId, String action) {
        this.playerId = playerId;
        this.action = action;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getAction() {
        return action;
    }
}

