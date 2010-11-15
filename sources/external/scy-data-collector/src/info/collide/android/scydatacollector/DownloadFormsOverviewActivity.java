package info.collide.android.scydatacollector;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DownloadFormsOverviewActivity extends ListActivity {

    private Activity activity = this;

    ProgressDialog mypd;

    private CheckBox ownForms;

    protected static final int GUIUPDATEIDENTIFIER = 99822832;

    // Set up the message handler
    Handler myGUIUpdateHandler = new Handler() {

        // @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GUIUPDATEIDENTIFIER:
                    invalidate();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private WebServicesController wsc;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloadforms);

        ownForms = (CheckBox) findViewById(R.id.chkOwnFormulars);
        wsc = new WebServicesController(activity);
        ListView lv = (ListView) findViewById(android.R.id.list);

        final LoadFormsDataListAdapter lfdla = new LoadFormsDataListAdapter(this, lv, ownForms.isChecked());

        ownForms.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lfdla.update(ownForms.isChecked());
            }
        });

        Button btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setEnabled(false);
        btnDownload.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                DownloadFormsOverviewActivity.this.downloadForms(lfdla.getAlDfpm());
            }

        });
        setListAdapter(lfdla);
    }

    private void downloadForms(final ArrayList<DataFormPreviewModel> alDfpm) {
        int i = 0;
        for (DataFormPreviewModel dfpm : alDfpm) {
            if (dfpm.is_download())
                i++;
        }
        MessageDialog mdDownload = new MessageDialog(this);
        android.content.DialogInterface.OnClickListener oclYes = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                final ProgressDialog mypd = ProgressDialog.show(activity, getResources().getString(R.string.msgLoadFormRepo), getResources().getString(R.string.msgPleaseWait), false);
                Thread t = new Thread() {

                    public void run() {
                        for (DataFormPreviewModel dfpm : alDfpm) {
                            if (dfpm.is_download()) {
                                DataCollectorFormModel dcfm = wsc.fetchDCFMFromServer(dfpm.getURI());
                                if (dcfm != null) {
                                    final ContentValues cv = new ContentValues();
                                    String username = new DataCollectorConfiguration(activity).getUserName();
                                    try {
                                        cv.put("dcfm", dcfm.toByteArray());
                                        cv.put("username", username);

                                        // Uri uri = Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms");
                                        activity.getContentResolver().update(DataCollectorContentProvider.CONTENT_URI, cv, null, null);
                                        activity.runOnUiThread(new Runnable() {
                                            
                                            @Override
                                            public void run() {
                                                mypd.dismiss();
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                };
                t.start();
            }

        };
        // TODO: i18n
        mdDownload.createYesNoDialog(getResources().getString(R.string.msgDownloadForms), oclYes, "Ja", null, "Nein");

    }

    public void invalidate() {
        if (mypd != null)
            mypd.dismiss();
        activity.finish();

    }
}
