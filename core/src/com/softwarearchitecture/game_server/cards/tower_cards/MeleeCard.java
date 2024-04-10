package com.softwarearchitecture.game_server.cards.tower_cards;

import java.io.Serializable;

import com.softwarearchitecture.ecs.Card;
import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;

public class MeleeCard extends Card implements Serializable {

    public MeleeCard(PlacedCardComponent placedCardComponent, CostComponent costComponent,
            SpriteComponent spriteComponent) {
        super(placedCardComponent, costComponent, spriteComponent);
        //TODO Auto-generated constructor stub
    }

}