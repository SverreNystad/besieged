package com.softwarearchitecture.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.LongMap;

import java.util.HashMap;

import com.softwarearchitecture.ecs.SoundController;
import com.softwarearchitecture.ecs.components.SoundComponent;

public class LibGDXSound implements SoundController {
    // Cache loaded sounds to avoid loading them multiple times
    private final HashMap<String, Sound> soundCache = new HashMap<>();
    private final LongMap<Boolean> soundPlaying = new LongMap<>();
    private int volume;

    /**
     * Create a new LibGDXSound with the specified volume.
     * 
     * @param volume The volume to play sounds at.
     */
    public LibGDXSound(int volume) {
        this.volume = volume;
        if (volume < 0) {
            System.out.println("Volume must be greater than 0");
            this.volume = 0;
        } else if (volume > 100) {
            System.out.println("Volume must be less than 100");
            this.volume = 100;
        }
    }

    public void setVolume(int volume) {
        System.out.println("Setting volume to: " + volume);
        if (volume < 0 || volume > 100) {
            System.out.println("Volume must be between 0 and 100");
            return;
        }
        this.volume = volume;
        System.out.println("Volume set to: " + this.volume);
    }

    public void playSound(SoundComponent soundComponent) {
        Sound sound = soundCache.computeIfAbsent(soundComponent.sound_path, path -> {
            System.out.println("Loading sound: " + path);
            return Gdx.audio.newSound(Gdx.files.internal(path));
        });

        // Determine if sound should loop
        long id;
        if (soundComponent.loop) {
            id = sound.loop(((float) this.volume) / 100f); // Start looping the sound
        } else {
            id = sound.play(((float) this.volume) / 100f); // Play sound once
        }
        soundPlaying.put(id, true);
    }

    public void playBackgroundMusic(SoundComponent soundComponent) {
        if (!isAnySoundPlaying()) {
            Sound sound = soundCache.computeIfAbsent(soundComponent.sound_path, path -> {
                System.out.println("Loading sound: " + path);
                return Gdx.audio.newSound(Gdx.files.internal(path));
            });

            long id = sound.loop(((float) this.volume) / 100f);  // Always loop background music
            soundPlaying.put(id, true);  // Mark this sound ID as playing
        }
    }

    // Check if any sound is still marked as playing
    private boolean isAnySoundPlaying() {
        for (Boolean state : soundPlaying.values()) {
            if (state) {
                return true;
            }
        }
        return false;
    }


    // Optional: Consider adding a method to dispose of sounds when no longer needed
    public void dispose() {
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();
        soundPlaying.clear();
    }

    @Override
    public int getVolume() {
        return volume;
    }
}
