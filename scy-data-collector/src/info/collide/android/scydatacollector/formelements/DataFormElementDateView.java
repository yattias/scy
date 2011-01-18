package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementDateView extends DataFormElementView {

    private TextView previewView;
    private ImageButton detailsButton;

    public DataFormElementDateView(final DataFormElementModel elementModel, final DataCollectorFormActivity application, final int id) {
        super(elementModel, application);

        inflate(getApplication(), R.layout.dateformelement, this);
        
        TextView label = (TextView) findViewById(R.id.dateformelement_label);
        label.setWidth(super.Column1width);
        label.setText(elementModel.getTitle());

        ImageButton btnGetDate = (ImageButton) findViewById(R.id.dateformelement_capture_date);
        btnGetDate.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                events(DataFormElementEventTypes.ONBEFORE);
                Time time = new Time();
                time.setToNow();
                elementModel.addStoredData(time.format("%d.%m.%Y").getBytes());
            }
        });

        detailsButton = (ImageButton) findViewById(R.id.dateformelement_show_details);
        detailsButton.setVisibility(INVISIBLE);

        detailsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                openDetail(application, id);
            }
        });

        previewView = (TextView) findViewById(R.id.dateformelement_date);
        previewView.setWidth(super.Column3width + Column4width);

        updateView(elementModel);
    }

    @Override
    protected void updateView(DataFormElementModel elementModel) {
        if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
            String tmp = new String(elementModel.getStoredData(elementModel.getDataList().size() - 1));
            previewView.setText(tmp);
            detailsButton.setVisibility(VISIBLE);
        }
    }
}