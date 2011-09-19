package info.collide.android.scydatacollector;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

public class DownloadFormsOverviewActivity extends ListActivity {

    private static final int DOWNLOAD_PROGRESS = 0;

    public static final int PREVIEW_PROGRESS = 1;
    
    private Activity activity = this;

    private CheckBox ownForms;

    private WebServicesController wsc;

    private ProgressDialog progressDialog;

    private DataCollectorContentProvider dccp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloadforms);

        ownForms = (CheckBox) findViewById(R.id.chkOwnFormulars);
        wsc = new WebServicesController(activity);
        ListView lv = (ListView) findViewById(android.R.id.list);

        dccp = new DataCollectorContentProvider(this);

        final LoadFormsDataListAdapter lfdla = new LoadFormsDataListAdapter(this, lv, wsc, ownForms.isChecked());

        ownForms.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new FormOverviewDownloader().execute();
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

        new FormOverviewDownloader().execute();
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DOWNLOAD_PROGRESS) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(R.string.msgPleaseWait);
            progressDialog.setMessage(getString(R.string.msgLoadFormRepo));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            return progressDialog;
        } else if (id == PREVIEW_PROGRESS) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(R.string.msgPleaseWait);
            progressDialog.setMessage(getString(R.string.msgLoadFormRepo));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            return progressDialog;
        }
        return super.onCreateDialog(id);
    }

    private void downloadForms(final ArrayList<DataFormPreviewModel> alDfpm) {
        final ArrayList<String> uris = new ArrayList<String>();
        for (DataFormPreviewModel dfpm : alDfpm) {
            if (dfpm.is_download()) {
                uris.add(dfpm.getURI());
            }
        }
        new AsyncTask<String, Integer, Void>() {

            protected void onPreExecute() {
                showDialog(DOWNLOAD_PROGRESS);
                progressDialog.setMax(uris.size());
            };
            
            @Override
            protected Void doInBackground(String... uris) {
                String username = new DataCollectorConfiguration(activity).getUserName();
                
                for (int i = 0; i < uris.length; i++) {
                    publishProgress(i);
                    DataCollectorFormModel dcfm = wsc.fetchDCFMFromServer(uris[i]);
                    if (dcfm != null) {
                        dccp.update(dcfm, username);
                    }
                }
                publishProgress(uris.length);
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

    class FormOverviewDownloader extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showDialog(PREVIEW_PROGRESS);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (getListAdapter() instanceof LoadFormsDataListAdapter) {
                LoadFormsDataListAdapter listAdapter = (LoadFormsDataListAdapter) getListAdapter();
                listAdapter.downloadForms(ownForms.isChecked());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            LoadFormsDataListAdapter listAdapter = (LoadFormsDataListAdapter) getListAdapter();
            listAdapter.notifyDataSetChanged();
            dismissDialog(PREVIEW_PROGRESS);
        }

    }
}