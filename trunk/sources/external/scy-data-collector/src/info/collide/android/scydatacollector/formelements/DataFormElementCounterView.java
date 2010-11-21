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

    public DataFormElementCounterView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);

        inflate(getApplication(), R.layout.counterformelement, this);
        
        TextView label = (TextView) findViewById(R.id.counterformelement_label);
        label.setWidth(super.Column1width);
        label.setText(dfem.getTitle());
        
        EditText txt = (EditText) findViewById(R.id.counterformelement_counter_field);
        txt.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt.setWidth(Column2width);
        txt.setMinimumWidth(Column2width);
        txt.setMaxHeight(RowMaxHeight);

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            txt.setText(tmp);
        } else {
            txt.setText("0");
        }

        ImageButton buttonInc = (ImageButton) findViewById(R.id.counterformelement_increase);
        buttonInc.setMinimumWidth(Column3width);
        
        ImageButton buttonDec = (ImageButton) findViewById(R.id.counterformelement_decrease);
        buttonDec.setMinimumWidth(Column4width);

        ImageButton buttonDetails = new ImageButton(application);
        buttonDetails.setId(id);
        buttonDetails.setMinimumWidth(Column5width);
        
        if (dfem.getDataList().size() < 1) {
            buttonDetails.setVisibility(INVISIBLE);
        } else {
            buttonDetails.setVisibility(VISIBLE);
        }
        
        buttonDetails.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        buttonInc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // try {
                String tmp = "0";
                if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
                    tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));

                }

                dfec.events(DataFormElementEventTypes.ONBEFORE);

                int counter = Integer.parseInt(tmp);
                counter++;

                dfem.addStoredData((String.valueOf(counter)).getBytes());
                vibrate(25);
                dfec.events(DataFormElementEventTypes.ONAFTER);
            }

        });
        buttonDec.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String tmp = "0";
                if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
                    tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));

                }
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                int counter = Integer.parseInt(tmp);
                counter--;
                dfem.addStoredData((String.valueOf(counter)).getBytes());
                vibrate(25);
                dfec.events(DataFormElementEventTypes.ONAFTER);
            }
        });
    }

    private static void vibrate(int i) {
        Vibrator vib = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(i);
    }

}
