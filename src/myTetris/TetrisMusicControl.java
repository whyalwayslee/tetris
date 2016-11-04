package myTetris;

import javax.swing.*;

/**
 * Created by Lee.Chisnall on 04/11/2016.
 */
public class TetrisMusicControl implements MusicControl {

    private java.applet.AudioClip backgroundMusic;

    public TetrisMusicControl(){
        ClassLoader ldr = this.getClass().getClassLoader();
        backgroundMusic = JApplet.newAudioClip(ldr.getResource("myTetris/ThemeA.mid"));
    }

    @Override
    public void playBackgroundMusic() {
        backgroundMusic.play();
    }

}
