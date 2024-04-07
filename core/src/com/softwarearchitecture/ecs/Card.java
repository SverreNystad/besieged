package com.softwarearchitecture.ecs;

import com.softwarearchitecture.ecs.components.CostComponent;
import com.softwarearchitecture.ecs.components.PlacedCardComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;

/**
 * The {@code Card} interface serves as a marker interface for all card types
 * in the game. 
 */
public abstract class Card {
    
    private PlacedCardComponent placedCardComponent; // Position of the card on the board
    private CostComponent costComponent; 
    private SpriteComponent spriteComponent;

    public Card(PlacedCardComponent placedCardComponent, CostComponent costComponent, SpriteComponent spriteComponent) {
        this.placedCardComponent = placedCardComponent;
        this.costComponent = costComponent;
        this.spriteComponent = spriteComponent;
    }
}
