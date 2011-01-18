package info.collide.android.scydatacollector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DataCollectorConfigurationActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.config);

        Button btnConfigClose = (Button) findViewById(R.id.btnConfigClose);
        final EditText etUserName = (EditText) findViewById(R.id.etUserName);
        final EditText etServerUrl = (EditText) findViewById(R.id.etServerUrl);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etGroupname = (EditText) findViewById(R.id.etGroupName);

        final DataCollectorConfiguration config = new DataCollectorConfiguration(this);

        etServerUrl.setText(config.getServerUrl());
        etUserName.setText(config.getUserName());
        etPassword.setText(config.getPassword());
        etGroupname.setText(config.getGroupname());

        btnConfigClose.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                config.setServerUrl(etServerUrl.getText().toString());
                config.setUserName(etUserName.getText().toString());
                config.setGroupname(etGroupname.getText().toString());
                config.setPassword(etPassword.getText().toString());

                Intent result = new Intent();
                result.putExtra("configUserName", etUserName.getText().toString());
                setResult(DataCollectorActivity.SETTINGSREQUESTCODE, result);
                finish();
            }
        });

    }
}