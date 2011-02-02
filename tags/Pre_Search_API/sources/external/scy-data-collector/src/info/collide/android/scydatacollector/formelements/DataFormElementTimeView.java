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

    private TextView previewField;
    private ImageButton detailsButton;

    public DataFormElementTimeView(final DataFormElementModel elementModel, final DataCollectorFormActivity application, final int id) {

        super(elementModel, application);

        inflate(getApplication(), R.layout.timeformelement, this);
        
        TextView label = (TextView) findViewById(R.id.timeformelement_label);
        label.setWidth(super.Column1width);
        label.setText(elementModel.getTitle());

        ImageButton btnGetDate = (ImageButton) findViewById(R.id.timeformelement_capture_time);

        detailsButton = (ImageButton) findViewById(R.id.timeformelement_show_details);
        detailsButton.setVisibility(INVISIBLE);
        detailsButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                openDetail(application, id);
            }
        });

        previewField = (TextView) findViewById(R.id.timeformelement_time);
        previewField.setWidth(super.Column3width + super.Column4width);

        btnGetDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                events(DataFormElementEventTypes.ONBEFORE);
                Time time = new Time();
                time.setToNow();
                elementModel.addStoredData(time.format("%H:%M:%S").getBytes());
                events(DataFormElementEventTypes.ONAFTER);
                detailsButton.setVisibility(VISIBLE);
            }
        });
        updateView(elementModel);
    }

    @Override
    protected void updateView(DataFormElementModel elementModel) {
        if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
            String timeString = new String(elementModel.getStoredData(elementModel.getDataList().size() - 1));
            previewField.setText(timeString);
            detailsButton.setVisibility(VISIBLE);
        }
    }

}
