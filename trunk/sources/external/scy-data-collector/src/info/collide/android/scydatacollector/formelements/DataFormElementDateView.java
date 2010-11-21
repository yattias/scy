package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementDateView extends DataFormElementView {

    public DataFormElementDateView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);

        inflate(getApplication(), R.layout.dateformelement, this);
        
        TextView label = (TextView) findViewById(R.id.dateformelement_label);
        label.setWidth(super.Column1width);
        label.setText(dfem.getTitle());

        ImageButton btnGetDate = (ImageButton) findViewById(R.id.dateformelement_capture_date);
        btnGetDate.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                Time time = new Time();
                time.setToNow();
                dfem.addStoredData(time.format("%d.%m.%Y").getBytes());
            }
        });

        ImageButton buttonDetails = (ImageButton) findViewById(R.id.dateformelement_show_details);
        buttonDetails.setId(id);
        buttonDetails.setVisibility(INVISIBLE);

        buttonDetails.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        TextView preview = (TextView) findViewById(R.id.dateformelement_date);
        preview.setWidth(super.Column3width + Column4width);

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            preview.setText(tmp);
            buttonDetails.setVisibility(VISIBLE);
        }
    }
}