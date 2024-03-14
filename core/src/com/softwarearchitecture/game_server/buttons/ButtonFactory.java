package com.softwarearchitecture.game_server.buttons;

public class ButtonFactory {

    public static Button create(int x, int y, ButtonType buttonType, ButtonObserver observer) {
        switch (buttonType) {

            case OPTIONS:
                return new OptionsButton(x, y, observer);
            case QUIT:
                return new QuitButton(x, y, observer);
            case MULTI_PLAYER:
                return new MultiplayerButton(x, y, observer);
            case SINGLE_PLAYER:
                return new SingleplayerButton(x, y, observer);
            case JOIN:
                return new JoinButton(x, y, observer);
            case HOST:
                return new HostButton(x, y, observer);
            case PAUSE:
                return new PauseButton(x, y, observer);
            case GAME_MENU:
                return new GameMenuButton(x, y, observer);

        }
        return null;
    }

}
