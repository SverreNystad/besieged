package com.softwarearchitecture.ecs;

public class Controllers {
    public final GraphicsController graphicsController;
    public final InputController inputController;
    public final SoundController soundController;

    public Controllers(GraphicsController graphicsController, InputController inputController, SoundController soundController) {
        this.graphicsController = graphicsController;
        this.inputController = inputController;
        this.soundController = soundController;
    }
}
