package byow.Core;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class PlayMusic {
    private AudioInputStream audioInputStream;
    private Clip clip;
    public void playLoop(String nameOfMusic){
        //@ChatGPT
        try{
            audioInputStream = AudioSystem.getAudioInputStream(new File(nameOfMusic).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e){
            System.out.println("Error playing music.");
            e.printStackTrace();
        }
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void playSingle(String nameOfMusic){
        //@ChatGPT
        try{
            audioInputStream = AudioSystem.getAudioInputStream(new File(nameOfMusic).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e){
            System.out.println("Error playing music.");
            e.printStackTrace();
        }
        clip.start();
    }
    public void stop(){
        //@ChatGPT
        clip.stop();
        clip.close();
    }
    /* @ChatGPT
    public static void main(String[] args) {
        PlayMusic p = new PlayMusic("bgm.wav");
        p.play();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }*/
}
