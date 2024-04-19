package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class HealthComponent implements Serializable {
    // TODO: Change all fields to public and remove getters and setters

    private int health;
    private int maxHealth;

    public HealthComponent(int maxHealth) {
        if (maxHealth >= 0) {
            this.maxHealth = maxHealth;
        }
        else {
            this.maxHealth = 1;
        }
        this.health = maxHealth;
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
