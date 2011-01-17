package info.collide.android.scydatacollector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder {

    final MediaRecorder recorder = new MediaRecorder();

    final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/info.collide.android.scydatacollector/cache/temp.3gp";

    private boolean _isRecording = false;

    private DataFormElementModel _dfem = null;

    /**
     * Creates a new audio recording at the given path (relative to root of SD card).
     */
    public AudioRecorder(DataFormElementModel dfem) {
        _dfem = dfem;
    }

    /**
     * Starts a new recording.
     */
    public boolean start() throws IOException {
        boolean result = false;
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted.  It is " + state + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File voicetmp = new File(path);
        if (!voicetmp.exists()) {
            voicetmp.delete();
        }
        File directory = voicetmp.getParentFile();
        if (!directory.exists()) {
            Log.d("AudioRecorder", "Recording path " + directory.toString() + " does not exist!");
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Path to file could not be created.");
            }
        }
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(path);
            recorder.prepare();

            recorder.start();
            result = true;
            this._isRecording = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * Stops a recording that has been previously started.
     */
    public void stop() throws IOException {
        recorder.stop();

        // recorder.release();
        // TODO release recorder
        this._isRecording = false;
        // dfem.se
    }

    public void writeBytesToFile(byte[] buffer) throws IOException {
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(buffer);
        fos.close();
    }

    public byte[] getBytesFromFile() throws IOException {
        File file = new File(path);
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public Boolean isRecording() {
        return this._isRecording;
    }

}
