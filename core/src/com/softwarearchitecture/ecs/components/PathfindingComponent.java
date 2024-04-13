package com.softwarearchitecture.ecs.components;

import java.io.Serializable;
import java.util.Queue;

import com.softwarearchitecture.math.Vector2;

public class PathfindingComponent implements Serializable {
    public Queue<Vector2> path;
    public Vector2 destination;  // You might want to add a destination for recalculating paths

    public PathfindingComponent(Queue<Vector2> path, Vector2 destination) {
        this.path = path;
        this.destination = destination;
    }

    public Queue<Vector2> getPath() {
        return path;
    }

    public Vector2 getDestination() {
        return destination;
    }

    public void setPath(Queue<Vector2> path) {
        this.path = path;
    }

    public void setDestination(Vector2 destination) {
        this.destination = destination;
    }
}
