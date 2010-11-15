package info.collide.android.scydatacollector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class DataCollectorController {

    private DataCollectorActivity application;

    public static int REQUESTCODECONFIG = 10000;

    public DataCollectorController(DataCollectorActivity application) {
        this.application = application;

        application.addbtnOpenFormOverview(new btnOpenFormOverviewListener());
        application.addbtnOpenDownloadFormOverview(new btnOpenDownloadFormOverviewListener());
        application.addbtnLoadConfig(new btnLoadConfigListener());
        application.addbtnQuit(new btnQuitListener());
    }

    private class btnQuitListener implements OnClickListener {

        public void onClick(View v) {
            application.finish();
        }
    }

    private class btnOpenFormOverviewListener implements OnClickListener {

        public void onClick(View v) {
            Intent tki = new Intent();
            tki.setClass(application, DataCollectorFormOverviewActivity.class);
            application.startActivity(tki);
        }
    }

    private class btnOpenDownloadFormOverviewListener implements OnClickListener {

        public void onClick(View v) {
            if (new DataCollectorConfiguration(application).getServerUrl() == null) {
                Toast.makeText(application, R.string.notconfiguredyet, Toast.LENGTH_LONG).show();
            } else {
                Intent tki = new Intent();
                tki.setClass(application, DownloadFormsOverviewActivity.class);
                application.startActivity(tki);
            }
        }
    }

    private class btnLoadConfigListener implements OnClickListener {

        public void onClick(View v) {
            Intent tki = new Intent();
            tki.setClass(application, DataCollectorConfigurationActivity.class);
            application.startActivityForResult(tki, REQUESTCODECONFIG);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DataCollectorController.REQUESTCODECONFIG) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {

                    application.setTitle(application.getString(R.string.app_name) + " User:" + bundle.getString("configUserName"));
                }
            }
        }
    }
}
