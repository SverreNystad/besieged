package com.softwarearchitecture.game_server.cards.elemental_cards;

import java.io.Serializable;

import com.softwarearchitecture.ecs.Card;
import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;

public class FlameCard extends Card implements Serializable {

    public FlameCard(PlacedCardComponent placedCardComponent, CostComponent costComponent,
            SpriteComponent spriteComponent) {
        super(placedCardComponent, costComponent, spriteComponent);
        //TODO Auto-generated constructor stub
    }

}
