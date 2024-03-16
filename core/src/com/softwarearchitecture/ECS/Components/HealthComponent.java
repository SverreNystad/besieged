package com.softwarearchitecture.ecs.components;

public class HealthComponent {
    private int health;

    public HealthComponent(int health) {
        if (health >= 0) {
            this.health = health;
        }
        else {
            this.health = 0;
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health >= 0) {
            this.health = health;
        }
        else {
            this.health = 0;
        }
    }
}
