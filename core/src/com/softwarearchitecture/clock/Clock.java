package com.softwarearchitecture.clock;

public class Clock {
    private long currentTime;
    private float animationTime = 150f;
    private float movementTime = 100f;
    private float attackTime = 300f;

    // singleton instance
    private static Clock instance = null;

    public static Clock getInstance() {
        if (instance == null) {

            instance = new Clock();
        }
        return instance;
    }

    private Clock() {
        currentTime = System.currentTimeMillis();

    }

    public boolean isTimeToAnimate() {
        boolean timeToAnimate = System.currentTimeMillis() - currentTime >= animationTime;
        if (timeToAnimate) {
            currentTime = System.currentTimeMillis();
        }
        return timeToAnimate;
    }

    public boolean isTimeToMove() {

        boolean timeToMove = System.currentTimeMillis() - currentTime >= movementTime;
        if (timeToMove) {
            currentTime = System.currentTimeMillis();
        }
        return timeToMove;

    }

    public boolean isTimeToAttack(float time) {
        boolean timeToMove = System.currentTimeMillis() - currentTime >= attackTime;
        if (timeToMove) {
            currentTime = System.currentTimeMillis();
        }
        return timeToMove;
    }
}
