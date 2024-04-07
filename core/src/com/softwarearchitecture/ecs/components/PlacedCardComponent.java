package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.game_server.CardFactory;
import com.softwarearchitecture.game_server.CardFactory.CardType;

public class PlacedCardComponent implements Serializable {
    public int x;
    public int y;
    public CardType cardType;

    public PlacedCardComponent(int x, int y, CardType cardType) {
        this.x = x;
        this.y = y;
        this.cardType = cardType;
    }
}