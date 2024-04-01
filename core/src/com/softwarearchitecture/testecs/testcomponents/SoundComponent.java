package com.softwarearchitecture.testecs.testcomponents;

import java.io.Serializable;

public class SoundComponent implements Serializable {
    public String sound_path;

    public SoundComponent(String sound_path) {
        this.sound_path = sound_path;
    }
}
