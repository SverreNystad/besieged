package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class MoneyComponent implements Serializable {
    public int amount;

    public MoneyComponent(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
