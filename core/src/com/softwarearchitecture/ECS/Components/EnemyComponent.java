package com.softwarearchitecture.ecs.components;

public class EnemyComponent {
    public int damage;

    public EnemyComponent(int damage) {
        if (damage >= 1) {
            this.damage = damage;
        }
        else {
            this.damage = 1;
        }
    }
}
