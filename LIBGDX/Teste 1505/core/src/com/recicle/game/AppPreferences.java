package com.recicle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppPreferences {

    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREF_NAME = "default";
    //These will store the values of the preferences which will get saved when we close our application.

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREF_NAME);
    }

    //GETTERS

    public float getMusicVolume() { //returns the volume we set it up
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f); //0.5 for half volume or 1.0 for full volume
    }

    public float getSoundVolume(){
        return getPrefs().getFloat(PREF_SOUND_VOL, 0.5f);
    }

    public boolean isMusicEnabled(){
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public boolean isSoundEnabled(){
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

     //SETTERS

    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume); //to save the value of the volume to our preferences
        getPrefs().flush(); //to make sure they are written to disk and saved
    }

    public void setSoundVolume(float volume){
        getPrefs().putFloat(PREF_SOUND_VOL, volume);
        getPrefs().flush();
    }

    public void setMusicEnabled(boolean musicEnabled){
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public void setSoundEnabled(boolean soundEnabled){
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEnabled);
        getPrefs().flush();
    }

}
