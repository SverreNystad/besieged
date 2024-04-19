package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.game_server.CardFactory.CardType;

public class PlacedCardComponent implements Serializable {
    public CardType cardType;
    public boolean playSound = false;

    public PlacedCardComponent(CardType cardType) {
        this.cardType = cardType;
    }
}