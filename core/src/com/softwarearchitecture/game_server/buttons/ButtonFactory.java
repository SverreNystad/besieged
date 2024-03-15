package com.softwarearchitecture.game_server.buttons;

public class ButtonFactory {

    // TODO: logic for creating buttons

    public static Button createButton(int x, int y, ButtonObserver observer, ButtonType type, int width, int height) {
        return new Button(x, y, observer, type, width, height);
    }

}
