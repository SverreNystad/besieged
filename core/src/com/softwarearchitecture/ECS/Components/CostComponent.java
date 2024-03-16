package com.softwarearchitecture.ecs.components;

public class CostComponent {
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
