package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementTimeView extends DataFormElementView {

    public DataFormElementTimeView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {

        super(dfem, application, dfec);

        inflate(getApplication(), R.layout.timeformelement, this);
        
        TextView label = (TextView) findViewById(R.id.timeformelement_label);
        label.setWidth(super.Column1width);
        label.setText(dfem.getTitle());

        ImageButton btnGetDate = (ImageButton) findViewById(R.id.timeformelement_capture_time);

        final ImageButton buttonDetails = (ImageButton) findViewById(R.id.timeformelement_show_details);
        buttonDetails.setId(id);
        buttonDetails.setVisibility(INVISIBLE);
        buttonDetails.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        final TextView preview = (TextView) findViewById(R.id.timeformelement_time);
        preview.setWidth(super.Column3width + super.Column4width);

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String timeString = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            preview.setText(timeString);
            buttonDetails.setVisibility(VISIBLE);
        }

        btnGetDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                Time time = new Time();
                time.setToNow();
                dfem.addStoredData(time.format("%H:%M:%S").getBytes());
                dfec.events(DataFormElementEventTypes.ONAFTER);
                buttonDetails.setVisibility(VISIBLE);
            }
        });
    }

}
