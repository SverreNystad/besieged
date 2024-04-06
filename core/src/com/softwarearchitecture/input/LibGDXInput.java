package com.softwarearchitecture.input;

import java.util.function.Consumer;

import com.softwarearchitecture.game_client.InputController;
import com.softwarearchitecture.game_client.TouchLocation;

public class LibGDXInput implements InputController {

    @Override
    public void onTouch(Consumer<TouchLocation> onTouch) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onTouch'");
    }

    @Override
    public void onRelease(Consumer<TouchLocation> onRelease) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onRelease'");
    }

}
