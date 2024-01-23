import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sounds extends Thread {
    File sound;
    Clip clip;

    public Sounds(String hang) {
        this.sound = new File(hang);
    }

    @Override
    public void run()
    {
        try{
            this.clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
        //  System.out.println(sound + " is running");
            System.out.println();
            Thread.sleep(clip.getMicrosecondLength()/1000);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}

