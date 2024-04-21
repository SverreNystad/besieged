package com.softwarearchitecture.ecs;

import com.softwarearchitecture.ecs.components.SoundComponent;

/**
 * The {@code SoundController} interface defines methods for controlling the playback of sound effects and background
 * music within the game. It allows for triggering specific sounds linked to game events, managing ambient music, 
 * and adjusting overall sound levels. This interface serves as an abstraction layer over the audio system, 
 * facilitating easy integration and manipulation of sound throughout the game.
 */
public interface SoundController {

    /**
     * Plays a specific sound effect once. This method is typically used to respond to game events such as actions
     * performed by the player or significant game state changes.
     *
     * @param soundComponent the {@link SoundComponent} that contains the audio data and playback properties for the sound effect.
     */
    void playSound(SoundComponent soundComponent);

    /**
     * Starts or changes the background music being played. Unlike sound effects, background music is intended to loop continuously
     * and create an auditory backdrop for the game. This method can be used to set or change the music based on game context, such
     * as switching themes between levels or different game states.
     *
     * @param soundComponent the {@link SoundComponent} that contains the audio data and playback properties for the background music.
     */
    void playBackgroundMusic(SoundComponent soundComponent);
    /**
     * Retrieves the current volume level of the game's audio output. Volume levels are typically managed on a scale (e.g., 0-100)
     * to represent the intensity of the sound output.
     *
     * @return the current volume level, typically a value between 0 (muted) and 100 (maximum volume).
     */
    int getVolume();

    /**
     * Sets the volume level for the game's audio output. This allows the player or the game's settings to adjust the loudness
     * of all sound effects and music according to preferences or environmental requirements.
     *
     * @param volume the new volume level, where 0 is muted and higher values increase the sound's loudness, typically capped at 100.
     */
    void setVolume(int volume);
}
