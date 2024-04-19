package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class SoundComponent implements Serializable {
    public String sound_path;
    public boolean loop = false;
    public boolean isPlaying = false;
    public boolean isBackgroundMusic = false;

    public SoundComponent(String sound_path, boolean loop, boolean isBackgroundMusic) {
        this.sound_path = sound_path;
        this.loop = loop;
        this.isBackgroundMusic = isBackgroundMusic;
    }
}
