package com.softwarearchitecture.game_client;

import java.util.function.Consumer;

public interface InputController {
    void onTouch(Consumer<TouchLocation> onTouch);
    void onRelease(Consumer<TouchLocation> onRelease);
}
