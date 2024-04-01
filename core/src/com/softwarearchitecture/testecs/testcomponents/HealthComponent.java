package com.softwarearchitecture.testecs.testcomponents;

import java.io.Serializable;

public class HealthComponent implements Serializable {
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
