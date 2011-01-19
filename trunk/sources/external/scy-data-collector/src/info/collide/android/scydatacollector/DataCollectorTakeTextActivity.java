package info.collide.android.scydatacollector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DataCollectorTakeTextActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.text);
        
        Bundle extras = getIntent().getExtras();

        Button btnOK = (Button) findViewById(R.id.Button01);
        final EditText txt = (EditText) findViewById(R.id.EditText01);

        txt.setText(extras.getString("datatext"));

        btnOK.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                byte[] tmp = txt.getText().toString().getBytes();
                // this
                // savedInstanceState.putByteArray("datatext", tmp);
                Bundle bundle = new Bundle();
                bundle.putByteArray("datatext", tmp);
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();

            }
        });

    }

}