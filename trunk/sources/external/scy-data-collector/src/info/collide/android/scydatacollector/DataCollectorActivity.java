package info.collide.android.scydatacollector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DataCollectorActivity extends Activity {

    public static final int SETTINGSREQUESTCODE = 1;

    private ImageView openFormOverviewButton;
    
    private OnClickListener openFormListener;

    private ImageView openDownloadFormsOverviewButton;

    private ImageView loadConfigButton;

    private TextView openFormOverviewTitle;

    private OnClickListener openDownloadFormsOverviewListener;

    private TextView openDownloadFormsOverviewTitle;

    private OnClickListener loadConfigListener;

    private TextView loadConfigTitle;

    private TextView openFormOverviewText;

    private TextView openDownloadFormsOverviewText;

    private TextView loadConfigText;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainmenu);

        openFormListener = new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent tki = new Intent();
                tki.setClass(getApplication(), DataCollectorFormOverviewActivity.class);
                startActivity(tki);
            }
        };
        
        openFormOverviewButton = (ImageView) findViewById(R.id.FormsIcon);
        openFormOverviewTitle = (TextView) findViewById(R.id.SelectFormTitle);
        openFormOverviewText = (TextView) findViewById(R.id.SelectFormText);
       
        openDownloadFormsOverviewListener = new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (!new DataCollectorConfiguration(DataCollectorActivity.this).isComplete()) {
                    Toast.makeText(getApplication(), R.string.notconfiguredyet, Toast.LENGTH_LONG).show();
                } else {
                    Intent tki = new Intent();
                    tki.setClass(getApplication(), DownloadFormsOverviewActivity.class);
                    startActivity(tki);
                }            
            }
        };

        openDownloadFormsOverviewButton = (ImageView) findViewById(R.id.SyncIcon);
        openDownloadFormsOverviewTitle = (TextView) findViewById(R.id.SyncFormTitle);
        openDownloadFormsOverviewText = (TextView) findViewById(R.id.SyncFormText);
        
        loadConfigButton = (ImageView) findViewById(R.id.PreferencesIcon);
        loadConfigTitle = (TextView) findViewById(R.id.PreferencesTitle);
        loadConfigText = (TextView) findViewById(R.id.PreferencesText);
        
        loadConfigListener = new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent tki = new Intent();
                tki.setClass(getApplication(), DataCollectorConfigurationActivity.class);
                startActivityForResult(tki, SETTINGSREQUESTCODE);            
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGSREQUESTCODE) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    setTitle(getString(R.string.app_name) + " - User: " + bundle.getString("configUserName"));
                }
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // register listeners
        openFormOverviewButton.setOnClickListener(openFormListener);
        openFormOverviewTitle.setOnClickListener(openFormListener);
        openFormOverviewText.setOnClickListener(openFormListener);
        
        openDownloadFormsOverviewButton.setOnClickListener(openDownloadFormsOverviewListener);
        openDownloadFormsOverviewTitle.setOnClickListener(openDownloadFormsOverviewListener);
        openDownloadFormsOverviewText.setOnClickListener(openDownloadFormsOverviewListener);
        
        loadConfigButton.setOnClickListener(loadConfigListener);
        loadConfigTitle.setOnClickListener(loadConfigListener);
        loadConfigText.setOnClickListener(loadConfigListener);
    }
    
}