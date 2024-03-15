package com.softwarearchitecture.ecs.components;

public class DamageComponent {
    private int damage;

    public DamageComponent(int damage) {
        if (damage >= 0) {
            this.damage = damage;
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        if (damage >= 0) {
            this.damage = damage;
        }
    }
}
