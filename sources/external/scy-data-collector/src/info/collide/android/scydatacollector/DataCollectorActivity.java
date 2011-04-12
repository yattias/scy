package info.collide.android.scydatacollector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.Log;
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
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        openFormOverviewButton.setColorFilter(0x22FFFFFF, Mode.SRC_ATOP);
                        openFormOverviewTitle.setTextColor(0xFF00851B);
                        openFormOverviewText.setTextColor(0xFF00851B);
                    }
                });
                Log.d("DataCollector", "Button state: " + openDownloadFormsOverviewButton.isPressed() + openDownloadFormsOverviewButton.isSelected() + openDownloadFormsOverviewButton.isFocused());
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
                Log.i("DataCollector", Thread.currentThread().getName());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        openDownloadFormsOverviewButton.setColorFilter(0x22FFFFFF, Mode.SRC_ATOP);
                        openDownloadFormsOverviewTitle.setTextColor(0xFF00851B);
                        openDownloadFormsOverviewText.setTextColor(0xFF00851B);
                    }
                });
                if (!new DataCollectorConfiguration(DataCollectorActivity.this).isComplete()) {
                    Toast.makeText(getApplication(), R.string.notconfiguredyet, Toast.LENGTH_LONG).show();
                    openDownloadFormsOverviewButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        	openDownloadFormsOverviewButton.setColorFilter(0x00000000, Mode.SRC_ATOP);
                        	openDownloadFormsOverviewTitle.setTextColor(0xFF000000);
                        	openDownloadFormsOverviewText.setTextColor(0xFF000000);
                        }
                    }, 2000);
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
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        loadConfigButton.setColorFilter(0x22FFFFFF, Mode.SRC_ATOP);
                        loadConfigTitle.setTextColor(0xFF00851B);
                        loadConfigText.setTextColor(0xFF00851B);
                    }
                });
                Intent tki = new Intent();
                tki.setClass(getApplication(), DataCollectorConfigurationActivity.class);
                startActivityForResult(tki, SETTINGSREQUESTCODE);
            }
        };
        
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

        // change elements back
        openFormOverviewButton.setColorFilter(0x00000000, Mode.SRC_ATOP);
        openFormOverviewTitle.setTextColor(0xFF000000);
        openFormOverviewText.setTextColor(0xFF000000);

        openDownloadFormsOverviewButton.setColorFilter(0x00000000, Mode.SRC_ATOP);
        openDownloadFormsOverviewTitle.setTextColor(0xFF000000);
        openDownloadFormsOverviewText.setTextColor(0xFF000000);

        loadConfigButton.setColorFilter(0x00000000, Mode.SRC_ATOP);
        loadConfigTitle.setTextColor(0xFF000000);
        loadConfigText.setTextColor(0xFF000000);
    }

}