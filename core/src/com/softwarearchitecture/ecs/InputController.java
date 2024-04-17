package com.softwarearchitecture.ecs;

import java.util.function.Consumer;

public interface InputController {
    void onTouch(Consumer<TouchLocation> onTouch);
    void onRelease(Consumer<TouchLocation> onRelease);
    TouchLocation getLastReleaseLocation();
}
