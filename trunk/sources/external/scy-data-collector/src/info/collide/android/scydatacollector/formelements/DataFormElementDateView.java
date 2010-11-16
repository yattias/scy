package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class DataFormElementDateView extends DataFormElementView {

    public DataFormElementDateView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);
           
        TextView label = new TextView(getApplication());
        label.setWidth(super.Column1width);

        Button btnGetDate = new Button(getApplication());
        btnGetDate.setText("Date");
        // final EditText txt = new EditText(getApplication());
        Button _btnDetails = new Button(getApplication());
        _btnDetails.setText("Details");
        _btnDetails.setWidth(super.Column5width);
        _btnDetails.setId(id);

        _btnDetails.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        TextView preview = new TextView(getApplication());
        preview.setWidth(super.Column3width + Column4width);

        _btnDetails.setVisibility(INVISIBLE);
        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            preview.setText(tmp);
            _btnDetails.setVisibility(VISIBLE);
        }

        label.setText(dfem.getTitle());

        btnGetDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                Time time = new Time();
                time.setToNow();
                dfem.addStoredData((String.valueOf(time.monthDay) + "." + String.valueOf(time.month) + "." + String.valueOf(time.year)).getBytes());
            }
        });

        addView(label);
        addView(btnGetDate);
        addView(preview);
        addView(_btnDetails);

    }

}
