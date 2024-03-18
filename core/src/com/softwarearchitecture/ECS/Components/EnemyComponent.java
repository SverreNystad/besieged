package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class EnemyComponent implements Serializable {
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
