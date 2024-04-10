package com.softwarearchitecture.ecs;

public class Controllers {
    public final GraphicsController graphicsController;
    public final InputController inputController;

    public Controllers(GraphicsController graphicsController, InputController inputController) {
        this.graphicsController = graphicsController;
        this.inputController = inputController;
    }
}
