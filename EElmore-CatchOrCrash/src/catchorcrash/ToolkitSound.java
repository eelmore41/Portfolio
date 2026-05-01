package catchorcrash;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ToolkitSound {

    public static void playGoodSound() { //Good item
        playSound("sounds/good.wav");
    }

    public static void playBadSound() { //Bad item
        playSound("sounds/bad.wav");
    }

    public static void playLevelUp() { //New level
        playSound("sounds/levelup.wav");
    }

    public static void playDeathSound() { //Death scene
        playSound("sounds/gameover.wav");
    }

    public static void playCongratsSound() { //Beat level mode 
        playSound("sounds/congrats.wav");
    }

    private static void playSound(String fileName) {
        try {
            //Loads and plays the wav file from the sounds folder
            File soundFile = new File(fileName);

            if (!soundFile.exists()) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }
}
