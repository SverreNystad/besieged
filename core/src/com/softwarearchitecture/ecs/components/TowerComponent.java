package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class TowerComponent implements Serializable {

    private int damage;
    private int range;

    public TowerComponent(int damage, int range) {
        if (damage >= 1 && range >= 1) {
            this.damage = damage;
            this.range = range;
        }
        else {
            this.damage = 1;
            this.range = 1;
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public int getRange() {
        return this.range;
    }

    public void setDamage(int damage) {
        if (damage >= 1) {
            this.damage = damage;
        }
        else {
            this.damage = 1;
        }
    }

    public void setRange(int range) {
        if (range >= 1) {
            this.range = range;
        }
        else {
            this.range = 1;
        }
    }
}
