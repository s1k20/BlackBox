package controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class GameMusic {
    Clip music;

    public void playMusic() {
        try {
            // Obtain an audio input stream from the audio file
            URL url = this.getClass().getResource("gamemusic/blackboxgamemusic.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

            // Get a sound clip resource
            music = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream
            music.open(audioIn);
            music.start(); // Play the audio clip

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file format not supported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the audio file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable.");
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        this.music.stop();
    }

    private void playSound(String filePath) {
        try {
            // Obtain an audio input stream from the audio file
            URL url = this.getClass().getResource(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

            // Get a sound clip resource
            Clip clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream
            clip.open(audioIn);
            clip.start(); // Play the audio clip

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file format not supported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the audio file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable.");
            e.printStackTrace();
        }

    }

    public void playAtomPlace() {
        playSound("sfx/atom_place.wav");
    }

    public void playNextSound() {
        playSound("sfx/next_sound.wav");
    }

    public void playAtomRemoved() {
        playSound("sfx/remove_atom.wav");
    }

    public void playSendRay() {
        playSound("sfx/send_ray.wav");
    }
}

