package com.softwarearchitecture.ecs.components;

import java.io.Serializable;
import java.util.List;

public class AnimationComponent implements Serializable {

    public List<String> animationPaths;
    public int currentFrame;
    public int frameCount;

    public AnimationComponent(List<String> animationPaths) {
        this.animationPaths = animationPaths;
        this.currentFrame = 0;
        this.frameCount = animationPaths.size();
    }

    public String getCurrentFrame() {
        return animationPaths.get(currentFrame);
    }

    public void nextFrame() {
        currentFrame = (currentFrame + 1) % frameCount;
    }

    public void previousFrame() {
        currentFrame = (currentFrame - 1 + frameCount) % frameCount;
    }

    public void setFrame(int frame) {
        currentFrame = frame % frameCount;
    }

    public void reset() {
        currentFrame = 0;
    }

    public void setAnimation(List<String> animationPaths) {
        this.animationPaths = animationPaths;
        this.frameCount = animationPaths.size();
        currentFrame = 0;
    }

    public String getAnimationPath() {
        return animationPaths.get(this.currentFrame);
    }

}
