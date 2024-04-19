package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class CostComponent implements Serializable {
    // TODO: Change all fields to public and remove getters and setters

    private int cost;

    public CostComponent(int cost) {
        if (cost >= 0) {
            this.cost = cost;
        }
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        if (cost >= 0) {
            this.cost = cost;
        }
    }   
}
