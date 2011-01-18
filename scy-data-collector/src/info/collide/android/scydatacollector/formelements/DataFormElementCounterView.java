package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.content.Context;
import android.os.Vibrator;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementCounterView extends DataFormElementView {

    private ImageButton buttonDetails;
    private EditText counterField;

    public DataFormElementCounterView(final DataFormElementModel elementModel, final DataCollectorFormActivity application, final int id) {
        super(elementModel, application);

        inflate(getApplication(), R.layout.counterformelement, this);
        
        TextView label = (TextView) findViewById(R.id.counterformelement_label);
        label.setWidth(super.Column1width);
        label.setText(elementModel.getTitle());
        
        counterField = (EditText) findViewById(R.id.counterformelement_counter_field);
        counterField.setInputType(InputType.TYPE_CLASS_NUMBER);
        counterField.setWidth(Column2width);
        counterField.setMinimumWidth(Column2width);
        counterField.setMaxHeight(RowMaxHeight);

        ImageButton buttonInc = (ImageButton) findViewById(R.id.counterformelement_increase);
        buttonInc.setMinimumWidth(Column3width);
        
        ImageButton buttonDec = (ImageButton) findViewById(R.id.counterformelement_decrease);
        buttonDec.setMinimumWidth(Column4width);

        buttonDetails = new ImageButton(application);
        buttonDetails.setMinimumWidth(Column5width);
        
        buttonDetails.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                openDetail(application, id);
            }
        });

        buttonInc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String tmp = "0";
                if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
                    tmp = new String(elementModel.getStoredData(elementModel.getDataList().size() - 1));
                }

                events(DataFormElementEventTypes.ONBEFORE);

                int counter = Integer.parseInt(tmp);
                counter++;

                elementModel.addStoredData((String.valueOf(counter)).getBytes());
                vibrate(25);
                events(DataFormElementEventTypes.ONAFTER);
            }

        });
        buttonDec.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String tmp = "0";
                if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
                    tmp = new String(elementModel.getStoredData(elementModel.getDataList().size() - 1));

                }
                events(DataFormElementEventTypes.ONBEFORE);
                int counter = Integer.parseInt(tmp);
                counter--;
                elementModel.addStoredData((String.valueOf(counter)).getBytes());
                vibrate(25);
                events(DataFormElementEventTypes.ONAFTER);
            }
        });
        updateView(elementModel);
    }

    private void vibrate(int i) {
        Vibrator vib = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(i);
    }

    @Override
    protected void updateView(DataFormElementModel elementModel) {
        if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
            String tmp = new String(elementModel.getStoredData(elementModel.getDataList().size() - 1));
            counterField.setText(tmp);
        } else {
            counterField.setText("0");
        }
        if (elementModel.getDataList().size() < 1) {
            buttonDetails.setVisibility(INVISIBLE);
        } else {
            buttonDetails.setVisibility(VISIBLE);
        }
    }

}
