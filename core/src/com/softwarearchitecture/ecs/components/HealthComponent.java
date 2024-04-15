package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class HealthComponent implements Serializable {
    private int health;
    private int maxHealth;

    public HealthComponent(int health, int maxHealth) {
        if (health >= 0) {
            this.health = health;
        }
        else {
            this.health = 0;
        }
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        if (health >= 0) {
            this.health = health;
        }
        else {
            this.health = 0;
        }
    }

    public void setMaxHealth(int maxHealth) {
        if (maxHealth >= 0) {
            this.maxHealth = maxHealth;
        }
    }
}
