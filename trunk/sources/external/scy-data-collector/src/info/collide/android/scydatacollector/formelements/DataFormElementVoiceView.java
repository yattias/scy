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

    private DataFormElementController _dfec;

    public DataFormElementVoiceView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);
        _dfec = dfec;
        TextView label = new TextView(getApplication());
        label.setWidth(super.Column1width);
        ar = new AudioRecorder(dfem);

        final ImageButton btnRec = new ImageButton(getApplication());
        final ImageButton btnStp = new ImageButton(getApplication());
        final ImageButton btnPly = new ImageButton(getApplication());

        ImageButton _btnDetails = new ImageButton(application);
        // _btnDetails.setText("Details");
        _btnDetails.setImageResource(R.drawable.ic_menu_info_details);
        _btnDetails.setMaxHeight(RowMaxHeight);
        _btnDetails.setId(id);

        _btnDetails.setMinimumWidth(Column5width);

        _btnDetails.setId(id);
        if (dfem.getDataList().size() < 1) {
            _btnDetails.setVisibility(INVISIBLE);
        } else
            _btnDetails.setVisibility(VISIBLE);
        _btnDetails.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        label.setText(dfem.getTitle());

        // btnRec.setText("o");
        // Bitmap icon = Bitmap.
        btnRec.setImageResource(R.drawable.ic_btn_speak_now);
        btnStp.setImageResource(R.drawable.media_controls_dark_stop);
        btnPly.setImageResource(R.drawable.media_controls_dark_play);
        // btnStp.setText("#");
        // btnPly.setText(">");

        btnStp.setEnabled(false);

        if (getDfem().getStoredData(dfem.getDataList().size() - 1) != null) {

        } else {
            btnPly.setEnabled(false);
        }
        btnRec.setMinimumWidth((Column2width + Column3width + Column4width) / 3);
        btnStp.setMinimumWidth((Column2width + Column3width + Column4width) / 3);
        btnPly.setMinimumWidth((Column2width + Column3width + Column4width) / 3);

        btnRec.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                try {
                    if (ar.start()) {
                        _dfec.events(DataFormElementEventTypes.ONBEFORE);
                        Toast.makeText(getApplication(), "Aufnahme gestartet", 100).show();

                        btnStp.setEnabled(true);
                        btnRec.setEnabled(false);
                    } else
                        Toast.makeText(getApplication(), "Aufnahme konnte nicht gestartet werden", 100).show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        btnStp.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    if (ar.isRecording()) {
                        ar.stop();
                        dfem.addStoredData(ar.getBytesFromFile());
                        _dfec.events(DataFormElementEventTypes.ONAFTER);
                        btnRec.setEnabled(true);
                        btnPly.setEnabled(true);
                        btnStp.setEnabled(false);
                        // ap.refresh();
                        Toast.makeText(getApplication(), "Aufnahme gestoppt", 10).show();

                    } else if (ap != null && ap.isPlaying()) {
                        Toast.makeText(getApplication(), "Abspielen gestoppt", 10).show();
                        ap.stop();
                        btnPly.setEnabled(true);

                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnPly.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    if (dfem.getStoredData(dfem.getDataList().size() - 1) != null)
                        ar.writeBytesToFile(dfem.getStoredData(dfem.getDataList().size() - 1));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ap = new AudioPlayer();
                ap.play();
                btnPly.setEnabled(false);
                btnRec.setEnabled(false);
                btnStp.setEnabled(true);
                Toast.makeText(getApplication(), "Abspielen", 10).show();

                ap.setOnCompletitionListener(new OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplication(), "Abspielen fertig", 10).show();
                        btnPly.setEnabled(true);
                        btnRec.setEnabled(true);
                        btnStp.setEnabled(false);
                        ap.release();
                    }
                });

            }
        });
        addView(label);
        addView(btnRec);
        addView(btnStp);
        addView(btnPly);
        addView(_btnDetails);
    }
}
