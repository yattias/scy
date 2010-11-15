package info.collide.android.scydatacollector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DataCollectorActivity extends Activity {

    Button btnLoadConfig;

    Button btnQuit;

    Button btnOpenFormOverview;

    Button btnOpenDownloadFormsOverview;

    // Button btnOpenSynchronize;
    DataCollectorController dcc;

    DataCollectorActivity application;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = this;
        setContentView(R.layout.mainmenu);

        btnOpenDownloadFormsOverview = (Button) findViewById(R.id.btnOpenDownloadForms);
        btnLoadConfig = (Button) findViewById(R.id.btnOpenConfig);
        btnQuit = (Button) findViewById(R.id.btnQuit);
        btnOpenFormOverview = (Button) findViewById(R.id.btnOpenOverview);

        dcc = new DataCollectorController(this);
    }

    public void addbtnQuit(OnClickListener ocl) {
        btnQuit.setOnClickListener(ocl);
    }

    public void addbtnOpenFormOverview(OnClickListener ocl) {
        btnOpenFormOverview.setOnClickListener(ocl);
    }

    public void addbtnLoadConfig(OnClickListener ocl) {
        btnLoadConfig.setOnClickListener(ocl);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dcc.onActivityResult(requestCode, resultCode, data);
    }

    public void addbtnOpenDownloadFormOverview(OnClickListener ocl) {
        btnOpenDownloadFormsOverview.setOnClickListener(ocl);
    }

}