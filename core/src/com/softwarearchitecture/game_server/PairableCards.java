package com.softwarearchitecture.game_server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.softwarearchitecture.game_server.CardFactory.CardType;

public class PairableCards {

    public static List<CardType> getPairable(CardType cardType) {
        List<CardType> pairableCards = new ArrayList<CardType>();
        switch (cardType) {

            case MAGIC:
                pairableCards.add(CardType.FIRE);
                pairableCards.add(CardType.ICE);
                pairableCards.add(CardType.LIGHTNING);
                pairableCards.add(CardType.MAGIC);
                break;
            case BOW:
                pairableCards.add(CardType.FIRE);
                pairableCards.add(CardType.BOW);
                pairableCards.add(CardType.TECHNOLOGY);
                break;
            default:
                break;

        }
        return pairableCards;
    }

    public static boolean isPairable(CardType cardType1, CardType cardType2) {
        List<CardType> pairableCards = getPairable(cardType1);
        return pairableCards.contains(cardType2);
    }

    // TODO: make is placable method, that checks if the grid you want to place the
    // card on is empty
    // if not empty, check if the card is pairable with the card on the grid

    public enum TowerType {
        BOW,
        FIRE_BOW,
        SHARP_SHOOTER,
        MAGIC,
        ICE_MAGIC,
        FIRE_MAGIC,
        TOR
    }

    public static Optional<TowerType> getTower(CardType card1, CardType card2) {
        if (!isPairable(card1, card2)) {
            return Optional.empty();
        }
        return Optional.ofNullable(determineTowerType(card1, card2));
    }

    private static TowerType determineTowerType(CardType card1, CardType card2) {
        switch (card1) {
            case MAGIC:
                return handleMagicCardType(card2);
            case BOW:
                return handleBowCardType(card2);
            default:
                return handleDefaultCardType(card1, card2);
        }
    }

    private static TowerType handleMagicCardType(CardType card2) {
        switch (card2) {
            case FIRE:
                return TowerType.FIRE_MAGIC;
            case ICE:
                return TowerType.ICE_MAGIC;
            case LIGHTNING:
                return TowerType.TOR;
            case MAGIC:
                return TowerType.MAGIC;
            default:
                return null;
        }
    }

    private static TowerType handleBowCardType(CardType card2) {
        switch (card2) {
            case FIRE:
                return TowerType.FIRE_BOW;
            case TECHNOLOGY:
                return TowerType.SHARP_SHOOTER;
            case BOW:
                return TowerType.BOW;
            default:
                return null;
        }
    }

    private static TowerType handleDefaultCardType(CardType card1, CardType card2) {
        if (card2 == CardType.BOW || card2 == CardType.MAGIC) {
            return getTower(card2, card1).orElse(null);
        }
        return null;
    }

}
