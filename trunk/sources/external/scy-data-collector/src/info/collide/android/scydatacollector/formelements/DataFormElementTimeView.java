package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DataFormElementTimeView extends DataFormElementView {

    public DataFormElementTimeView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {

        super(dfem, application, dfec);

        TextView label = new TextView(getApplication());
        label.setWidth(super.Column1width);

        Button btnGetDate = new Button(getApplication());
        btnGetDate.setText("Time");
        // final EditText txt = new EditText(getApplication());

        Button _btnDetails = new Button(application);
        _btnDetails.setText("Details");
        _btnDetails.setId(id);
        _btnDetails.setVisibility(INVISIBLE);
        _btnDetails.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        final TextView preview = new TextView(getApplication());
        preview.setWidth(super.Column3width + super.Column4width);

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            preview.setText(tmp + " DS: " + String.valueOf(dfem.getDataList().size() - 1));
        }

        label.setText(dfem.getTitle());

        btnGetDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                Time time = new Time();
                time.setToNow();
                dfem.addStoredData((String.valueOf(time.hour) + ":" + String.valueOf(time.minute) + ":" + String.valueOf(time.second)).getBytes());
                dfec.events(DataFormElementEventTypes.ONAFTER);
            }
        });

        addView(label);
        addView(btnGetDate);
        addView(preview);
        addView(_btnDetails);
    }

}
