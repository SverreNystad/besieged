package com.softwarearchitecture.ecs.components;

public class TowerComponent {
    public enum DamageType {
        FIRE, WATER, LIGHTNING // Sample types
    }

    public DamageType damageType;
    private int damage;

    public TowerComponent(DamageType damageType, int damage) {
        this.damageType = damageType;
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
