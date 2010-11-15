package info.collide.android.scydatacollector;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DataCollectorFormActivity extends Activity {

    DataCollectorFormModel dcfm;

    DataCollectorFormController dcc;

    DataCollectorFormView dcfv;

    ProgressDialog mypd;

    final private static String DCFM_TITLE = "dcfm";

    protected static final int GUIUPDATEIDENTIFIER = 99822832;

    // Set up the message handler
    Handler myGUIUpdateHandler = new Handler() {

        // @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GUIUPDATEIDENTIFIER:
                    dcfv.invalidate();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.form);

        dcfm = new DataCollectorFormModel();
        // if (savedInstanceState!=null)
        // try {
        // dcfm.fromByteArray(savedInstanceState.getByteArray(DCFM_TITLE));
        // } catch (StreamCorruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (ClassNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        dcfv = new DataCollectorFormView(dcfm, this);

        dcc = new DataCollectorFormController(dcfm, dcfv, this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dcc.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            dcfm = DataCollectorFormModel.fromByteArray(savedInstanceState.getByteArray(DCFM_TITLE));
            // dcfm.notifyObservers();
            // dcfv = new DataCollectorFormView(dcfm, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /**************************************************
         * Here we do _temporary_ save all critical data, like our selectedProduct.
         **************************************************/
        try {
            outState.putByteArray(DCFM_TITLE, dcfm.toByteArray());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }
}
