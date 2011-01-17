package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.AudioPlayer;
import info.collide.android.scydatacollector.AudioRecorder;
import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DataFormElementVoiceView extends DataFormElementView {

    private AudioRecorder ar;

    private AudioPlayer ap;

    private ImageButton buttonRec;
    
    private RecordButtonListener buttonRecListener; 

    private ImageButton buttonStop;

    private ImageButton buttonPlay;

    private ImageButton buttonDetails;
    

    public DataFormElementVoiceView(final DataFormElementModel elementModel, final DataCollectorFormActivity application, final int id) {
        super(elementModel, application);

        ar = new AudioRecorder(elementModel);

        inflate(getApplication(), R.layout.voiceformelement, this);
        
        TextView label = (TextView) findViewById(R.id.voiceformelement_label);
        label.setWidth(super.Column1width);
        label.setText(elementModel.getTitle());

        buttonRec = (ImageButton) findViewById(R.id.voiceformelement_record);
        buttonRec.setMinimumWidth((Column2width + Column3width + Column4width) / 3);

        buttonStop = (ImageButton) findViewById(R.id.voiceformelement_stop);
        buttonStop.setEnabled(false);
        buttonStop.setMinimumWidth((Column2width + Column3width + Column4width) / 3);

        buttonPlay = (ImageButton) findViewById(R.id.voiceformelement_play);
        buttonPlay.setMinimumWidth((Column2width + Column3width + Column4width) / 3);

        buttonDetails = (ImageButton) findViewById(R.id.voiceformelement_details);
        buttonDetails.setId(id);
        buttonDetails.setMinimumWidth(Column5width);
        buttonDetails.setVisibility(INVISIBLE);

        buttonDetails.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                openDetail(application, id);
            }
        });

        buttonRecListener = new RecordButtonListener();
        buttonRec.setOnClickListener(buttonRecListener);

        buttonStop.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    if (ar.isRecording()) {
                        ar.stop();
                        elementModel.addStoredData(ar.getBytesFromFile());
                        events(DataFormElementEventTypes.ONAFTER);
                        buttonRec.setEnabled(true);
                        buttonPlay.setEnabled(true);
                        buttonStop.setEnabled(false);
                        // ap.refresh();
                        Toast.makeText(getApplication(), "Aufnahme gestoppt", 10).show();

                    } else if (ap != null && ap.isPlaying()) {
                        Toast.makeText(getApplication(), "Abspielen gestoppt", 10).show();
                        ap.stop();
                        buttonPlay.setEnabled(true);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonPlay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null)
                        ar.writeBytesToFile(elementModel.getStoredData(elementModel.getDataList().size() - 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ap = new AudioPlayer();
                ap.play();
                buttonPlay.setEnabled(false);
                buttonRec.setEnabled(false);
                buttonStop.setEnabled(true);
                Toast.makeText(getApplication(), "Abspielen", 10).show();

                ap.setOnCompletitionListener(new OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {
                        Toast.makeText(getApplication(), "Abspielen fertig", 10).show();
                        buttonPlay.setEnabled(true);
                        buttonRec.setEnabled(true);
                        buttonStop.setEnabled(false);
                        ap.release();
                    }
                });

            }
        });
        updateView(elementModel);
    }

    private class RecordButtonListener implements OnClickListener {
        public void onClick(View v) {
            try {
                if (ar.start()) {
                    events(DataFormElementEventTypes.ONBEFORE);
                    Toast.makeText(getApplication(), "Aufnahme gestartet", 100).show();

                    buttonStop.setEnabled(true);
                    buttonRec.setEnabled(false);
                } else {
                    Toast.makeText(getApplication(), "Aufnahme konnte nicht gestartet werden", 100).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void updateView(DataFormElementModel elementModel) {
        if (elementModel.getDataList().size() < 1) {
            buttonDetails.setVisibility(INVISIBLE);
        } else {
            buttonDetails.setVisibility(VISIBLE);
        }
        if (getDfem().getStoredData(elementModel.getDataList().size() - 1) == null) {
            buttonPlay.setEnabled(false);
        }
    }
}
