package com.softwarearchitecture.ecs.components;

public class DamageComponent {
    private int damage;

    public DamageComponent(int damage) {
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
