package info.collide.android.scydatacollector;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;

public class AudioPlayer {

    final MediaPlayer mp = new MediaPlayer();

    final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/info.collide.android.scydatacollector/cache/temp.3gp";
    
    public AudioPlayer() {
        try {
            mp.setDataSource(path);
            mp.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnCompletitionListener(OnCompletionListener ocl) {
        mp.setOnCompletionListener(ocl);
    }

    public void play() {
        if (!mp.isPlaying()) {
            mp.start();
        }
    }

    public boolean isPlaying() {
        return mp.isPlaying();

    }

    public void stop() {
        if (mp.isPlaying()) {
            mp.stop();
            mp.seekTo(0);
        }
    }

    public void release() {
        mp.release();
    }
}
