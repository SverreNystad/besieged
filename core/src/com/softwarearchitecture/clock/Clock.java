package com.softwarearchitecture.clock;

public class Clock {
    private long deltaTimeTime;

    // singleton instance
    private static ThreadLocal<Clock> instance = null;

    public static Clock getInstance() {
        if (instance == null) {
            instance = ThreadLocal.withInitial(Clock::new);
        }
        return instance.get();
    }

    private Clock() {
        deltaTimeTime = System.currentTimeMillis();
    }

    public float getDeltaTime() {
        return (float) (System.currentTimeMillis() - deltaTimeTime) / 1_000f;
    }

    public float getAndResetDeltaTime() {
        float deltaTime = (float)(System.currentTimeMillis() - deltaTimeTime) / 1_000f;
        deltaTimeTime = System.currentTimeMillis();
        return deltaTime;
    }
}
