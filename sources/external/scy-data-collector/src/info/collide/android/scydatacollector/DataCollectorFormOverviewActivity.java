package info.collide.android.scydatacollector;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class DataCollectorFormOverviewActivity extends ListActivity {

    public final int REFRESH_VIEW = 12345678;
    
    public final static int PROGRESS_DIALOG = 1;

    private DataCollectorFormsDataListAdapter dcfdla;

    private DataCollectorFormOverviewActivity dcfoa = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dcfdla = new DataCollectorFormsDataListAdapter(this, this.getListView());

        setListAdapter(dcfdla);
        if (dcfdla.getCount() == 0) {
            MessageDialog md = new MessageDialog(this);
            android.content.DialogInterface.OnClickListener oclYes = new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dcfoa.finish();
                }
            };

            md.createInfoDialog(getResources().getString(R.string.openFormsNoForms), oclYes);
        }

    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        if (id == PROGRESS_DIALOG) {
            dialog = new ProgressDialog(this);
            dialog.setTitle(R.string.msgSaveToRepository);
            ((ProgressDialog)dialog).setMessage(getString(R.string.msgPleaseWait));
            ((ProgressDialog)dialog).setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        return dialog;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REFRESH_VIEW)
            dcfdla.refreshView();
    }
}
