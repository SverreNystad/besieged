package com.softwarearchitecture.game_server.buttons;

import java.util.List;

import com.softwarearchitecture.GameApp;

public class ButtonLayout {

    public ButtonLayout(int numberOfGrids, Layout layout, List<ButtonObserver> observers) {

        /*
         * Creates different layouts with buttons for each screen
         */

        int width = GameApp.WIDTH;
        int height = GameApp.HEIGHT;

        int gridWidth = width / numberOfGrids;
        int gridHeight = height / numberOfGrids;

        switch (layout) {
            case MENU_SCREEN: {
                for (ButtonObserver observer : observers) {
                    ButtonFactory.createButton(gridWidth * numberOfGrids / 2, 2 * gridHeight, observer,
                            ButtonType.OPTIONS, gridWidth, gridHeight);

                }
            }
            case GAME_SCREEN: {
                for (ButtonObserver observer : observers) {
                    ButtonFactory.createButton(gridWidth * numberOfGrids / 2, 2 * gridHeight, observer,
                            ButtonType.PAUSE, gridWidth, gridHeight);
                }
            }
            default: {
                throw new UnsupportedOperationException("Unimplemented layout");
            }

        }

    }
}
