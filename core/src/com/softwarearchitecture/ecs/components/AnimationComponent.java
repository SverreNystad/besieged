package com.softwarearchitecture.ecs.components;

import java.io.Serializable;
import java.util.List;

public class AnimationComponent implements Serializable {

    private List<String> animationPaths;
    private int currentFrame;
    private int frameCount;
    private float timeSinceLastFrame;
    private final float frameDuration = 0.15f;

    public AnimationComponent(List<String> animationPaths) {
        this.animationPaths = animationPaths;
        this.currentFrame = 0;
        this.frameCount = animationPaths.size();
    }

    public String getFramePath(float deltaTime) {
        timeSinceLastFrame += deltaTime;
        if (timeSinceLastFrame >= frameDuration) {
            timeSinceLastFrame = timeSinceLastFrame - frameDuration;
            nextFrame();
        }
        return animationPaths.get(currentFrame);
    }

    private void nextFrame() {
        currentFrame = (currentFrame + 1) % frameCount;
    }

    public void setAnimation(List<String> animationPaths) {
        this.animationPaths = animationPaths;
        this.frameCount = animationPaths.size();
        currentFrame = 0;
    }

}
