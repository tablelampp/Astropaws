package model;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * This class manages the sound in the game.
 */
public class SoundManager {
    private static SoundManager instance;
    private Clip mainThemeClip;
    private Clip clickClip;
    private Clip collectClip;
    private boolean isMainThemePlaying = false;
    private boolean isGameThemePlaying = false;

    /**
     * Private constructor to enforce singleton pattern
     */
    private SoundManager() {
        
    }

    /**
     * Returns the singleton instance of the SoundManager. If the 
     * instance does not exist, it creates one.
     * 
     * @return the instance of SoundManager
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Loads a sound file from the specified path and opens it in the given Clip.
     * 
     * @param path the path of the sound file.
     * @param clip the Clip to load the sound into.
     */
    private void loadSound(String path, Clip clip) {
        try {
            URL soundURL = getClass().getResource(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound: " + e.getMessage());
        }
    }

    /**
     * Sets the volume of the given sound.
     * 
     * @param clip the Clip whose volume is to be set.
     * @param volume the desired volume level.
     */
    private void setVolume(Clip clip, float volume) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            } catch (Exception e) {
                System.err.println("Error setting volume: " + e.getMessage());
            }
        }
    }

    /**
     * Plays the main theme music on a continuous loop.
     */
    public void playMainTheme() {
        if (!isMainThemePlaying) {
            try {
                if (mainThemeClip == null) {
                    mainThemeClip = AudioSystem.getClip();
                    loadSound("/model/assets/SFX/maintheme.wav", mainThemeClip);
                }
                mainThemeClip.setFramePosition(0);
                // Set volume to 0.8 (slightly quieter) for main theme
                setVolume(mainThemeClip, 0.8f);
                mainThemeClip.loop(Clip.LOOP_CONTINUOUSLY);
                mainThemeClip.start();
                isMainThemePlaying = true;
            } catch (Exception e) {
                System.err.println("Error playing main theme: " + e.getMessage());
            }
        }
    }

    /**
     * Plays the game theme music on a continuous loop.
     */
    public void playGameTheme() {
        if (!isGameThemePlaying) {
            try {
                if (mainThemeClip == null) {
                    mainThemeClip = AudioSystem.getClip();
                    loadSound("/model/assets/SFX/maintheme.wav", mainThemeClip);
                }
                mainThemeClip.setFramePosition(0);
                // Set volume to 0.8 (slightly quieter) for game theme
                setVolume(mainThemeClip, 0.8f);
                mainThemeClip.loop(Clip.LOOP_CONTINUOUSLY);
                mainThemeClip.start();
                isGameThemePlaying = true;
            } catch (Exception e) {
                System.err.println("Error playing game theme: " + e.getMessage());
            }
        }
    }

    /**
     * Play the click sound effect.
     */
    public void playClickSound() {
        try {
            if (clickClip == null) {
                clickClip = AudioSystem.getClip();
                loadSound("/model/assets/SFX/click.wav", clickClip);
            }
            clickClip.setFramePosition(0);
            clickClip.start();
        } catch (Exception e) {
            System.err.println("Error playing click sound: " + e.getMessage());
        }
    }

    /**
     * Plays the collect sound effect.
     */
    public void playCollectSound() {
        try {
            if (collectClip == null) {
                collectClip = AudioSystem.getClip();
                loadSound("/model/assets/SFX/collect.wav", collectClip);
            }
            collectClip.setFramePosition(0);
            // Set volume to 2.0 (twice as loud) for collect sound
            setVolume(collectClip, 2.0f);
            collectClip.start();
        } catch (Exception e) {
            System.err.println("Error playing collect sound: " + e.getMessage());
        }
    }

    /**
     * Stops playing the main theme music if it is currently playing.
     */
    public void stopMainTheme() {
        if (mainThemeClip != null && mainThemeClip.isRunning()) {
            mainThemeClip.stop();
            mainThemeClip.close();
            mainThemeClip = null;
            isMainThemePlaying = false;
        }
    }

    /**
     * Stops the game theme music if it is currently playing.
     */
    public void stopGameTheme() {
        if (mainThemeClip != null && mainThemeClip.isRunning()) {
            mainThemeClip.stop();
            mainThemeClip.close();
            mainThemeClip = null;
            isGameThemePlaying = false;
        }
    }
} 