package com.softwarearchitecture.game_server.buttons;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GameMenuButton extends Button {

    private final int WIDTH = 100;
    private final int HEIGHT = 100;

    public GameMenuButton(int x, int y, ButtonObserver observer) {
        position = new Vector3(x, y, 0);
        hitBox = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
        observers = new ArrayList<>();
        attachObserver(observer);

        // TODO: Add texture, maybe through a entity component system maybe?

    }

    @Override
    protected void notifyObservers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyObservers'");
    }

    @Override
    protected void attachObserver(ButtonObserver observer) {
        observers.add(observer);
    }

    @Override
    public void update(Vector3 mouse) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
