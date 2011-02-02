package info.collide.android.scydatacollector;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DownloadFormsOverviewActivity extends ListActivity {

    private static final int DOWNLOAD_PROGRESS = 0;
    
    private Activity activity = this;

    private CheckBox ownForms;

    private WebServicesController wsc;

    private ProgressDialog progressDialog;

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
                downloadForms(lfdla.getAlDfpm());
            }

        });
        setListAdapter(lfdla);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DOWNLOAD_PROGRESS) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(R.string.msgPleaseWait);
            progressDialog.setMessage(getString(R.string.msgLoadFormRepo));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            return progressDialog;
        }
        return super.onCreateDialog(id);
    }

    private void downloadForms(final ArrayList<DataFormPreviewModel> alDfpm) {
        ArrayList<String> uris = new ArrayList<String>();
        for (DataFormPreviewModel dfpm : alDfpm) {
            if (dfpm.is_download()) {
                uris.add(dfpm.getURI());
            }
        }
        new AsyncTask<String, Integer, Void>() {

            protected void onPreExecute() {
                showDialog(DOWNLOAD_PROGRESS);
            };
            
            @Override
            protected Void doInBackground(String... uris) {
                int countForms = uris.length;
                String username = new DataCollectorConfiguration(activity).getUserName();
                
                for (int i = 0; i < uris.length; i++) {
                    publishProgress((int) ((i / (float) countForms) * 100));
                    DataCollectorFormModel dcfm = wsc.fetchDCFMFromServer(uris[i]);
                    if (dcfm != null) {
                        final ContentValues cv = new ContentValues();
                        try {
                            cv.put("dcfm", dcfm.toByteArray());
                            cv.put("username", username);
                            activity.getContentResolver().update(DataCollectorContentProvider.CONTENT_URI, cv, null, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                publishProgress(100);
                return null;
            }
            
            protected void onProgressUpdate(final Integer... progress) {
                Log.d("DataCollector", "Changing progress to " + progress[0]);
                runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        progressDialog.setProgress(progress[0]);
                    }
                });
            };
            
            protected void onPostExecute(Void result) {
                dismissDialog(DOWNLOAD_PROGRESS);
                finish();
            };
        }.execute(uris.toArray(new String[] {}));
    }
}
