package com.softwarearchitecture.game_server;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.softwarearchitecture.game_server.CardFactory.CardType;

public class PairableCards {

    private static final Map<Pair, TowerType> pairableTowers = new HashMap<>();

    static {
        pairableTowers.put(new Pair(CardType.MAGIC, CardType.FIRE), TowerType.FIRE_MAGIC);
        pairableTowers.put(new Pair(CardType.MAGIC, CardType.LIGHTNING), TowerType.TOR);
        pairableTowers.put(new Pair(CardType.MAGIC, CardType.MAGIC), TowerType.MAGIC);
        pairableTowers.put(new Pair(CardType.BOW, CardType.FIRE), TowerType.FIRE_BOW);
        pairableTowers.put(new Pair(CardType.BOW, CardType.TECHNOLOGY), TowerType.SHARP_SHOOTER);
        pairableTowers.put(new Pair(CardType.BOW, CardType.BOW), TowerType.BOW);
        pairableTowers.put(new Pair(CardType.BOW, CardType.LIGHTNING), TowerType.BOW_LIGHTING);
        pairableTowers.put(new Pair(CardType.MAGIC, CardType.TECHNOLOGY), TowerType.MAGIC_TECH);
        pairableTowers.put(new Pair(CardType.TECHNOLOGY, CardType.TECHNOLOGY), TowerType.MORTAR);
        pairableTowers.put(new Pair(CardType.FIRE, CardType.FIRE), TowerType.INFERNO);
        pairableTowers.put(new Pair(CardType.FIRE, CardType.TECHNOLOGY), TowerType.FURNACE);
        pairableTowers.put(new Pair(CardType.TECHNOLOGY, CardType.LIGHTNING), TowerType.TESLA);
        pairableTowers.put(new Pair(CardType.LIGHTNING, CardType.LIGHTNING), TowerType.THUNDERBOLT);
        pairableTowers.put(new Pair(CardType.BOW, CardType.MAGIC), TowerType.BOW_MAGIC);
        pairableTowers.put(new Pair(CardType.FIRE, CardType.LIGHTNING), TowerType.FIRE_LIGHTNING);

    }

    public static boolean isPairable(CardType card1, CardType card2) {
        return pairableTowers.containsKey(new Pair(card1, card2)) || pairableTowers.containsKey(new Pair(card2, card1));
    }

    public static Optional<TowerType> getTower(CardType card1, CardType card2) {
        TowerType tower = pairableTowers.get(new Pair(card1, card2));
        if (tower == null) {
            tower = pairableTowers.get(new Pair(card2, card1));
        }
        return Optional.ofNullable(tower);
    }

    public static class Pair {
        private CardType first;
        private CardType second;

        public Pair(CardType first, CardType second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Pair pair = (Pair) o;
            return (first == pair.first && second == pair.second);
        }

        @Override
        public int hashCode() {
            return 31 * first.hashCode() + second.hashCode();
        }
    }

    public enum TowerType {
        BOW,
        FIRE_BOW,
        SHARP_SHOOTER,
        MAGIC,
        FIRE_MAGIC,
        TOR, BOW_LIGHTING, MAGIC_TECH, MORTAR, INFERNO, FURNACE, TESLA, THUNDERBOLT, BOW_MAGIC, FIRE_LIGHTNING
    }
}
