package com.softwarearchitecture.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.softwarearchitecture.ecs.InputController;
import com.softwarearchitecture.ecs.TouchLocation;
import java.util.function.Consumer;

public class LibGDXInput extends InputAdapter implements InputController {
    private Consumer<TouchLocation> onTouch;
    private Consumer<TouchLocation> onRelease;

    public LibGDXInput() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void onTouch(Consumer<TouchLocation> onTouch) {
        this.onTouch = onTouch;
    }

    @Override
    public void onRelease(Consumer<TouchLocation> onRelease) {
        this.onRelease = onRelease;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (onTouch != null) {
            float u = screenX / (float) Gdx.graphics.getWidth();
            float v = 1 - screenY / (float) Gdx.graphics.getHeight(); // LibGDX's Y-axis is inverted
            onTouch.accept(new TouchLocation(u, v));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (onRelease != null) {
            float u = screenX / (float) Gdx.graphics.getWidth();
            float v = 1 - screenY / (float) Gdx.graphics.getHeight(); // LibGDX's Y-axis is inverted
            onRelease.accept(new TouchLocation(u, v));
        }
        return true;
    }
}
