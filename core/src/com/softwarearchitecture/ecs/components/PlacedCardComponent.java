package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.game_server.CardFactory.CardType;

public class PlacedCardComponent implements Serializable {
    public CardType cardType;

    public PlacedCardComponent(CardType cardType) {
        this.cardType = cardType;
    }
}