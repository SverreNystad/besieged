package com.softwarearchitecture.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.ecs.components.SoundComponent;

public class LibGDXSound implements SoundController {
    // Cache loaded sounds to avoid loading them multiple times
    private final HashMap<String, Sound> soundCache = new HashMap<>();
    private float volume;

    /**
     * Create a new LibGDXSound with the specified volume.
     * @param volume The volume to play sounds at.
     */
    public LibGDXSound(float volume) {
        this.volume = volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void playSound(SoundComponent soundComponent, float volume) {
        // Check if the sound is already loaded
        Sound sound = soundCache.get(soundComponent.sound_path);
        if (sound == null) {
            System.out.println("Loading sound: " + soundComponent.sound_path);
            // Sound not in cache, load it and add to cache
            sound = Gdx.audio.newSound(Gdx.files.internal(soundComponent.sound_path));
            soundCache.put(soundComponent.sound_path, sound);
        }
        
        // Play the sound
        sound.play(volume);
    }
    
    @Override
    public void playSound(SoundComponent soundComponent) {
        playSound(soundComponent, volume);
    }

    // Optional: Consider adding a method to dispose of sounds when no longer needed
    public void dispose() {
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();
    }
}
