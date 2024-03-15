package com.softwarearchitecture.ecs.components;

import java.util.ArrayList;
import java.util.List;

public class TowerComponent {
    public enum DamageType {
        FIRE, WATER, LIGHTNING // Sample types
    }

    public List<DamageType> damageTypes;
    private int damage;

    public TowerComponent(List<DamageType> damageTypes, int damage) {
        this.damageTypes = new ArrayList<>(damageTypes);
        if (damage >= 1) {
            this.damage = damage;
        }
        else {
            this.damage = 1;
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        if (damage >= 1) {
            this.damage = damage;
        }
        else {
            this.damage = 1;
        }
    }
}
