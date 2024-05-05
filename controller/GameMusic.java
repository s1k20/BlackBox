package controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class GameMusic {
    private Clip music;

    public void playMusic() {
        try {
            // get audio from file source
            URL url = this.getClass().getResource("gamemusic/blackboxgamemusic.wav");
            assert url != null;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

            music = AudioSystem.getClip();

            music.open(audioIn);
            music.start(); // Play the audio clip

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file format not supported.");
        } catch (IOException e) {
            System.out.println("Error reading the audio file.");
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable.");
        }
    }

    public void stopMusic() {
        this.music.stop();
    }

    private void playSound(String filePath) {
        try {
            // get audio file from file path
            URL url = this.getClass().getResource(filePath);
            assert url != null;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();

            clip.open(audioIn);
            clip.start(); // Play the audio clip

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file format not supported.");
        } catch (IOException e) {
            System.out.println("Error reading the audio file.");
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable.");
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

