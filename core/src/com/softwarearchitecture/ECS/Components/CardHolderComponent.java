package com.softwarearchitecture.ecs.components;

import java.util.List;

import com.softwarearchitecture.ecs.Card;

import java.util.ArrayList;

public class CardHolderComponent {
    public List<Card> cardList;

    public CardHolderComponent(List<Card> cardList) {
        this.cardList = new ArrayList<>(cardList);
    }
}
