package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataFormElementNumberView extends DataFormElementView {

    public DataFormElementNumberView(final DataFormElementController dfec, final DataFormElementModel dfem, DataCollectorFormActivity application, int id) {
        super(dfem, application, dfec);

        inflate(getApplication(), R.layout.numberformelement, this);
        
        TextView label = (TextView) findViewById(R.id.numberformelement_label);
        label.setWidth(super.Column1width);
        label.setText(dfem.getTitle());

        Button btnOK = (Button) findViewById(R.id.numberformelement_okay);
        btnOK.setText("ok");
        btnOK.setWidth(super.Column3width);
        
        final EditText txt = (EditText) findViewById(R.id.numberformelement_number_field);

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            txt.setText(tmp);
        } else {
            txt.setText("0");
        }
        txt.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt.setWidth(super.Column2width + super.Column3width);

        btnOK.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                dfem.addStoredData(txt.getEditableText().toString().getBytes());
                dfec.events(DataFormElementEventTypes.ONAFTER);
            }
        });
    }

}
