package com.softwarearchitecture.testecs.testcomponents;

import java.util.List;

import com.softwarearchitecture.ecs.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class CardHolderComponent implements Serializable {
    public List<Card> cardList;

    public CardHolderComponent(List<Card> cardList) {
        this.cardList = new ArrayList<>(cardList);
    }
}
