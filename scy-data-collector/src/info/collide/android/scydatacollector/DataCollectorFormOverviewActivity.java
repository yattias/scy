package info.collide.android.scydatacollector;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class DataCollectorFormOverviewActivity extends ListActivity {

    public final int REFRESH_VIEW = 12345678;

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REFRESH_VIEW)
            dcfdla.refreshView();
    }
}
