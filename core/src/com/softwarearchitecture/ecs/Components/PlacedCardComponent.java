package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class PlacedCardComponent implements Serializable {
    public int x;
    public int y;

    public PlacedCardComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }
}