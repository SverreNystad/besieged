package com.softwarearchitecture.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.softwarearchitecture.ecs.InputController;
import com.softwarearchitecture.ecs.TouchLocation;
import java.util.function.Consumer;

public class LibGDXInput extends InputAdapter implements InputController {
    private Consumer<TouchLocation> onTouch;
    private Consumer<TouchLocation> onRelease;
    private Viewport viewport;

    public LibGDXInput(Viewport viewport) {
        Gdx.input.setInputProcessor(this);
        this.viewport = viewport;
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
            float correctedX = screenX - viewport.getLeftGutterWidth();
            float correctedY = screenY - viewport.getBottomGutterHeight();
            float u = correctedX / viewport.getScreenWidth();
            float v = 1f - correctedY / viewport.getScreenHeight();
            onTouch.accept(new TouchLocation(u, v));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (onRelease != null) {
            float correctedX = screenX - viewport.getLeftGutterWidth();
            float correctedY = screenY - viewport.getBottomGutterHeight();
            float u = correctedX / viewport.getScreenWidth();
            float v = 1f - correctedY / viewport.getScreenHeight();
            onRelease.accept(new TouchLocation(u, v));
        }
        return true;
    }
}
